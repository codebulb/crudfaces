/*
 * Copyright 2015 CrudFaces / Nicolas Hofstetter (codebulb.ch).
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
import ch.codebulb.crudfaces.component.IncludeMainCss;
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
import javax.faces.component.UISelectBoolean;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutcomeTargetButton;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import org.omnifaces.util.Faces;
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
    
    /**
     * Global style class override for invalid component cell styling
     */
    public static String MESSAGE_COMPONENT_SUBCELL_STYLE_CLASS = null;
    /**
     * Global style class override for invalid component message cell styling
     */
    public static String MESSAGE_SUBCELL_STYLE_CLASS = null;
    
    private BootstrapFormLayoutProvider formLayoutProvider = new BootstrapFormLayoutProvider();
    
    private int[] groupRatios;
    private FormLayoutModel model;
    
    protected Map<UIComponent, UIMessage> componentsWithAttachedMessages = new HashMap<>();
    protected Map<String, UIComponent> buttons = new LinkedHashMap<>();
    protected Map<UISelectBoolean, HtmlOutputLabel> checkboxesWithLabels = new LinkedHashMap<>();

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
        
        String[] groupRatiosString = component.getGroupRatios().split(" ");
        groupRatios = new int[groupRatiosString.length];
        for (int i = 0; i < groupRatiosString.length; i++) {
            groupRatios[i] = Integer.parseInt(groupRatiosString[i]);
        }
        
        model = new FormLayoutModel(component.getGroups(), groupRatios, formLayoutProvider.getResolution());
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (component.getChildCount() > 0) {
            findSpecialComponents((FormLayout) component, context);
            
            for (UIComponent child : component.getChildren()) {
                if (shouldRender(child)) {
                    model.add(child);
                }
            }
            
            encode((FormLayout) component, context);
        }
    }

    private void findSpecialComponents(FormLayout formLayout, FacesContext context) {
        for (UIComponent child : formLayout.getChildren()) {
            if (isMessage(child)) {
                UIComponent target = SearchExpressionFacade.resolveComponent(context, child, ((UIMessage) child).getFor());
                componentsWithAttachedMessages.put(target, (UIMessage) child);
            }
            else if(isButton(child)) {
                buttons.put(child.getClientId(), child);
            }
            
            if (formLayout.isCheckboxLabelsInline()) {
                if(isLabel(child)) {
                    UIComponent target = SearchExpressionFacade.resolveComponent(context, child, ((HtmlOutputLabel) child).getFor());
                    if (isCheckbox(target)) {
                        checkboxesWithLabels.put((UISelectBoolean)target, (HtmlOutputLabel) child);
                    }
                }
            }
        }
    }
    
    private void encode(FormLayout formLayout, FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        for (FormLayoutModel.Row row : model.getRows()) {
            writer.startElement("div", null);
            writer.writeAttribute("class", StringsHelper.join(" ").add(formLayoutProvider.getRowStyleClass()).add("cf-formlayout-row").toString(), null);
            
            for (FormLayoutModel.Group group : row.getGroups()) {
                writer.startElement("div", null);
                writer.writeAttribute("class", StringsHelper.join(" ").add(formLayoutProvider.getGroupStyleClass())
                    .add(formLayoutProvider.createUnitsForXLarge(formLayoutProvider.getResolution() / formLayout.getGroups() * group.getRatio()).getStyleClasses())
                    .add("cf-formlayout-group").toString(), null);
                
                for (FormLayoutModel.Comp comp : group.getComps()) {
                    UIComponent component = comp.component;
                    
                    writer.startElement("div", null);

                    String cellStyleClass = formLayoutProvider.createUnits(comp.getOriginalRatio(), comp.getRatio()).getStyleClasses();

                    writer.writeAttribute("class", StringsHelper.join(" ").
                            add(cellStyleClass).add("cf-formlayout-cell").add(formLayoutProvider.getComponentClass(component)).toString(), null);

                    UIMessage attachedMessage = componentsWithAttachedMessages.get(component);

                    if (attachedMessage == null) {
                        encodeComponent(component, formLayout, context);
                    }
                    else {
                        encodeCombinedComponentWithMessage(component, formLayout, attachedMessage, context);
                    }

                    // end comp
                    writer.endElement("div");
                }
                
                // end group
                writer.endElement("div");
            }
            
            // end row
            writer.endElement("div");
        }
    }
    
    private static boolean shouldRender(UIComponent child) {
        return !(isMessage(child) || isButton(child));
    }
    
    private static boolean isButton(UIComponent child) {
        return ((child instanceof HtmlCommandButton || child instanceof CommandButton || 
                HtmlOutcomeTargetButton.class.isAssignableFrom(child.getClass()) ||
                HtmlCommandLink.class.isAssignableFrom(child.getClass()))
                || ComponentsHelper.<Boolean>getPassThroughAttribute(child, "buttonBar", Boolean.class))
                && !ComponentsHelper.getPassThroughAttribute(child, "inline", Boolean.class);
    }
    
    private static boolean isMessage(UIComponent child) {
        return child instanceof UIMessage;
    }
    
    static boolean isCheckbox(UIComponent child) {
        return child instanceof UISelectBoolean;
    }
    
    private static boolean isLabel(UIComponent child) {
        return child instanceof HtmlOutputLabel;
    }
    
    static int getColspan(UIComponent child) {
        int ret = ComponentsHelper.getPassThroughAttribute(child, "colspan", Integer.class);
        if (ret == 0) {
            return 1;
        }
        else {
            return ret;
        }
    }

    private void encodeCombinedComponentWithMessage(UIComponent child, FormLayout formLayout, UIMessage attachedMessage, FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", null);
        String invalidStyleClass = null;
        if (child instanceof UIInput && !((UIInput)child).isValid()) {
            invalidStyleClass = MESSAGE_COMPONENT_SUBCELL_STYLE_CLASS != null ? MESSAGE_COMPONENT_SUBCELL_STYLE_CLASS : 
                    formLayoutProvider.getMessageComponentSubCellStyleClass();
        }
        writer.writeAttribute("class", StringsHelper.join(" ").add(invalidStyleClass).add("cf-formlayout-componentcell").toString(), null);
        encodeComponent(child, formLayout, context);
        writer.endElement("div");

        writer.startElement("div", null);
        if (child instanceof UIInput && !((UIInput)child).isValid()) {
            invalidStyleClass = MESSAGE_COMPONENT_SUBCELL_STYLE_CLASS != null ? MESSAGE_SUBCELL_STYLE_CLASS : 
                    formLayoutProvider.getMessageSubCellStyleClass();
        }
        writer.writeAttribute("class", StringsHelper.join(" ").add(invalidStyleClass).add("cf-formlayout-messagecell").toString(), null);
        attachedMessage.encodeAll(context);
        writer.endElement("div");
    }
    
    private void encodeComponent(UIComponent child, FormLayout formLayout, FacesContext context) throws IOException {
        if (formLayout.isCheckboxLabelsInline()) {
            if (isCheckbox(child)) {
                // put checkbox and label in a containing div
                ResponseWriter writer = context.getResponseWriter();
                writer.startElement("div", null);
                child.encodeAll(context);
                checkboxesWithLabels.get(child).encodeAll(context);
                writer.endElement("div");
                return;
            }
            else if(isLabel(child)) {
                // don't render the label separately
                if (checkboxesWithLabels.values().contains(child)) {
                    return;
                }
            }
        }
        child.encodeAll(context);
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
        formLayoutProvider.writeButtonBar(context, true, firstColspan, formLayout.getGroups(), buttons.values());
        
        writer.endElement("div");
    }
}
