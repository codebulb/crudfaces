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
package ch.codebulb.crudfaces.renderkit;

import ch.codebulb.crudfaces.component.IncludeMainJs;
import ch.codebulb.crudfaces.util.StringsHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;
import org.omnifaces.util.Faces;
import org.primefaces.component.ajaxstatus.AjaxStatus;

/**
 * <p>
 * A JSF RenderKit which implicitly attaches a client-side JavaScript-based
 * solution to change the class attributes of HTML tags in every view; most
 * notably, it comes with out-of-the-box support to change "PrimeFaces style"
 * class attributes into "Bootstrap style" class attributes.
 * </p>
 * <p>
 * As the Bootstrap CSS framework is becoming a de-facto standard for modern web
 * design, it became desirable to use this framework for component styling in
 * JSF as well. Especially mixing it with the most popular PrimeFaces component
 * framework however turned out to be extremely intricate as PrimeFaces comes
 * with its own CSS styling which is implicitly applied and conflicts with
 * Bootstrap's styles.</p>
 * <p>
 * Although PrimeFaces comes with a built-in Bootstrap-like theme, it's not
 * fully Bootstrap-compatible and offers limited / outdated functionality only.
 * As another option, the dedicated
 * <a href="http://bootsfaces.net/">BootsFaces library</a>
 * offers wrapper components for PrimeFaces components, enhanced with Bootstrap
 * styling. However, using this library would introduce dependency on this
 * not-yet-completed wrapper library, building in another layer of abstraction
 * and indirection above PrimeFaces. This is not desirable.</p>
 * <p>
 * CrudFaces features a client side JavaScript (jQuery) function which changes
 * PrimeFaces components on-the-fly to become compatible with Bootstrap. In its
 * default configuration, the StyleClassChangeRenderKit will remove conflicting
 * PrimeFaces classes (<code>ui-...</code>) and apply Bootstrap's classes where
 * appropriate (e.g. <code>btn</code> for <code>&lt;commandLink&gt;</code>).</p>
 * <p>
 * Originally intended to be implemented as a true server-side JSF render kit,
 * the functionality is now realized in a simple client-side JavaScript
 * function. It is however still triggered by activating the render kit as the
 * render kit's sole remaining job is to implicitly activate the JavaScript
 * function on every view both by registering it in an OmniFaces
 * <code>&lt;o:onloadScript&gt;</code> component (to trigger it on every page
 * load / AJAX call) and as a global <code>document.onclick()</code> function
 * (to trigger it on any button click). This addresses the following issues a
 * server-side implementation wouldn't be able to address:</p>
 * <ul>
 * <li>Performance impact is moved to the client and potentially less severe
 * through the use of optimized jQuery DOM manipulation functions.</li>
 * <li>It can be retriggered whenever a PrimeFaces function manipulates the DOM
 * again, which would re-introduce unwanted PrimeFaces styling classes
 * again.</li>
 * <li>Because it is built on top of vanilla jQuery functions, it's really easy
 * to maintain / manipulate the "style class change" rule set.</li>
 * </ul>
 * <p>
 * By using a render kit as the trigger, enabling the functionality globally is
 * still as simple as registering the render kit in web.xml.</p>
 *
                    <h2>Usage</h2>
 * <p>
 * Activate the render kit in your web.xml:</p>
 * <pre class="brush:xml">
 * &lt;factory&gt;
 * &lt;render-kit-factory&gt;ch.codebulb.crudfaces.renderkit.StyleClassChangeRenderKitFactory&lt;/render-kit-factory&gt;
 * &lt;/factory&gt;
 * </pre>
 *
 * <h2>Known restrictions</h2>
 * <ul>
 * <li>Only basic PrimeFaces components are supported.</li>
 * </ul>
 * <ul>
 * <li>No support for customization.</li>
 * </ul>
 * <ul>
 * <li>DOM manipulations which re-introduce PrimeFaces components but which
 * aren't triggered by an onclick() event cannot be caught; however, there is no
 * known case where this is a problem.</li>
 * </ul>
 * <p>
 * These restrictions are most likely to be addressed and resolved in future
 * CrudFaces versions.</p>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class StyleClassChangeRenderKit extends RenderKitWrapper {
    private RenderKit wrapped;

    public StyleClassChangeRenderKit(RenderKit wrapped) {
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

        public InnerResponseWriter(ResponseWriter wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public ResponseWriter cloneWithWriter(Writer writer) {
            return new InnerResponseWriter(super.cloneWithWriter(writer));
        }
        
        private List<String> getStyleClasses(String styleClassAttribute) {
            return new ArrayList<>(Arrays.asList(styleClassAttribute.split(" ")));
        }
        
        private String getStyleClassAttribute(List<String> styleClasses) {
            return StringsHelper.join(styleClasses, " ");
        }

        @Override
        public void startDocument() throws IOException {
            super.startDocument();
            // Don't use default constructor, because http://stackoverflow.com/a/7823259/1399395
            IncludeMainJs script = 
                    (IncludeMainJs) Faces.getContext().getApplication().createComponent(IncludeMainJs.COMPONENT_TYPE);
            script.encodeAll(Faces.getContext());
        }

        @Override
        public void endElement(String name) throws IOException {
            if ("body".equals(name)) {
                AjaxStatus status = (AjaxStatus) Faces.getContext().getApplication().createComponent(AjaxStatus.COMPONENT_TYPE);
                status.setOncomplete("CrudFaces.styleClassChanges.changePrimeFacesToBootstrap();");
                status.encodeAll(Faces.getContext());
                
                ResponseWriter writer = Faces.getContext().getResponseWriter();
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", "type");
                writer.writeText("CrudFaces.styleClassChanges.changePrimeFacesToBootstrap();CrudFaces.styleClassChanges.initChangePrimeFacesToBootstrap()", null);
                writer.endElement("script");
            }
            super.endElement(name);
        }

        @Override
        public ResponseWriter getWrapped() {
            return wrapped;
        }
    }
}
