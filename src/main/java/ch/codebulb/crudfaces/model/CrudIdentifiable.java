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
package ch.codebulb.crudfaces.model;

import java.io.Serializable;

import ch.codebulb.crudfaces.controller.CrudController;
import ch.codebulb.crudfaces.service.CrudService;

/**
 * A minimal contract any entity type recognized by {@link CrudService} /
 * {@link CrudController} must fulfill.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public interface CrudIdentifiable extends Serializable {
    Long getId();
    void setId(Long id);
}
