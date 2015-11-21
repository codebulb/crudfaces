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

import ch.codebulb.crudfaces.renderer.FormLayoutModel.Group;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.2
 */
public class FormLayoutModelTest {
    private static BootstrapFormLayoutProvider FORM_LAYOUT_PROVIDER = new BootstrapFormLayoutProvider();
    
    private FormLayoutModel model;
    
    @Before
    public void init() {
        model = new FormLayoutModel(2, new int[]{4, 6, 2}, FORM_LAYOUT_PROVIDER.getResolution());
    }
    
    @Test
    public void testOneGroupNoColspan() {
        // group 1
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        
        assertEquals(1, model.getRows().size());
        assertRatiosEqual(model.getRows().get(0).getGroups(), 1);
        assertRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 6, 2);
        assertOriginalRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 6, 2);
    }
    
    @Test
    public void testTwoGroupsNoColspan() {
        // group 1
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        // group 2
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        
        assertEquals(1, model.getRows().size());
        assertRatiosEqual(model.getRows().get(0).getGroups(), 1, 1);
        assertRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 6, 2);
        assertRatiosEqual(model.getRows().get(0).getGroups().get(1), 4, 6, 2);
        assertOriginalRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 6, 2);
        assertOriginalRatiosEqual(model.getRows().get(0).getGroups().get(1), 4, 6, 2);
    }
    
    @Test
    public void testTwoRowsTwoGroupsNoColspan() {
        // ROW 1
        // group 1
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        // group 2
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        
        // ROW 2
        // group 1
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        // group 2
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        
        assertEquals(2, model.getRows().size());
        assertRatiosEqual(model.getRows().get(0).getGroups(), 1, 1);
        assertRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 6, 2);
        assertRatiosEqual(model.getRows().get(0).getGroups().get(1), 4, 6, 2);
        assertOriginalRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 6, 2);
        assertOriginalRatiosEqual(model.getRows().get(0).getGroups().get(1), 4, 6, 2);
        assertRatiosEqual(model.getRows().get(1).getGroups(), 1, 1);
        assertRatiosEqual(model.getRows().get(1).getGroups().get(0), 4, 6, 2);
        assertRatiosEqual(model.getRows().get(1).getGroups().get(1), 4, 6, 2);
        assertOriginalRatiosEqual(model.getRows().get(1).getGroups().get(0), 4, 6, 2);
        assertOriginalRatiosEqual(model.getRows().get(1).getGroups().get(1), 4, 6, 2);
    }
    
    @Test
    public void testOneGroupInnerColspan() {
        // group 1
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(2));
        
        assertEquals(1, model.getRows().size());
        assertRatiosEqual(model.getRows().get(0).getGroups(), 1);
        assertRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 8);
        assertOriginalRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 8);
    }
    
    @Test
    public void testTwoGroupsInnerColspan() {
        // group 1
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(2));
        // group 2
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        
        assertEquals(1, model.getRows().size());
        assertRatiosEqual(model.getRows().get(0).getGroups(), 1, 1);
        assertRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 8);
        assertRatiosEqual(model.getRows().get(0).getGroups().get(1), 4, 6, 2);
        assertOriginalRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 8);
        assertOriginalRatiosEqual(model.getRows().get(0).getGroups().get(1), 4, 6, 2);
    }
    
    @Test
    public void testOneGroupOverflowingColspan() {
        // group 1-2
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(4));
        model.add(componentWithColspan(1));
        
        assertEquals(1, model.getRows().size());
        assertRatiosEqual(model.getRows().get(0).getGroups(), 2);
        assertRatiosEqual(model.getRows().get(0).getGroups().get(0), 2, 9, 1);
        assertOriginalRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 6, 2);
    }
    
    @Test
    public void testTwoGroupsOverflowingColspan() {
        // group 1-2
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(4));
        model.add(componentWithColspan(1));
        // group 3
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        model.add(componentWithColspan(1));
        
        assertEquals(2, model.getRows().size());
        assertRatiosEqual(model.getRows().get(0).getGroups(), 2);
        assertRatiosEqual(model.getRows().get(0).getGroups().get(0), 2, 9, 1);
        assertOriginalRatiosEqual(model.getRows().get(0).getGroups().get(0), 4, 6, 2);
        assertRatiosEqual(model.getRows().get(1).getGroups(), 1);
        assertRatiosEqual(model.getRows().get(1).getGroups().get(0), 4, 6, 2);
        assertOriginalRatiosEqual(model.getRows().get(1).getGroups().get(0), 4, 6, 2);
    }
    
    private static void assertRatiosEqual(List<Group> actualGroups, int... expectedRatios) {
        assertEquals(expectedRatios.length, actualGroups.size());
        for (int i = 0; i < expectedRatios.length; i++) {
            assertEquals(expectedRatios[i], actualGroups.get(i).getRatio());
        }
    }
    
    private static void assertRatiosEqual(Group actualGroup, int... expectedRatios) {
        assertEquals(expectedRatios.length, actualGroup.getComps().size());
        int sum = 0;
        for (int i = 0; i < expectedRatios.length; i++) {
            int ratio = (int)actualGroup.getComps().get(i).getRatio();
            assertEquals(expectedRatios[i], ratio);
            sum += ratio;
        }
        // must always add up to resolution
        assertEquals(FORM_LAYOUT_PROVIDER.getResolution(), sum);
    }
    
    private static void assertOriginalRatiosEqual(Group actualGroup, int... expectedRatios) {
        assertEquals(expectedRatios.length, actualGroup.getComps().size());
        int sum = 0;
        for (int i = 0; i < expectedRatios.length; i++) {
            int ratio = (int)actualGroup.getComps().get(i).getOriginalRatio();
            assertEquals(expectedRatios[i], ratio);
            sum += ratio;
        }
        // must always add up to resolution
        assertEquals(FORM_LAYOUT_PROVIDER.getResolution(), sum);
    }
    
    private static UIComponent componentWithColspan(int colspan) {
        UIComponent ret = new HtmlOutputText();
        ret.getPassThroughAttributes().put("colspan", String.valueOf(colspan));
        return ret;
    }
}
