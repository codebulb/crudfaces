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
package ch.codebulb.crudfaces.validation.constraints;

import ch.codebulb.crudfaces.util.StringsHelper;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Bean Validation constraint implementation for {@link MaxSize}.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.2
 */
public class MaxSizeValidator implements ConstraintValidator<MaxSize, String> {
    private int value;

    @Override
    public void initialize(MaxSize annotation) {
        value = annotation.value();
    }

    @Override
    public boolean isValid(String candidate, ConstraintValidatorContext context) {
        if (StringsHelper.isEmpty(candidate)) {
            return true;
        }
        if (candidate.length() > value) {
            return false;
        }
        
        return true;
    }
    
}
