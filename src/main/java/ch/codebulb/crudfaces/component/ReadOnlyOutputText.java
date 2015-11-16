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
package ch.codebulb.crudfaces.component;

import javax.faces.component.html.HtmlOutputText;

import ch.codebulb.crudfaces.componenthandler.ReadOnly;

/**
 * A helper component which is used by {@link ReadOnly} to replace an original read/write input component by its read-only equivalent.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class ReadOnlyOutputText extends HtmlOutputText {
    
}
