/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.system;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.logic.common.DisplayNameComponent;
import org.terasology.engine.rendering.assets.font.Font;
import org.terasology.engine.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.engine.rendering.nui.widgets.browser.data.basic.FlowParagraphData;
import org.terasology.engine.rendering.nui.widgets.browser.data.basic.flow.TextFlowRenderable;
import org.terasology.engine.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.ContainerInteger;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.FixedContainerInteger;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.TextRenderStyle;
import org.terasology.engine.utilities.Assets;
import org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry;
import org.terasology.inGameHelpAPI.components.HelpItem;
import org.terasology.inGameHelpAPI.components.ItemHelpComponent;
import org.terasology.inGameHelpAPI.systems.HelpCategory;
import org.terasology.inGameHelpAPI.ui.ItemWidget;
import org.terasology.inGameHelpAPI.ui.WidgetFlowRenderable;

import java.util.Map;

public class WorkstationCraftingCategory implements HelpCategory {
    private final String name = "WorkstationCrafting";
    private ItemsCategoryInGameHelpRegistry itemsCategoryInGameHelpRegistry;
    Map<String, DocumentData> items = Maps.newHashMap();
    HTMLDocument rootDocument;
    DocumentData currentDocument;

    public WorkstationCraftingCategory() {
    }

    public WorkstationCraftingCategory(ItemsCategoryInGameHelpRegistry itemsCategoryInGameHelpRegistry) {
        this.itemsCategoryInGameHelpRegistry = itemsCategoryInGameHelpRegistry;
    }

    public void setRegistry(ItemsCategoryInGameHelpRegistry reg) {
        this.itemsCategoryInGameHelpRegistry = reg;
    }

    private void initialise() {
        TextRenderStyle titleRenderStyle = new TextRenderStyle() {
            @Override
            public Font getFont(boolean hyperlink) {
                return Assets.getFont("engine:NotoSans-Regular-Title").get();
            }
        };
        ParagraphRenderStyle titleParagraphStyle = new ParagraphRenderStyle() {
            @Override
            public ContainerInteger getParagraphPaddingTop() {
                return new FixedContainerInteger(5);
            }
        };


        rootDocument = new HTMLDocument(null);
        FlowParagraphData itemListParagraph = new FlowParagraphData(null);
        rootDocument.addParagraph(itemListParagraph);

        for (Prefab itemPrefab : itemsCategoryInGameHelpRegistry.getKnownPrefabs()) {
            HTMLDocument documentData = new HTMLDocument(null);
            ItemHelpComponent helpComponent = itemPrefab.getComponent(ItemHelpComponent.class);
            if (helpComponent == null) {
                helpComponent = new ItemHelpComponent();
                helpComponent.paragraphText.add("An unknown item.");
            }

            if (getCategoryName().equalsIgnoreCase(helpComponent.getCategory())) {
                FlowParagraphData imageNameParagraph = new FlowParagraphData(null);
                documentData.addParagraph(imageNameParagraph);
                imageNameParagraph.append(new WidgetFlowRenderable(new ItemWidget(itemPrefab.getName()), 48, 48, itemPrefab.getName()));
                DisplayNameComponent displayNameComponent = itemPrefab.getComponent(DisplayNameComponent.class);
                if (displayNameComponent != null) {
                    imageNameParagraph.append(new TextFlowRenderable(displayNameComponent.name, titleRenderStyle, null));
                } else {
                    imageNameParagraph.append(new TextFlowRenderable(itemPrefab.getName(), titleRenderStyle, null));
                }

                helpComponent.addHelpItemSection(documentData);

                // add all the other ones from components
                for (HelpItem helpItem : Iterables.filter(itemPrefab.iterateComponents(), HelpItem.class)) {
                    if (helpItem != helpComponent) {
                        helpItem.addHelpItemSection(documentData);
                    }
                }

                // add all the other ones from code registered HelpItems
                for (HelpItem helpItem : itemsCategoryInGameHelpRegistry.getHelpItems(itemPrefab)) {
                    helpItem.addHelpItemSection(documentData);
                }

                items.put(itemPrefab.getName(), documentData);

                // add this to the root document
                itemListParagraph.append(new WidgetFlowRenderable(new ItemWidget(itemPrefab.getName()), 48, 48, itemPrefab.getName()));
            }
        }
    }

    @Override
    public String getCategoryName() {
        return name;
    }

    @Override
    public DocumentData getDocumentData() {
        initialise();
        if (currentDocument == null) {
            return rootDocument;
        } else {
            return currentDocument;
        }
    }

    @Override
    public void resetNavigation() {
        currentDocument = null;
    }

    @Override
    public boolean handleNavigate(String hyperlink) {
        if (items.size() == 0) {
            // handle the case where we navigate before we have shown the screen.  There is probably a better way to do this.
            initialise();
        }

        if (items.containsKey(hyperlink)) {
            currentDocument = items.get(hyperlink);
            return true;
        } else {
            return false;
        }
    }
}
