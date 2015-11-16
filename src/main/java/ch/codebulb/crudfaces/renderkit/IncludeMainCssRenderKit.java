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

import ch.codebulb.crudfaces.component.IncludeMainCss;
import java.io.IOException;
import java.io.Writer;

import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;
import org.omnifaces.util.Faces;


/**
 * A special JSF render kit to implicitly include the {@link IncludeMainCss} component in each view.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class IncludeMainCssRenderKit extends RenderKitWrapper {
    private RenderKit wrapped;

    public IncludeMainCssRenderKit(RenderKit wrapped) {
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

        @Override
        public void startDocument() throws IOException {
            super.startDocument();
            // Don't use default constructor, because http://stackoverflow.com/a/7823259/1399395
            IncludeMainCss stylesheet = 
                    (IncludeMainCss) Faces.getContext().getApplication().createComponent(IncludeMainCss.COMPONENT_TYPE);
            stylesheet.encodeAll(Faces.getContext());
        }
        
        @Override
        public ResponseWriter getWrapped() {
            return wrapped;
        }
    }
}
