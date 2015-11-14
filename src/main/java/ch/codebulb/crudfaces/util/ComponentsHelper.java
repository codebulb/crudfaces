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
package ch.codebulb.crudfaces.util;

import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import org.omnifaces.util.Components;
import org.omnifaces.util.Faces;

/**
 * A collection of helper utility methods for dealing with JSF components.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class ComponentsHelper {

    private ComponentsHelper() {
    }

    /**
     * Gets the requested HTML5 pass-through attribute and casts it, if necessary.
     *
     * @param <T> the expected type of the pass-through attribute
     * @param component the component
     * @param attribute the attribute name
     * @param type the expected type of the pass-through attribute
     * @return the pass through attribute
     */
    public static <T> T getPassThroughAttribute(UIComponent component, String attribute, Class<T> type) {
        Map<String, Object> passThroughAttributes = component.getPassThroughAttributes();
        Object value = passThroughAttributes.get(attribute);
        
        if (value == null) {
            if (type == Boolean.class) {
                return (T) Boolean.FALSE;
            }
            if (type == Integer.class) {
                return (T) Integer.valueOf(0);
            }
            return null;
        }
        if (value instanceof ValueExpression) {
            ValueExpression expr = (ValueExpression) value;
            return (T) expr.getValue(Faces.getELContext());
        }
        if (value instanceof String) {
            if (type == Boolean.class) {
                return (T) Boolean.valueOf((String) value);
            }
            if (type == Integer.class) {
                return (T) Integer.valueOf((String) value);
            }
            if (type == String.class) {
                return (T) value;
            }
        }
        throw new IllegalStateException("Cannot convert value of type " + value.getClass() + " into type " + type + " for value: " + value);
    }
    
    /**
     * Gets the requested attribute and casts it, if necessary.
     *
     * @param <T> the expected type of the attribute
     * @param component the component
     * @param attribute the attribute
     * @param type the expected type of the attribute
     * @return the attribute
     */
    public static <T> T getAttribute(UIComponent component, String attribute, Class<T> type) {
        T value = Components.getAttribute(component, attribute);
        if (value == null) {
            if (type == Boolean.class) {
                return (T) Boolean.FALSE;
            }
            else {
                return null;
            }
        }
        if (value instanceof String) {
            if (type == Boolean.class) {
                return (T) Boolean.valueOf((String) value);
            }
            if (type == String.class) {
                return (T) value;
            }
            throw new IllegalStateException("Cannot convert value of type " + value.getClass() + " into type " + type + " for value: " + value);
        }
        
        // else: it's already converted
        return value;
    }
}
