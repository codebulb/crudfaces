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
package ch.codebulb.crudfaces.component;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

/**
 * A simple helper JSF component whose only job is to include CrudFaces' main JavaScript file for a web page.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
@FacesComponent(IncludeMainJs.COMPONENT_TYPE)
@ResourceDependencies({
	@ResourceDependency(library="javax.faces", name="jsf.js", target="head"),
	@ResourceDependency(library="omnifaces", name="omnifaces.js", target="head"),
    @ResourceDependency(library="crudfaces", name="crudfaces.js", target="head")
})
public class IncludeMainJs extends UIComponentBase {
    public static final String COMPONENT_TYPE = "ch.codebulb.crudfaces.component.IncludeMainJs";
    public static final String COMPONENT_FAMILY = "ch.codebulb.crudfaces.component.IncludeMainJs"; 

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}
