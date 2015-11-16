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
package ch.codebulb.crudfaces.validator;

import ch.codebulb.crudfaces.util.FacesHelper;
import static javax.validation.Validation.byDefaultProvider;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.validation.MessageInterpolator;
import org.omnifaces.util.Components;
import org.omnifaces.util.Faces;
import static org.omnifaces.util.FacesLocal.getLocale;


/**
 * A Java Bean Validation {@link MessageInterpolator} implementation which supports message lookups
 * in parent <code>&lt;message-bundle&gt;</code> as well as in the project's <code>&lt;resource-bundle&gt;</code>.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class ParentProjectAwareMessageInterpolator implements MessageInterpolator {

    private final MessageInterpolator wrapped;

    public ParentProjectAwareMessageInterpolator() {
            wrapped = byDefaultProvider().configure().getDefaultMessageInterpolator();
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
            return wrapped.interpolate(messageTemplate, context);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {  
        if (messageTemplate.startsWith("{") && messageTemplate.endsWith("}")) {
            String template = messageTemplate.substring(1, messageTemplate.length()-1);

            ResourceBundle messageBundle = Faces.getMessageBundle();
            try {
                return (String) messageBundle.getObject(template);
            }
            catch (MissingResourceException ex) {
                String message = FacesHelper.i18nOrNull(template, context.getValidatedValue(), Components.getLabel(Components.getCurrentComponent()));
                if (message != null) {
                    return message;
                }
            }
        }
        return wrapped.interpolate(messageTemplate, context, locale);
    }

}