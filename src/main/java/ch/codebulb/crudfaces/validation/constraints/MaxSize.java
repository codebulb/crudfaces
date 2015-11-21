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

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * <p>
 * A Bean Validation constraint to check for a String's max number of
 * characters.</p>
 * <p>
 * The <code>@Size</code> Bean Validation constraint is built to check for both
 * min and max boundaries, which is also reflected by its default validation
 * message (<i>size must be between {min} and {max}</i>).</p>
 * <p>
 * However, for many application we would like a validation constraint for max
 * boundary only. Using <code>@Size</code> with <code>min = 0</code> would
 * result in an awkward validation message (e.g. <i>size must be between 0 and
 * 100</i>).</p>
 * <p>
 * Therefore, use the <code>@MaxSize</code> validation constraint to check for
 * max boundary only.</p>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.2
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MaxSizeValidator.class)
public @interface MaxSize {
    String message() default "{ch.codebulb.crudfaces.validation.constraints.MaxSize.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    int value();
}
