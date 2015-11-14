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
package ch.codebulb.crudfaces.controller;

import ch.codebulb.crudfaces.model.CrudIdentifiable;

/**
 * <p>
 * An abstract generic base implementation of a CRUD JSF backing bean
 * "controller" which implements a fully REST-compliant JSF controller with
 * out-of-the-box support for master / detail pages and PrimeFaces dataTable
 * components.
 * </p>
 * <p>
 * In order to create a CRUD controller for an entity type, make sure the entity
 * implements <code>CrudIdentifiable</code> (or inherits from
 * <code>CrudEntity</code>), implement <code>CrudController</code> for the
 * entity and register it as a CDI or JSF bean in the container (depending on
 * beans.xml <code>bean-discovery-mode</code>, explicit registration may not be
 * necessary), and in the JSF EL (e.g. though <code>@Named</code>).</p>
 * <p>
 * For this demo application, the controller backing bean implementation for the
 * <code>Customer</code> entity extends <code>CrudController</code> like
 * this:</p>
 * <pre class="brush:java">
 * &#064;ViewScoped &#064;Named public class CustomerController extends
 * CrudTableController&lt;Customer&gt; { &#064;Inject private CustomerService
 * service;
 *
 * &#064;Override protected CrudService&lt;Customer&gt; getService() { return
 * service; } }
 * </pre>
 * <ul>
 * <li>Within the <code>getService()</code> method, return the concrete
 * <code>CrudService</code> for the entity type in question which you should
 * dependency-inject into the controller.</li>
 * </ul>
 * <p>
 * Yes, you really only need to implement this single method, and you get best
 * practices CRUD operations for a RESTful PrimeFaces-based JSF application
 * out-of-the-box.
 * </p>
 * <p>
 * CrudController supports <h:link outcome="/crud/index.xhtml">three ways of
 * building a view</h:link>:</p>
 * <ul>
 * <li>a <h:link outcome="/crud/restful/index.xhtml">RESTful, stateless separate
 * master / details view</h:link></li>
 * <li>a <h:link outcome="/crud/stateless/index.xhtml">stateless combined master
 * / detail view</h:link></li>
 * <li>a <h:link outcome="/crud/stateful/index.xhtml">stateful combined master /
 * detail view</h:link></li>
 * </ul>
 * <p>
 * This documentation features a tutorial for every one of these view flavors.
 * Check them out to see which view type best suits your business requirements
 * and how to implement it.</p>
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
