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
package ch.codebulb.crudfaces.managedbeans;

import ch.codebulb.crudfaces.util.FacesHelper;
import java.util.HashMap;

/**
 * <p>
 * A helper bean for I18N (localization) String lookup from within EL which
 * allows accessing localization keys on any registered resource-bundle, not
 * just a resource-bundle with a given "var" id.
 * </p>
 * <p>
 * Through the <code>#{I18N}</code> EL-enabled bean, you can lookup I18N keys
 * such as you would using the <code>var</code> key of a designated
 * <code>&lt;resource-bundle&gt;</code>. So instead of e.g.
 * <code>#{msg['save']}</code>, use <code>#{I18N['save']}</code>.</p>
 * <p>
 * Using the I18N bean rather than referring to a specific
 * <code>&lt;resource-bundle&gt;</code> by its <code>var</code> offers the
 * following advantages:</p>
 * <ul>
 * <li>It allows to lookup Strings in any registered
 * <code>&lt;resource-bundle&gt;</code>. resource-bundles are consulted in the
 * order they are registered.</li>
 * <li>As a consequence of the above, this also allows you to use the built-in
 * default message Strings shipped with CrudFaces alongside your own message
 * Strings using <code>#{I18N}</code> as a unified access interface with the
 * ability to override CrudFaces default message Strings with your own message
 * Strings. CrudFaces ships with default messages for many general-purpose
 * message keys, e.g. "save" or "cancel". See
 * <h:link outcome="/global/message-bundle.xhtml">message-bundle</h:link> for
 * details.</li>
 * <li>The I18N EL bean offers a simple interface to provide up to 5 message
 * String parameters. It is far more concise than using e.g. OmniFaces'
 * <code>&lt;of:format...&gt;</code> EL functions with a message lookup.</li>
 * <li><code>#{I18N}</code> auto-capitalizes the main message, but not message
 * parameters. This promotes re-using message Strings in labels / buttons
 * (capitalized) and as parameters (non capitalized)</li>
 * <li>In case you don't want auto-capitalizing: There is the
 * <code>#{i18n}</code> bean (mind the caps!) which works exactly as
 * <code>#{I18N}</code>, but without auto-capitalization.</li>
 * </ul>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class I18N extends HashMap<String, String> {    
    
    /**
     * Returns the I18N String with the key provided.
     *
     * @see java.util.HashMap#get(java.lang.Object)
     */
    @Override
    public String get(Object key) {
        return i18n((String)key);
    }
    
    /**
     * Returns the I18N String with the key and the one parameter provided.
     *
     * @return the string
     */
    public String w(Object key, Object param0) {
        return with(key, param0);
    }
    
    /**
     * Returns the I18N String with the key and the 2 parameters provided.
     *
     * @return the string
     */
    public String w2(Object key, Object param0, Object param1) {
        return with(key, param0, param1);
    }
    
    /**
     * Returns the I18N String with the key and the 3 parameters provided.
     *
     * @return the string
     */
    public String w3(Object key, Object param0, Object param1, Object param2) {
        return with(key, param0, param1, param2);
    }
    
    /**
     * Returns the I18N String with the key and the 4 parameters provided.
     *
     * @return the string
     */
    public String w4(Object key, Object param0, Object param1, Object param2, Object param3) {
        return with(key, param0, param1, param2, param3);
    }
    
    /**
     * Returns the I18N String with the key and the 5 parameters provided.
     *
     * @return the string
     */
    public String w5(Object key, Object param0, Object param1, Object param2, Object param3, Object param4) {
        return with(key, param0, param1, param2, param3, param4);
    }
    
    private String with(Object key, Object... params) {
        return FacesHelper.i18n((String)key, params);
    }

    /**
     * Returns the I18N String with the key and the one parameter provided
     * whereby the parameter is localized as well.
     *
     * @return the string
     */
    public String wi(Object key, Object param0) {
        return withI18N(key, param0);
    }
    
    /**
     * Returns the I18N String with the key and the 2 parameters provided
     * whereby the parameters are localized as well.
     *
     * @return the string
     */
    public String wi2(Object key, Object param0, Object param1) {
        return withI18N(key, param0, param1);
    }
    
    /**
     * Returns the I18N String with the key and the 3 parameters provided
     * whereby the parameters are localized as well.
     *
     * @return the string
     */
    public String wi3(Object key, Object param0, Object param1, Object param2) {
        return withI18N(key, param0, param1, param2);
    }
    
    /**
     * Returns the I18N String with the key and the 4 parameters provided
     * whereby the parameters are localized as well.
     *
     * @return the string
     */
    public String wi4(Object key, Object param0, Object param1, Object param2, Object param3) {
        return withI18N(key, param0, param1, param2, param3);
    }
    
    /**
     * Returns the I18N String with the key and the 5 parameters provided
     * whereby the parameters are localized as well.
     *
     * @return the string
     */
    public String wi5(Object key, Object param0, Object param1, Object param2, Object param3, Object param4) {
        return withI18N(key, param0, param1, param2, param3, param4);
    }
    
    
    private String withI18N(Object key, Object... params) {
        Object[] paramStrings = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            paramStrings[i] = FacesHelper.i18nNoCaps(params[i].toString());
        }
        return i18n((String)key, paramStrings);
    }
    
    protected String i18n(String key, Object... params) {
        return FacesHelper.i18n((String)key, params);
    }
}
