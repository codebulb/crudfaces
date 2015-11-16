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
package ch.codebulb.crudfaces.controller;

import ch.codebulb.crudfaces.model.CrudEntity;
import ch.codebulb.crudfaces.model.CrudIdentifiable;
import ch.codebulb.crudfaces.service.CrudService;

/**
 * <p>
 * An abstract generic base implementation of a CRUD JSF backing bean
 * "controller" which implements a fully REST-compliant JSF controller with
 * out-of-the-box support for master / detail pages and PrimeFaces dataTable
 * components.
 * </p>
 * <p>
 * In order to create a CRUD controller for an entity type, make sure the entity
 * implements {@link CrudIdentifiable} (or inherits from
 * {@link CrudEntity}), implement <code>CrudController</code> for the
 * entity and register it as a CDI or JSF bean in the container (depending on
 * beans.xml <code>bean-discovery-mode</code>, explicit registration may not be
 * necessary), and in the JSF EL (e.g. though <code>@Named</code>).</p>
 * <p>
 * As an example, a controller backing bean implementation for a
 * <code>Customer</code> entity can extend <code>CrudController</code> like
 * this:</p>
 * <pre class="brush:java">
&#064;ViewScoped
&#064;Named
public class CustomerController extends CrudTableController&lt;Customer&gt; {
    &#064;Inject
    private CustomerService service;
    
    &#064;Override
    protected CrudService&lt;Customer&gt; getService() {
        return service;
    }
}
                </pre>
 * <ul>
 * <li>Within the <code>getService()</code> method, return the concrete
 * {@link CrudService} for the entity type in question which you should
 * dependency-inject into the controller.</li>
 * </ul>
 * <p>
 * Yes, you really only need to implement this single method, and you get best
 * practices CRUD operations for a RESTful PrimeFaces-based JSF application
 * out-of-the-box.
 * </p>
 * <p>
 * <code>CrudController</code> supports three ways of
 * building a view:</p>
 * <ul>
 * <li>a RESTful, stateless separate
 * master / details view</li>
 * <li>a stateless combined master
 * / detail view</li>
 * <li>a stateful combined master /
 * detail view</li>
 * </ul>
 * <h2>Known restrictions</h2>
 * <ul>
 * <li>Only basic support for PrimeFaces features. No support for "inline edit"
 * yet.</li>
 * </ul>
 * <p>
 * These restrictions are most likely to be addressed and resolved in future
 * CrudFaces versions.</p>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public abstract class CrudController<T extends CrudIdentifiable> extends CrudTableController<T> {
    
}
