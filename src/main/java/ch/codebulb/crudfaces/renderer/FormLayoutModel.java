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

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;

import ch.codebulb.crudfaces.component.FormLayout;

/**
 * A model representing the styling applied to a {@link FormLayout}.<p/>
 * 
 * Originally, the {@link FormLayoutRenderer} built and rendered the component on-the-fly.
 * However, it became necessary to build a model beforehand and then walk the model in order to
 * write the actual HTML markup when introducing the possibility to add "colspan" effects to child components 
 * because of how Bootstrap grid layout CSS classes must be applied:
 * <ul>
 * <li>label + associated component must be placed in a "form-group" in order for 
 * validation styling to work properly.</li>
 * <li>Inside a "form-group" container, the total size ratio (the resolution) is always 12.</li>
 * <li>A "form-group" container's child component's ratio get multiplied by the container resolution.</li>
 * <li>When using "colspan", you can either extend a component inside a container or you can even
 * "devour" components from the next container, merging the containers together.</li>
 * </ul>
 * When writing the "form-group" container's markup, we must thus already know the desired total size ratio
 * of the container; however, we can only know this by summing up the colspans of the container's children.
 * Hence, we build a complete model of the component and all of its children first, including the required
 * calculations, and can then just write the markup by using the model's data.<p/>
 * <h2>Usage</h2>
 * <ul>
 * <li>Invoke the constructor: {@link #FormLayoutModel(int, int[], int)}</li>
 * <li>Subsequently {@link #add(UIComponent)} each component</li>
 * <li>{@link #getRows()} in order to {@link Row#getGroups()} in order to {@link Group#getComps()}</li>
 * </ul>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class FormLayoutModel {
    private int groups;
    private int[] groupRatios;
    private int resolution;
    
    private int currentGroupCol;
    private int currentColspan;
    private List<Comp> currentComps = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();

    /**
     * @param groups user-defined groups number
     * @param groupRatios user-defined groups ratios
     * @param resolution resolution provided by {@link BootstrapFormLayoutProvider}
     */
    public FormLayoutModel(int groups, int[] groupRatios, int resolution) {
        this.groups = groups;
        this.groupRatios = groupRatios;
        this.resolution = resolution;
        this.rows.add(new Row());
    }
    
    public void add(UIComponent component) {
        int colspan = FormLayoutRenderer.getColspan(component);
        
        if (rows.get(rows.size()-1).getRatioSum() >= groups) {
            rows.add(new Row());
        }
        
        int originalGroupColRatio = groupRatios[currentGroupCol];
        // with colspan: "devour" neighbor cells
        for (int i = 0; i < colspan; i++) {
            int currentGroupColRatio = groupRatios[currentGroupCol];
            currentColspan += currentGroupColRatio;
            // mark the current position in the groupRatios array
            currentGroupCol = (currentGroupCol + 1) % groupRatios.length;
        }
        currentComps.add(new Comp(component, currentColspan, originalGroupColRatio));
        currentColspan = 0;
        
        // end of the groupRatios array is reached: Close current group
        // (don't do this when jumping over a groupRatios end by devouring components with colspan)
        if (currentGroupCol == 0) {
            rows.get(rows.size()-1).getGroups().add(new Group(currentComps));
            currentComps = new ArrayList<>();
        }
    }

    public List<Row> getRows() {
        return rows;
    }
    
    public class Row {
        private List<Group> groups = new ArrayList<>();
        
        public List<Group> getGroups() {
            return groups;
        }

        public int getRatioSum() {
            int ret = 0;
            for (Group group : groups) {
                ret += group.getRatio();
            }
            return ret;
        }
    }
    
    public class Group {
        private List<Comp> comps;
        private final int sum;

        public Group(List<Comp> comps) {
            this.comps = comps;
            for (Comp comp : comps) {
                comp.group = this;
            }
            this.sum = calculateSum();
            
            // if group length == row length, always use the actual ratio
            if (getRatio() == 1) {
                for (Comp comp : comps) {
                    comp.setOriginalRatio(comp.getRatio());
                }
            }
        }
        
        // for performance reasons, calculate this once and store it in a final variable
        private int calculateSum() {
            int ret = 0;
            for (Comp comp : comps) {
                ret += comp.colspan;
            }
            return ret;
        }
        
        /**
         * Gets the ratio of the group compared to the {@link Row}: How many groups fit in a row?
         */
        public int getRatio() {
            return getSum() / resolution;
        }

        public List<Comp> getComps() {
            return comps;
        }

        /**
         * Gets the sum of all children's ratios.
         */
        public int getSum() {
            return sum;
        }
    }
    
    public class Comp {
        public final UIComponent component;
        private final int colspan;
        private int originalRatio;
        private Group group;

        /**
         * @param component the component
         * @param colspanRatio the ratio with colspan applied
         * @param originalRatio the original ratio without colspan applied.
         * for small responsive layouts, the original ratio may be used instead
         */
        public Comp(UIComponent component, int colspanRatio, int originalRatio) {
            this.component = component;
            this.colspan = colspanRatio;
            this.originalRatio = originalRatio;
        }
        
        /**
         * Gets the ratio of this component within the {@link Group}.
         * Within the group, the total of all children ratios must add up to 
         * {@link FormLayoutModel#resolution}.
         *
         * @return the ratio
         */
        public int getRatio() {
            return colspan / group.getRatio();
        }

        public int getOriginalRatio() {
            return originalRatio;
        }

        public void setOriginalRatio(int originalRatio) {
            this.originalRatio = originalRatio;
        }
    }
}
