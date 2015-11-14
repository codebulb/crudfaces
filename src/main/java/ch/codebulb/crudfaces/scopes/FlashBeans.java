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
package ch.codebulb.crudfaces.scopes;

import java.util.HashMap;
import javax.faces.context.Flash;
import org.omnifaces.util.Faces;

/**
 * <p>
 * A wrapper bean to access any (request-scoped) bean as if it were in "flash
 * scope", thus allowing changes to the bean to survive one redirect without
 * touching the session at all.
 * </p>
 * <p>
 * The flash scope, as introduced with JSF 2.0, is a handy tool in any situation
 * where any given backing bean value should survive just one redirect. In this
 * situation, request scope is too short, but any other scope like view scope
 * and session scope is much too long. Through my own research, I have also
 * learnt that
 * <a href="http://www.codebulb.ch/2015/05/java-ee-7-bean-scopes-compared-part-2.html#lessons">
 * especially view scope is actually buggy and can lead to major memory
 * leaks</a>.</p>
 * <p>
 * Although overall useful, one disadvantage of flash scope is that it's not
 * actually implemented as a true "bean scope" (with annotation), but it's
 * simply a Map-like bean exposed to the JSF EL (<code>#{flash}</code>), and
 * read / write access must be explicitly controlled.</p>
 * <p>
 * flashBeans is a wrapper for <code>#{flash}</code>, and it's also exposed via
 * the JSF EL (<code>#{flashBeans}</code>), but it works completely different
 * from vanilla flash: Through flashBeans, you can access any (request-scoped)
 * bean; if it already is in the flash scope, the instance held in the flash
 * scope is returned, otherwise, the bean is looked up by its EL name (i.e. in
 * the bean container), put into the flash scope and returned. That way, through
 * flashBeans, you can transparently access and save beans from / into flash
 * scope.</p>
 * <p>
 * Because flashBean implements the Map interface, you can access any bean
 * simply by its EL name using the dot-notation, e.g.
 * <code>#{flashBeans.personController}</code>. That way, you can locally turn
 * any request scoped bean into a "flash scoped" bean.</p>
 * <p>
 * Because JSF flash scope will not put anything into the HTTP session at all,
 * using this technique will help you prevent session polluting when compared
 * with alternative scopes such as view or session scope.</p>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class FlashBeans extends HashMap<String, Object> {
    private Flash flash;

    @Override
    public Object get(Object key) {
        String keyAsString = (String)key;
        Object instance = flash.get(keyAsString);
        if (instance == null) {
            instance = Faces.evaluateExpressionGet("#{" + keyAsString + "}");
            flash.put(keyAsString, instance);
        }
        return instance;
    }

    public Flash getFlash() {
        return flash;
    }

    public void setFlash(Flash flash) {
        this.flash = flash;
    }
}
