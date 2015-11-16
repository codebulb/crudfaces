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

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.focus.Focus;
import org.primefaces.expression.SearchExpressionFacade;

/**
 * <p>
 * The PrimeFaces
 * <code><a href="http://www.primefaces.org/showcase/ui/misc/focus.xhtml">focus</a></code>'s
 * default renderer is overridden with a custom one. It changes the behavior as
 * follows:</p>
 * <ul>
 * <li>Supports setting a button as <code>for</code> component, not just input
 * components.</li>
 * <li>Even if an explicit <code>for</code> component is set, it still respects
 * validation errors and would set focus to an invalid component if such is
 * present, ignoring the explicit <code>for</code> component. Originally, this
 * works the other way around which seems counter-intuitive. Typically, one
 * would always want to respect validation outcome when setting the focus.</li>
 * <li>Text in selected input component gets "selected", i.e. the entire text is
 * selected (in the default configuration, no text was selected, and the input
 * cursor was at position 0). This is especially useful when the focus is set as
 * a consequence of a failed validation: The user can delete the text simply by
 * hitting the delete key, or insert more text at the end of existing text
 * immediately. It also matches the browser's natural behavior on component
 * navigation with the tab key.</li>
 * </ul>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class FocusRenderer extends org.primefaces.component.focus.FocusRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        //// Original code below
        Focus focus = (Focus) component;
        ResponseWriter writer = context.getResponseWriter();

        //Dummy markup for ajax update
        writer.startElement("span", focus);
        writer.writeAttribute("id", focus.getClientId(context), "id");
        writer.endElement("span");

        writer.startElement("script", focus);
        writer.writeAttribute("type", "text/javascript", null);

        //// Original code above
        String invalidClientId = findFirstInvalidComponentClientId(context, focus);
        if (invalidClientId != null || focus.getFor() == null) {
            encodeImplicitFocus(context, focus);
        }
        else {
            encodeExplicitFocus(context, focus);
        }
        //// Original code below

        writer.endElement("script");
    }
    
    @Override
    protected void encodeImplicitFocus(FacesContext context, Focus focus) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String invalidClientId = findFirstInvalidComponentClientId(context, focus);

        writer.write("$(function(){");

        if (invalidClientId != null) {
            writer.write("PrimeFaces.focus('" + invalidClientId + "');");
            //// Original code above
            
            // don't forget to replace colons with two backslashes (the Java regex looks weird though)
            writer.write("CrudFaces.focus('" + invalidClientId.replaceAll(":", "\\\\\\\\:") + "');");
            //// Original code below
        } else if (focus.getContext() != null) {
            UIComponent focusContext = SearchExpressionFacade.resolveComponent(
                    context, focus, focus.getContext());

            writer.write("PrimeFaces.focus(null, '" + focusContext.getClientId(context) + "');");
        } else {
            writer.write("PrimeFaces.focus();");
        }

        writer.write("});");
    }
    
    @Override
    protected void encodeExplicitFocus(FacesContext context, Focus focus) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            UIComponent forComponent = SearchExpressionFacade.resolveComponent(
                            context, focus, focus.getFor());

            String clientId = forComponent.getClientId(context);

            writer.write("$(function(){");
            writer.write("PrimeFaces.focus('" + clientId +"');");
            //// Original code above
            
            // don't forget to replace colons with two backslashes (the Java regex looks weird though)
            writer.write("CrudFaces.focus('" + clientId.replaceAll(":", "\\\\\\\\:") + "');");
            //// Original code below

            writer.write("});");
    }
}
