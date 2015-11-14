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

import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

/**
 * The JSF render kit factory for {@link StyleClassChangeRenderKit}.
 * 
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class StyleClassChangeRenderKitFactory extends RenderKitFactory {
	private RenderKitFactory wrapped;
        
	public StyleClassChangeRenderKitFactory(RenderKitFactory wrapped) {
		this.wrapped = wrapped;
	}
        
	@Override
	public void addRenderKit(String renderKitId, RenderKit renderKit) {
		wrapped.addRenderKit(renderKitId, renderKit);
	}
        
	@Override
	public RenderKit getRenderKit(FacesContext context, String renderKitId) {
		RenderKit renderKit = wrapped.getRenderKit(context, renderKitId);
		return (HTML_BASIC_RENDER_KIT.equals(renderKitId)) ? new StyleClassChangeRenderKit(renderKit) : renderKit;
	}

	@Override
	public Iterator<String> getRenderKitIds() {
		return wrapped.getRenderKitIds();
	}

}