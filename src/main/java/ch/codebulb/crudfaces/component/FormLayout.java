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

import ch.codebulb.crudfaces.renderer.FormLayoutRenderer;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIPanel;
import org.omnifaces.util.State;

/**
 * <p>
 * A container component to apply implicitly "form-like" (e.g. label / input /
 * message) grid styling of its child components using a (responsive) CSS layout
 * with out-of-the-box support for Bootstrap responsive form grid CSS classes.
 * </p>
 * <p>
 * Modern interface stying is based on CSS, not HTML tables, because it embraces
 * separation of design and functionality, and it enables sophisticated
 * techniques such as responsive, mobile-friendly layouts. </p>
 * <p>
 * However, none of today's available JSF (component) frameworks deliver an
 * easy-to-use abstraction for styling components using these techniques. The
 * built-in containers for JSF and PrimeFaces, e.g.
 * <code>&lt;panelGrid&gt;</code>, are based on HTML tables. PrimeFaces provides
 * <a href="http://www.primefaces.org/showcase/ui/panel/grid.xhtml">
 * an additional set of CSS grid classes</a>, but they don't seem to actually
 * work responsively at all; also, they need to be applied explicitly.
 * Since PrimeFaces 5.3, they also provide a <a
 * href="http://www.primefaces.org/showcase/ui/misc/responsive.xhtml">responsive
 * design container component</a>. When directly compared with CrudFaces'
 * <code>&lt;cf:formLayout&gt;</code> it does however lack implicit styling,
 * true resize-aware responsibility and important features as provided here e.g.
 * by the <code>a:colspan</code> and the <code>checkboxLabelsInline</code>
 * attributes.
 * </p>
 * <p>
 * This situation is especially annoying as most JSF web applications typically make heavy
 * use of quite simple grid-like form layouts, but using
 * <code>&lt;panelGrid&gt;</code> or applying HTML tables / CSS explicitly is
 * cumbersome and diminishes XHTML readability, especially when trying to align
 * components in a certain way or when using "colspan" attributes. What we
 * really want is a panelGrid, but which works with CSS classes and is built
 * with responsiveness out-of-the box. This is what
 * <code>&lt;cf:formLayout&gt;</code> provides.</p>
 * <p>
 * A <a href="http://www.bootsfaces.net/layout/panelgrids.jsf">similar component
 * is also included in the BootsFaces library</a>, but it seems far more basic
 * and doesn't come as real-world production-ready as
 *  <code>&lt;cf:formLayout&gt;</code>.</p>
 * <p>
 * <code>&lt;cf:formLayout&gt;</code> implicitly applies the required CSS
 * classes to its child components to build a simple form-like grid layout for a
 * flexible amount of columns (default 2). It is preconfigured to use
 * Bootstrap's form-horizontal styling CSS classes with its base 12 grid
 * system.</p>
 * <p>
 * <code>&lt;cf:formLayout&gt;</code> also includes the following features:</p>
 * <ul>
 * <li><code>&lt;message&gt;</code>s are combined with their respective input
 * component into one cell. This is typically desired because otherwise,
 * non-present messages (which still render an empty cell) would create huge
 * gaps in the UI.</li>
 * <li>Buttons are by default put in a "button bar" at the bottom of the layout.
 * If you want a particular button to stay inline, add the
 * <code>a:inline="true"</code> HTML5 pass-through attribute to it. If on the
 * other hand you want a non-button component to be placed in the "button bar",
 * use the <code>a:buttonBar="true"</code> pass-through attribute.</li>
 * <li>Set the <code>a:colspan</code> HTML5 pass-through attribute on any component to achieve a 
 * "colspan" effect: The component will be placed in a cell which spans the 
 * number of columns provided whilst keeping the layout responsive.</li>
 * </ul>
 * <p>
 * Note that <code>&lt;cf:formLayout&gt;</code> does not implement an actual
 * HTML <code>&lt;form&gt;</code>, it's really just for layouting purposes which
 * leaves you the free choice of how to integrate it in an actual HTML form.</p>
 * 
 * <h2>Usage</h2>
 * <p>
 * Add the CrudFaces Facelets library declaration
 * <code>xmlns:cf=&quot;http://crudfaces.codebulb.ch&quot;</code> and use the
 * <code>&lt;cf:formLayout&gt;</code> component as shown in the demo showcase.</p>
 * 
 * <h2>Global overrides</h2>
 * <p>
 * The <code>FormLayout</code> class provides the following hooks to override
 * its default configuration globally:</p>
 * <ul>
 * <li><code>#CHECKBOX_LABELS_INLINE</code>: The default value for the
 * <code>checkboxLabelsInline</code> attribute. <i>Defaults to
 * <code>true</code>.</i></li>
 * </ul>
 * <p>
 * The <code>FormLayoutRenderer</code> class provides the following hooks to
 * override its default configuration globally:</p>
 * <ul>
 * <li><code>#MESSAGE_COMPONENT_SUBCELL_STYLE_CLASS</code>: The style class
 * applied to the <code>&lt;div&gt;</code> container of an invalid component
 * with a <code>&lt;message&gt;</code> attached. <i>Defaults to
 * <code>BootstrapFormLayoutProvider#getMessageComponentSubCellStyleClass()</code>.</i></li>
 * <li><code>#MESSAGE_SUBCELL_STYLE_CLASS</code>: The style class applied to the
 * <code>&lt;div&gt;</code> container of a <code>&lt;message&gt;</code>.
 * <i>Defaults to
 * <code>BootstrapFormLayoutProvider#getMessageSubCellStyleClass()</code>.</i></li>
 * </ul>
 * <p>
 * You can use e.g. an eagerly loaded application scoped bean to apply a global
 * configuration override:</p>
<pre class="brush:java">
&#064;Eager
&#064;ApplicationScoped
public class GlobalApplicationConfig {
    &#064;PostConstruct
    public void overrideCrudFacesConfig() {
        FormLayout.CHECKBOX_LABELS_INLINE = false;
        FormLayoutRenderer.MESSAGE_COMPONENT_SUBCELL_STYLE_CLASS = &quot;col-lg-12&quot;;
        FormLayoutRenderer.MESSAGE_SUBCELL_STYLE_CLASS = &quot;col-lg-12&quot;;
    }
}
</pre>
 *
                <h2>Known restrictions</h2>
 * <ul>
 * <li>Only basic PrimeFaces components are supported.</li>
 * <li>Nesting <code>&lt;cf:formLayout&gt;</code>s is not supported.</li>
 * <li>Support for custom styling is very limited; explicitly override the
 * default styles is not supported.</li>
 * <li>A "component group" must always be completed; incomplete groups are not
 * rendered. For instance, when working with a <code>&lt;cf:formLayout
 * groupRatios="4 6 2"&gt;</code>, which has 3 components per group (e.g. a
 * label, an input component, and a help button), and you put only 2 components
 * in the last group (e.g. a label and an input component), this group will not
 * be rendered. This applies to <code>a:colspan</code> usage as well.</li>
 * </ul>
 * <p>
 * These restrictions are most likely to be addressed and resolved in future
 * CrudFaces versions.</p>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
@FacesComponent(FormLayout.COMPONENT_TYPE)
public class FormLayout extends UIPanel {
    public static final String COMPONENT_TYPE = "ch.codebulb.crudfaces.component.FormLayout";
    
    public static boolean CHECKBOX_LABELS_INLINE = true;

    private final State state = new State(getStateHelper());

    private enum PropertyKeys {
        // Cannot be uppercased. They have to exactly match the attribute names.
        groups, groupRatios, checkboxLabelsInline, styleClass, style;
    }

    public FormLayout() {
        setRendererType(FormLayoutRenderer.RENDERER_TYPE);
    }

    public int getGroups() {
        return state.get(FormLayout.PropertyKeys.groups, 1);
    }
    
    public void setGroups(int columns) {
        state.put(FormLayout.PropertyKeys.groups, columns);
    }

    public String getGroupRatios() {
        return state.get(FormLayout.PropertyKeys.groupRatios, "4 8");
    }

    public void setGroupRatios(String columnRatios) {
        state.put(FormLayout.PropertyKeys.groupRatios, columnRatios);
    }

    public boolean isCheckboxLabelsInline() {
        return state.get(FormLayout.PropertyKeys.checkboxLabelsInline, CHECKBOX_LABELS_INLINE);
    }

    public void setCheckboxLabelsInline(boolean checkboxLabelsInline) {
        state.put(FormLayout.PropertyKeys.checkboxLabelsInline, checkboxLabelsInline);
    }

    public String getStyleClass() {
        return state.get(FormLayout.PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        state.put(FormLayout.PropertyKeys.styleClass, styleClass);
    }

    public String getStyle() {
        return state.get(FormLayout.PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        state.put(FormLayout.PropertyKeys.style, style);
    }
}
