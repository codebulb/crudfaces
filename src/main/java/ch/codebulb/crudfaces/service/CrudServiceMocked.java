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
package ch.codebulb.crudfaces.service;

import ch.codebulb.crudfaces.model.CrudIdentifiable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A mocked {@link CrudService} implementation which stores entities in a
 * {@link HashMap} rather than persisting them in an actual persistence
 * storage.</p>
 *
 * <p>
 * Whilst of no use in a real-world production environment, this class might
 * come in handy if you want to try something out without having a proper
 * database / persistence configuration set up. You may then use a
 * <code>CrudServiceMocked</code> implementation as e.g. a
 * <code>@SessionScoped</code> bean, and later change to a true
 * {@link CrudService} without any interface changes.</p>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public abstract class CrudServiceMocked<T extends CrudIdentifiable> extends CrudService<T> {  
    private final Map<Long, T> ENTITIES = new HashMap<>();
    private Long currentId = 0L;
    
    @Override
    public T findById(Long id) {
        return ENTITIES.get(id);
    }
    
    @Override
    public List<T> findAll() {
        return new ArrayList<>(ENTITIES.values());
    }
    
    @Override
    public long countAll() {
        return ENTITIES.size();
    }
    
    @Override
    public T save(T entity) {        
        // CREATE
        if (entity.getId() == null) {
            currentId = ++currentId;
            entity.setId(currentId);
        }
        // UPDATE
        ENTITIES.put(entity.getId(), entity);
        
        return entity;
    }
    
    @Override
    public void delete(Long id) {
        ENTITIES.remove(id);
    }
}
