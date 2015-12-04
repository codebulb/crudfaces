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
package ch.codebulb.crudfaces.util;

import java.util.Locale;

/**
 * A helper bean providing utility methods for dealing with JSF components.<p/>
 * 
 * This class is designed to be used as  managed bean by EL lookup in a JSF Facelets page. It can be looked up like this:
 * <code>#{componentsHelper}</code>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.2
 */
public class ComponentsHelperBean {
    
    /**
     * A helper method to be used as a backing bean action for a {@code <p:dataTable>} {@code <p:column>} {@code filterFunction}
     * when {@code <pe:triStateCheckbox>} is used as the filter component.<p/>
     * 
     * Use it like this:<p/>
<pre class="brush:xml">
&lt;p:column headerText=&quot;#{I18N['label']}&quot; sortBy=&quot;#{item.attribute}&quot; filterBy=&quot;#{item.attribute}&quot;
          filterFunction=&quot;#{componentsHelper.filterBoolean}&quot; style=&quot;width: 180px;&quot;&gt;
    &lt;f:facet name=&quot;filter&quot;&gt;
        &lt;pe:triStateCheckbox onchange=&quot;PF('tableWidget').filter()&quot; value=&quot;2&quot;/&gt;
    &lt;/f:facet&gt;
    &lt;h:outputText value=&quot;#{i18n[item.attribute]}&quot;/&gt;
&lt;/p:column&gt;
</pre>
     *
     * @param value the value which is tested for filtering
     * @param filter the filter value, in case of {@code pe:triStateCheckbox} either
     * <code>0</code> (unchecked), <code>1</code> (checked), or <code>2</code> (neither checked nor unchecked)
     * @param locale the locale
     * @return <code>true</code>, if the row with the value provided should be shown
     */
    public boolean filterBoolean(Object value, Object filter, Locale locale) {
        Boolean actual = (Boolean) value;
        
        if (actual == null) {
            return true;
        }
        else if (actual == true) {
            if ("1".equals(filter) || "2".equals(filter)) {
                return true;
            }
        }
        else if (actual == false) {
            if ("0".equals(filter) || "2".equals(filter)) {
                return true;
            }
        }
        
        return false;
    }
}
