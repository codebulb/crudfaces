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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A collection of helper utility methods for dealing with simple String operations.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class StringsHelper {

    private StringsHelper() {
    }
    
    /**
     * Returns <code>true</code> if the String provided is either <code>null</code> or empty.
     */
    public static boolean isEmpty(String input) {
        return input == null || input.isEmpty();
    }

    /**
     * Joins the String provided with the separator provided.
     */
    // as in http://stackoverflow.com/a/15293227/1399395
    public static String join(List<String> strings, String separator) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            if (i > 0) {
                ret.append(separator);
            }
            ret.append(strings.get(i));
        }
        return ret.toString();
    }
    
    /**
     * Builds a new String {@link Joiner} which can be used to join Strings in an easy-to-read manner.
     */
    public static Joiner join(String separator) {
        return new Joiner(separator);
    }
    
    public static class Joiner {
        private final String separator;
        private final List<String> strings = new ArrayList<>();

        public Joiner(String separator) {
            this.separator = separator;
        }
        
        public Joiner add(String string) {
            if (string == null) {
                return this;
            }
            
            strings.add(string);
            return this;
        }

        @Override
        public String toString() {
            return join(strings, separator);
        }
    }
    
    /**
     * Formats the date provided using the webapplication's defaul date format pattern as defined by the resource-bundle key
     * <code>format.calendar</code>. This is the most DRY-compliant way to reuse the same date format over the entire application.<p/>
     * 
     * You can also use it directly in a Facelets XHTML page like this, using OmniFaces' <code>&lt;o:importFunctions&gt;</code>:<p/>
<pre class="brush:xml">
&lt;o:importFunctions type=&quot;ch.codebulb.crudfaces.util.StringsHelper&quot; var=&quot;strings&quot; /&gt;
...
&lt;h:outputText value=&quot;#{strings:formatDate(myAttribute)}&quot;/&gt;
</pre>
     */
    public static String formatDate(Date input) {
        DateFormat dateFormat = new SimpleDateFormat(FacesHelper.i18nOrNullNoCaps("format.calendar"));
        return dateFormat.format(input);
    }
}
