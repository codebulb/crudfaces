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

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.omnifaces.el.functions.Strings;
import org.omnifaces.util.Faces;
import static org.omnifaces.util.FacesLocal.getResourceBundles;

/**
 * A collection of helper utility methods for dealing with common JSF functionality.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class FacesHelper {

    private FacesHelper() {
    }
    
    /**
     * Returns the capitalized I18N String with the key and the parameters provided or <code>null</code> if the key is not found.
     * <p/>
     * This method is designed from being called from Java code.
     */
    public static String i18nOrNull(String key, Object... params) {
        return i18n(true, false, key, params);
    }
    
    /**
     * Returns the I18N String with the key and the parameters provided or <code>null</code> if the key is not found.
     * <p/>
     * This method is designed from being called from Java code.
     */
    public static String i18nOrNullNoCaps(String key, Object... params) {
        return i18n(true, true, key, params);
    }

    /**
     * Returns the capitalized I18N String with the key and the parameters provided or the original key with "not found" markers if the key is not found.
     * <p/>
     * This method is designed from being called from JSF code.
     */
    public static String i18n(String key, Object... params) {
        return i18n(false, false, key, params);
    }
    
    /**
     * Returns the I18N String with the key and the parameters provided or the original key with "not found" markers if the key is not found.
     * <p/>
     * This method is designed from being called from JSF code.
     */
    public static String i18nNoCaps(String key, Object... params) {
        return i18n(false, true, key, params);
    }
    
    private static String i18n(boolean orNull, boolean withoutCapitalizing, String key, Object... params) {
        String bundleString;
        if (orNull) {
            bundleString = getBundleString(key);
        }
        else {
            bundleString = Faces.getBundleString(key);
        }
        if (bundleString == null) {
            return null;
        }
        
        String ret = MessageFormat.format(bundleString, params);
        
        if (withoutCapitalizing) {
            return ret;
        }
        return Strings.capitalize(ret);
    }

    /**
     * @see Faces#getBundleString(String)
     */
    private static String getBundleString(String key) {
        for (ResourceBundle bundle : getResourceBundles(Faces.getContext()).values()) {
            try {
                return bundle.getString(key);
            } catch (MissingResourceException ignore) {
                continue;
            }
        }

        return null;
    }
}
