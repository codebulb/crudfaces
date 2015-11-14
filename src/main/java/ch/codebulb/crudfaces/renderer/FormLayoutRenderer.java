/*
 * Copyright 2015 CrudFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ch.codebulb.crudfaces.renderer;

import ch.codebulb.crudfaces.component.FormLayout;
import ch.codebulb.crudfaces.util.ComponentsHelper;
import ch.codebulb.crudfaces.util.StringsHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIMessage;
import javax.faces.component.UIPanel;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutcomeTargetButton;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.expression.SearchExpressionFacade;

/**
 * The JSF component renderer for {@link FormLayout}.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
@FacesRenderer(componentFamily = UIPanel.COMPONENT_FAMILY, rendererType = FormLayoutRenderer.RENDERER_TYPE)
public class FormLayoutRenderer extends Renderer {

    public static final String RENDERER_TYPE = "ch.codebulb.FormLayout";
    private BootstrapFormLayoutProvider formLayoutProvider = new BootstrapFormLayoutProvider();
    
    int currentGroup;
    int currentGroupColumn;
    String[] groupColumnClasses;
    int[] groupRatios;
    
    protected Map<UIComponent, UIMessage> componentsWithAttachedMessages = new HashMap<>();
    protected Map<String, UIComponent> buttons = new LinkedHashMap<>();
    
    private int currentColspan = 1;

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        
        FormLayout formLayout = (FormLayout) component;
        initComponent(formLayout);
        
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", null);
        writer.writeAttribute("id", formLayout.getClientId(context), null);
        
        
        String styleClass = formLayoutProvider.getContainerStyleClass();
        StringsHelper.Joiner styleClasses = StringsHelper.join(" ").add(styleClass);
        styleClasses.add(formLayout.getStyleClass());
        
        writer.writeAttribute("class", styleClasses.add("cf-formlayout").toString(), null);
        
        if (formLayout.getStyle() != null) {
            writer.writeAttribute("style", formLayout.getStyle(), null);
        }
    }
    
    public void initComponent(FormLayout component) {
        componentsWithAttachedMessages = new HashMap<>();
        buttons = new LinkedHashMap<>();
        
        currentGroup = 0;
        currentGroupColumn = 0;
        
        String[] groupRatiosString = component.getGroupRatios().split(" ");
        groupRatios = new int[groupRatiosString.length];
        groupColumnClasses = new String[groupRatiosString.length];
        for (int i = 0; i < groupRatiosString.length; i++) {
            groupRatios[i] = Integer.parseInt(groupRatiosString[i]);
            groupColumnClasses[i] = formLayoutProvider.createUnits(groupRatios[i]).getStyleClasses();
        }        
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (component.getChildCount() > 0) {
            assignMessages(component, context);
            
            for (UIComponent child : component.getChildren()) {
                encodeChild(context, (FormLayout) component, child);
            }
        }
    }

    private void assignMessages(UIComponent component, FacesContext context) {
        // get message components
        for (UIComponent child : component.getChildren()) {
            if (child instanceof UIMessage) {
                UIComponent target = SearchExpressionFacade.resolveComponent(context, child, ((UIMessage) child).getFor());
                componentsWithAttachedMessages.put(target, (UIMessage) child);
            }
        }
    }
    
    private void encodeChild(FacesContext context, FormLayout component, UIComponent child) throws IOException {
        if (isButton(child)) {
            buttons.put(child.getClientId(), child);
        }
        
        if (isMessage(child) || isButton(child)) {
            return;
        }
        
        ResponseWriter writer = context.getResponseWriter();

        if (currentGroup == 0 && currentGroupColumn == 0) {
            writer.startElement("div", null);
            writer.writeAttribute("class", StringsHelper.join(" ").add(formLayoutProvider.getRowStyleClass()).add("cf-formlayout-row").toString(), null);
        }

        if (currentGroupColumn == 0) {
            writer.startElement("div", null);
            writer.writeAttribute("class", StringsHelper.join(" ").add(formLayoutProvider.getGroupStyleClass())
                    .add(formLayoutProvider.createUnitsForXLarge(formLayoutProvider.getResolution() / component.getGroups()).getStyleClasses())
                    .add("cf-formlayout-group").toString(), null);
        }

        writer.startElement("div", null);

        String cellStyleClass = groupColumnClasses[currentGroupColumn];

        writer.writeAttribute("class", StringsHelper.join(" ").add(cellStyleClass).add("cf-formlayout-cell").add(formLayoutProvider.getComponentClass(child)).toString(), null);

        UIMessage attachedMessage = componentsWithAttachedMessages.get(child);

        if (attachedMessage == null) {
            child.encodeAll(context);
        }
        else {
            encodeCombinedComponentWithMessage(child, attachedMessage, context);
        }

        writer.endElement("div");

        currentGroupColumn = currentGroupColumn + currentColspan;
        if (currentGroupColumn >= groupColumnClasses.length) {
            currentGroup = (currentGroup + 1) % component.getGroups();
            currentGroupColumn = 0;
        }

        if (currentGroupColumn == 0) {
            writer.endElement("div");
        }

        if (currentGroup == 0 && currentGroupColumn == 0) {
            writer.endElement("div");
        } 
    }
    
    private boolean isButton(UIComponent child) {
        return ((child instanceof HtmlCommandButton || child instanceof CommandButton || 
                HtmlOutcomeTargetButton.class.isAssignableFrom(child.getClass()) ||
                HtmlCommandLink.class.isAssignableFrom(child.getClass()))
                || ComponentsHelper.<Boolean>getPassThroughAttribute(child, "buttonBar", Boolean.class))
                && !ComponentsHelper.getPassThroughAttribute(child, "inline", Boolean.class);
    }
    
    private boolean isMessage(UIComponent child) {
        return child instanceof UIMessage;
    }

    private void encodeCombinedComponentWithMessage(UIComponent child, UIMessage attachedMessage, FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", null);
        String invalidPrefix = "";
        if (child instanceof UIInput && !((UIInput)child).isValid()) {
           invalidPrefix = formLayoutProvider.getMessageComponentSubCellStyleClass();
        }
        writer.writeAttribute("class", invalidPrefix + "cf-formlayout-componentcell", null);
        child.encodeAll(context);
        writer.endElement("div");

        writer.startElement("div", null);
        if (child instanceof UIInput && !((UIInput)child).isValid()) {
           invalidPrefix = formLayoutProvider.getMessageSubCellStyleClass();
        }
        writer.writeAttribute("class", invalidPrefix + "cf-formlayout-messagecell", null);
        attachedMessage.encodeAll(context);
        writer.endElement("div");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
        
        FormLayout formLayout = (FormLayout) component;
        
        ResponseWriter writer = context.getResponseWriter();
        
        int firstColspan = 1;
        if (groupRatios != null) {
            firstColspan = groupRatios[0] / formLayout.getGroups();
        }
        formLayoutProvider.writeButtonBar(context, true, firstColspan, buttons.values());
        
        writer.endElement("div");
    }
}
