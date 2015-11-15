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
package ch.codebulb.crudfaces.managedbeans;

import ch.codebulb.crudfaces.util.FacesHelper;

/**
 * An {@link I18N} variant which does <i>not</i> capitalize the main message.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class I18NNoCaps extends I18N {    
    @Override
    protected String i18n(String key, Object... params) {
        return FacesHelper.i18nNoCaps((String)key, params);
    }
}