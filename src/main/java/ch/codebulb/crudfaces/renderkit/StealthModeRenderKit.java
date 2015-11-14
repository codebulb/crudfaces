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
package ch.codebulb.crudfaces.renderkit;

import ch.codebulb.crudfaces.util.ComponentsHelper;
import java.io.IOException;
import java.io.Writer;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;
import org.omnifaces.util.Components;

/**
 * <p>
 * The JSF render kit which enables <code>a:stealth</code>, a globally available
 * component attribute which can be used as an alternative to the rendered
 * attribute, hiding the component just as rendered does, but with the ability
 * to reference to it and un-hide it without the need of an umbrella component.
 * </p>
 * <p>
 * In JSF (and PrimeFaces), the <code>rendered</code> attribute is used to
 * conditionally hide a component (i.e. not render it in the HTML outcome). A
 * <a href="http://stackoverflow.com/q/9010734/1399395">well-known problem</a>
 * with this feature is that as the component in question is completely removed
 * from the output component tree, there's no way to retrieve it again; most
 * notably, it cannot be referenced in an ajax update in order to notify it that
 * its <code>rendered</code> value has changed to <code>true</code> again. In
 * practice, one needs to reference to a parent component which will then
 * trigger re-rendering itself and of all of its child components. This is
 * cumbersome and can lead to undesired side effects.</p><p>
 * When using <code>a:stealth</code> instead of <code>rendered</code>, instead
 * of completely removing the component in question from the view, an empty
 * <code>&lt;span&gt;</code> tag will be rendered, with the <code>id</code>
 * preserved, so it can still be properly referenced in e.g. an ajax
 * <code>update</code> attribute.</p><p>
 * The attribute itself is realized as a so-called pass through attribute, as
 * introduced with JSF 2.2, belonging to the
 * <code>xmlns:a="http://xmlns.jcp.org/jsf/passthrough"</code> namespace. As
 * these pass through attributes are ignored by the XML validator, it won't
 * introduce nasty HTML warnings in your editor because of an unknown HTML
 * attribute.</p><p>
 * However, the attribute can also be applied as a true JSF attribute, making it
 * eligible for use with OmniFaces'
 * <code>&lt;o:massAttribute&gt;</code> component.</p>
 * <p>
 * <b>Note:</b> Semantically, the <code>a:stealth</code> attribute works like a
 * negated
 * <code>rendered</code> attribute would, i.e. <code>rendered="true"</code> is
 * <code>stealth="false"</code>, and vice versa. However, it accords better with
 * the semantics of the <code>disabled</code> attribute.</p><p>
 * Behind the scenes, the <code>a:stealth</code> attribute is interpreted by the
 * special <code>StealthModeRenderKit</code>. As this render kit has no other
 * side effects, it is activated by default.</p>
 *
                    <h2>Usage</h2>
 * <p>
 * Declare the <code>xmlns:a="http://xmlns.jcp.org/jsf/passthrough"</code>
 * namespace and set <code>a:stealth="true"</code> on any component to hide it
 * and its children and render an empty span tag instead.</p>
 *
                    <h2>Known restrictions</h2>
 * <ul>
 * <li>Child components of a stealth component cannot be un-hidden again. As a
 * workaround, use OmniFaces' <code>&lt;o:massAttribute&gt;</code> to apply
 * stealth to multiple components instead of applying it to their common parent
 * component.</li>
 * </ul>
 * <p>
 * These restrictions are most likely to be addressed and resolved in future
 * CrudFaces versions.</p>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class StealthModeRenderKit extends RenderKitWrapper {
    private RenderKit wrapped;

    public StealthModeRenderKit(RenderKit wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String characterEncoding) {
        return new InnerResponseWriter(super.createResponseWriter(writer, contentTypeList, characterEncoding));
    }

    @Override
    public RenderKit getWrapped() {
        return wrapped;
    }
    
    class InnerResponseWriter extends ResponseWriterWrapper {

        private ResponseWriter wrapped;
        private boolean stealth;

        public InnerResponseWriter(ResponseWriter wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public ResponseWriter cloneWithWriter(Writer writer) {
            return new InnerResponseWriter(super.cloneWithWriter(writer));
        }

        @Override
        public void startElement(String name, UIComponent component) throws IOException {
            if (stealth) {
                return;
            }
            
            UIComponent candidate = component;
            if (component == null && !("changes".equals(name) || "update".equals(name))) {
                candidate = Components.getCurrentComponent();
            }
            
            if (candidate != null) {
                stealth = isStealth(candidate);
                if (stealth) {
                    candidate.getChildren().clear();
                    super.startElement("span", null);
                    return;
                }
            }
            super.startElement(name, component);
        }

        @Override
        public void endElement(String name) throws IOException {
            if (stealth == true) {
                stealth = false;
                super.endElement("span");
                return;
            }
            super.endElement(name);
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            if (stealth) {
                return;
            }
            super.write(cbuf, off, len);
        }
        
        

        @Override
        public void writeText(char[] text, int off, int len) throws IOException {
            if (stealth) {
                return;
            }
            super.writeText(text, off, len);
        }

        @Override
        public void writeText(Object text, UIComponent component, String property) throws IOException {
            if (stealth) {
                return;
            }
            super.writeText(text, component, property);
        }

        @Override
        public void writeText(Object text, String property) throws IOException {
            if (stealth) {
                return;
            }
            super.writeText(text, property);
        }

        @Override
        public void writeAttribute(String name, Object value, String property) throws IOException {
            if ("id".equals(property) && Components.getCurrentComponent() != null) {
                if (isStealth(Components.getCurrentComponent())) {
                    super.writeAttribute(name, value, property);
                    return;
                }
            }
            if (stealth) {
                return;
            }
            super.writeAttribute(name, value, property);
        }

        private boolean isStealth(UIComponent component) {
            return ComponentsHelper.getPassThroughAttribute(component, "stealth", Boolean.class) ||
                    ComponentsHelper.getAttribute(component, "stealth", Boolean.class);
        }

        @Override
        public void startCDATA() throws IOException {
            if (stealth) {
                return;
            }
            super.startCDATA();
        }

        @Override
        public ResponseWriter getWrapped() {
            return wrapped;
        }
    }
}
