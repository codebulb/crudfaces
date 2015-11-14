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
package ch.codebulb.crudfaces.componenthandler;

import ch.codebulb.crudfaces.component.ReadOnlyOutputText;
import ch.codebulb.crudfaces.util.ComponentsHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import org.omnifaces.util.Faces;

/**
 * The componenthandler for the {@link ch.codebulb.crudfaces.component.ReadOnly} component.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class ReadOnly extends ComponentHandler {
    public ReadOnly(ComponentConfig config) {
        super(config);
    }

    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        super.onComponentCreated(ctx, c, parent);
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        super.apply(ctx, parent);
    }

    @Override
    public void onComponentPopulated(FaceletContext ctx, UIComponent component, UIComponent parent) {
        super.onComponentPopulated(ctx, component, parent);
        
        boolean disabled = false;
        TagAttribute disabledAttribute = getAttribute("disabled");
        if (disabledAttribute != null) {
            disabled = disabledAttribute.getBoolean(Faces.getFaceletContext());
        }
        
        removeReadOnlyComponents(component); // reset
        if (!disabled) {
            addReadOnlyComponents(component);
        }
    }
    
    private static void removeReadOnlyComponents(UIComponent parent) {
        List<ReadOnlyOutputText> remove = new ArrayList<>();
        for (UIComponent child : parent.getChildren()) {
            if (child instanceof ReadOnlyOutputText) {
                remove.add((ReadOnlyOutputText) child);
            }
            else if (child.getChildCount() > 0) {
                removeReadOnlyComponents(child);
            }
        }
        parent.getChildren().removeAll(remove);
    }

     private static void addReadOnlyComponents(UIComponent parent) {
         for (UIComponent child : parent.getChildren()) {
            if (child instanceof UIInput) {
                UIComponent childParent = child.getParent();
                int index = childParent.getChildren().indexOf(child);
                
                ReadOnlyOutputText outputText = new ReadOnlyOutputText();
                String value = ComponentsHelper.getAttribute(child, "value", String.class);
                outputText.setValue(value);
                outputText.setId(child.getId());

                childParent.getChildren().add(index, outputText);
                childParent.getChildren().remove(child);
            }
            else if (child.getChildCount() > 0) {
                addReadOnlyComponents(child);
            }
        }
     }
}
