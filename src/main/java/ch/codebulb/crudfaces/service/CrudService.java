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
package ch.codebulb.crudfaces.service;

import ch.codebulb.crudfaces.model.CrudEntity;
import ch.codebulb.crudfaces.model.CrudIdentifiable;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

/**
 * <p>
 * An abstract generic base implementation of a CRUD persistence storage access
 * "service" (a "persistence service"), making it easy to derive a
 * best-practices compliant concrete CRUD service implementation.
 * </p>
 * <p>
 * This service realizes the basic CRUD operations:</p>
 * <ul>
 * <li>Create: create() + save()</li>
 * <li>Read: findById(Long id) / findAll()</li>
 * <li>Update: save()</li>
 * <li>Delete: delete()</li>
 * </ul>
 * <p>
 * In order to create a CRUD service for an entity type, make sure the entity
 * implements <code>CrudIdentifiable</code> (or inherits from
 * <code>CrudEntity</code>), implement <code>CrudService</code> for the entity
 * and register it as a CDI bean in the container (depending on beans.xml
 * <code>bean-discovery-mode</code>, explicit registration may not be
 * necessary).</p>
 * <p>
 * For this demo application, the service implementation for the
 * <code>Customer</code> entity extends <code>CrudService</code> like this:</p>
 * <pre class="brush:java">
 * public class CustomerService extends CrudService&lt;Customer&gt; {
 * &#064;Override &#064;PersistenceContext protected void setEm(EntityManager
 * em) { super.setEm(em); }
 *
 * &#064;Override public Customer create() { return new Customer(); }
 *
 * &#064;Override public Class&lt;Customer&gt; getModelClass() { return
 * Customer.class; } }
 * </pre>
 * <ul>
 * <li>Within the <code>setEm(EntityManager)</code> method, simply call the
 * super method. The important part is that you inject your
 * <code>@PersistenceContext</code> in this method by annotation.</li>
 * </ul>
 * <p>
 * You can immediately use this service in a backing bean with full support for
 * CRUD operations on your persistence storage.</p>
 *
                <p>
 * Conveniently, CrudFaces also comes with an alternative implementation of
 * <code>CrudService</code> named <code>CrudServiceMocked</code>. As its name
 * suggests, this implementation's "persistence" functionality is based on a
 * simple <code>HashMap</code> storing the saved entities. Whilst of no use in a
 * real-world production environment, this class might come in handy if you want
 * to try something out without having a proper database / persistence
 * configuration set up. You may then use a <code>CrudServiceMocked</code>
 * implementation as e.g. a <code>@SessionScoped</code> bean, and later change
 * to a true <code>CrudService</code> without any interface changes.</p>
 * <p>
 * In fact, this showcases' live demo uses a <code>@SessionScoped</code>
 * <code>CrudServiceMocked</code> implementation.</p>
 *
                <h2>Known restrictions</h2>
 * <ul>
 * <li>No explicit support for cascading, especially for
 * <code>CrudServiceMocked</code>.</li>
 * </ul>
 * <p>
 * These restrictions are most likely to be addressed and resolved in future
 * CrudFaces versions.</p>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
@Transactional(Transactional.TxType.REQUIRED)
public abstract class CrudService<T extends CrudIdentifiable> implements Serializable {
    EntityManager em;
    
    /**
     * Invokes the constructor for the entity type.
     */
    public abstract T create();
    
    /**
     * Returns the entity type.
     */
    public abstract Class<T> getModelClass();
    
    /**
     * Sets the entity manager. Override this method to dependency-inject an {@link EntityManager}
     * with the associated <code>@PersistenceContext</code>
     */
    protected void setEm(EntityManager em) {
        this.em = em;
    }
    
    /**
     * Returns the entity with the {@link CrudEntity#getId()} provided.
     */
    public T findById(Long id) {
        return em.find(getModelClass(), id);
    }
    
    /**
     * Returns a List of all entities.
     */
    public List<T> findAll() {
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(getModelClass());
        query.select(query.from(getModelClass()));
        return (List<T>) em.createQuery(query).getResultList();
    }
    
    /**
     * Counts the number of entities.
     */
    public long countAll() {
        CriteriaQuery<Long> query = em.getCriteriaBuilder().createQuery(Long.class);
        query.select(em.getCriteriaBuilder().count(query.from(getModelClass())));
        return em.createQuery(query).getSingleResult();
    }
    
    /**
     * Saves / Inserts / Updates the entity provided and returns the updated entity (e.g. updated {@link CrudEntity#getId()} field.<p/>
     * <b>Note:</b> It's important to continue to work with the newly returned, updated entity rather than with the original entity.
     */
    public T save(T entity) {        
        if (entity.getId() == null) {
            em.persist(entity);
        }
        else {
            entity = em.merge(entity);
        }
        em.flush();
        return entity;
    }
    
    /**
     * Deletes the entity with the {@link CrudEntity#getId()} provided.
     */
    public void delete(Long id) {
        T entity = em.getReference(getModelClass(), id);
        em.remove(entity);
    }
}
