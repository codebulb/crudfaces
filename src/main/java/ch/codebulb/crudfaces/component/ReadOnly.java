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

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

/**
 * <p>
 * A componenthandler tag which turns all (deep) nested input components into
 * &lt;h:outputText&gt; components, thus rendering the "read only" equivalent.
 * </p>
 * <p>
 * Although vanilla JSF provides the <code>disabled</code> attribute for every
 * component, this attribute still renders the component in question as true
 * input component, but grays it out such that the user cannot manipulate it.
 * However, this does not cover the typical use-case of providing both a "write"
 * and a "read" mode for a component or even an entire view as this view is
 * expected to be presented as a true plain HTML output text. This is what is
 * achieved by using the
 * <code>&lt;cf:readOnly&gt;</code> tag.</p>
 * <p>
 * This component is realized as a simple JSF component, but with the special
 * <code>ReadOnly</code> componenthandler attached which executes the actual
 * transformation. Because the <code>id</code> attribute is kept if explicitly
 * set for the original input component, it can still be targetted by e.g. an
 * <code>&lt;outputLabel&gt;</code>, a
 * <code>&lt;message&gt;</code>, or a <code>&lt;p:focus&gt;</code> component,
 * hence preventing "target not found" errors.</p>
 * <h2>Usage</h2>
 * <p>
 * Using the tag is somewhat similar to OmniFaces'
 * <code>&lt;o:massAttribute&gt;</code> tag: Just surround the component(s) in
 * question with the tag. Of course, you can switch between input and output
 * rendering conditionally using the component's <code>disabled</code>
 * attribute.</p>
 *
                    <h2>Known restrictions</h2>
 * <ul>
 * <li>Only simple text input PrimeFaces components are supported.</li>
 * </ul>
 * <p>
 * These restrictions are most likely to be addressed and resolved in future
 * CrudFaces versions.</p>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
@FacesComponent(value=ReadOnly.COMPONENT_TYPE)
public class ReadOnly extends UIComponentBase {

    public static final String COMPONENT_FAMILY = "ch.codebulb.crudfaces.component";
    public static final String COMPONENT_TYPE = "ch.codebulb.crudfaces.component.ReadOnly";

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}
