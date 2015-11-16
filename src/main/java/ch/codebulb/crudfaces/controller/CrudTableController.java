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

import ch.codebulb.crudfaces.model.CrudIdentifiable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

/**
 * A CRUD controller with support for basic PrimeFaces <code>&lt;p:dataTable&gt;</code> operations. 
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public abstract class CrudTableController<T extends CrudIdentifiable> extends SimpleCrudController<T> {
    private List<T> filteredEntities;
    private Map<String, Object> filterValues = new HashMap<>();
    private T selectedEntity;
    private List<T> selectedEntities = new ArrayList<>();
    private T newEntity;
    private boolean edit = true;
    private List<T> removedEntities = new ArrayList<>();
    private Long currentId = -1L;
    
    /**
     * Initializes a new entity such that a new, empty entity is always available
     * without forcing the user to click a "create new" button first.
     */
    @PostConstruct
    protected void initNewEntity() {
        setNewEntity(getService().create());
    }
    
    /**
     * Deletes the entity provided from the service.
     */
    public void deleteEntity(Long id) {
        T entity = getService().findById(id);
        delete(id);
        
        // delete from in-memory (for AJAX)
        getEntities().remove(entity);
        if (entity.equals(getSelectedEntity())) {
            unselectAll();
        }
        getSelectedEntities().remove(entity);
    }
    
    /**
     * Removes the entity provided (operation not persisted).
     */
    public void removeEntity(Long id) {
        T removedEntity = null;
        for (T entity : getEntities()) {
            if (entity.getId().equals(id)) {
                removedEntity = entity;
            }
        }
        getEntities().remove(removedEntity);
        if (removedEntity.equals(getSelectedEntity())) {
            unselectAll();
        }
        getSelectedEntities().remove(removedEntity);
        
        removedEntities.add(removedEntity);
    }
    
    /**
     * Deletes the {@link #getSelectedEntity()} / {@link #getSelectedEntities()} from the service.
     */
    public String deleteSelected() {
        if (getSelectedEntity() != null && getSelectedEntity().getId() != null) {
            getService().delete(getSelectedEntity().getId());
        }
        getEntities().remove(getSelectedEntity());
        for (T selectedEntity : getSelectedEntities()) {
            getService().delete(selectedEntity.getId());
            // delete from in-memory (for AJAX)
            getEntities().remove(selectedEntity);
        }
        
        unselectAll();
        
        return resolveUrl(NavigationOutcome.LIST).redirect().navigateTo();
    }
    
    /**
     * @see #deleteSelected()
     */
    public void deleteSelectedEntity() {
        deleteSelected();
    }
    
    /**
     * @see #deleteSelected()
     */
    public void deleteSelectedEntities() {
        deleteSelected();
    }
    
    /**
     * Removes the {@link #getSelectedEntity()} / {@link #getSelectedEntities()} (operation not persisted).
     */
    public void removeSelectedEntity() {
        getEntities().remove(getSelectedEntity());
        for (T selectedEntity : getSelectedEntities()) {
            // delete from in-memory (for AJAX)
            getEntities().remove(selectedEntity);
        }
        
        removedEntities.add(getSelectedEntity());
        
        unselectAll();
    }
    
    /**
     * Removes the {@link #getSelectedEntity()} / {@link #getSelectedEntities()} (operation not persisted).
     */
    public void removeSelectedEntities() {
        getEntities().remove(getSelectedEntity());
        for (T selectedEntity : getSelectedEntities()) {
            // delete from in-memory (for AJAX)
            getEntities().remove(selectedEntity);
        }
        
        removedEntities.addAll(getSelectedEntities());
        
        unselectAll();
    }
    
    /**
     * Saves the {@link #getSelectedEntity()} to the service.
     */
    public void saveSelectedEntity() {
        Long id = getSelectedEntity().getId();
        T savedEntity = getService().save(getSelectedEntity());
        if (id == null) {
            getEntities().add(savedEntity);
        }
    }
    
    /**
     * Resets {@link #getSelectedEntity()} / {@link #getSelectedEntities()} / {@link #getNewEntity()}.
     */
    public void unselectAll() {
        this.selectedEntity = null;
        this.selectedEntities = new ArrayList<>();
        setNewEntity(getService().create());
    }

    public T getSelectedEntity() {
        return selectedEntity;
    }

    /**
     * Sets the selected entity.<p/>
     * <b>Note:</b> if the selected entity provided is <code>null</code>, no changes are made. 
     * Use {@link #unsetSelectedEntity()} to set the selectedEntity to <code>null</code>.
     */
    public void setSelectedEntity(T selectedEntity) {
        // Note: this method gets called with null e.g. on PrimeFaces dataTable filtering.
        if (selectedEntity == null) {
            return;
        }

        this.selectedEntity = selectedEntity;
        
        // make selectedEntities a synonym for selectedEntity, but not vice versa
        List<T> selectedEntities = new ArrayList<>(Arrays.asList(selectedEntity));
        setSelectedEntities(selectedEntities);
    }
    
    /**
     * Resets {@link #getSelectedEntity()}.
     */
    public void unsetSelectedEntity() {
        this.selectedEntity = null;
        setSelectedEntities(new ArrayList<T>());
    }
    
    /**
     * Inserts {@link #getNewEntity()} into the service.
     */
    protected void saveNewEntity() {
        Long id = getNewEntity().getId();
        if (getNewEntity().getId() != null) {
            throw new IllegalStateException("id of new entity is supposed to be null, but was " + id + " for new entity " + getNewEntity());
        }
        
        T savedEntity = getService().save(getNewEntity());
        getEntities().add(savedEntity);
        
        // make sure it now is the new selected entry
        this.selectedEntity = savedEntity;
        getSelectedEntities().add(savedEntity);
    }
    
    /**
     * Adds {@link #getNewEntity()} to the list of entities (operation not persisted).
     */
    public void addNewEntity() {
        getNewEntity().setId(currentId--);
        getEntities().add(getNewEntity());
        
        // make sure it now is the new selected entry
        this.selectedEntity = getNewEntity();
        getSelectedEntities().add(getNewEntity());
    }
    
    /**
     * Save the {@link #getSelectedEntity()} to the service; or, if it is <code>null</code>, save {@link #getNewEntity()} to the service.
     */
    public void saveSelectedOrNewEntity() {
        if (getSelectedEntity() != null) {
            saveSelectedEntity();
        }
        else {
            saveNewEntity();
        }
    }
    
    /**
     * Adds {@link #getNewEntity()} if {@link #getSelectedEntity()} is <code>null</code> (operation not persisted).
     */
    public void updateSelectedOrNewEntity() {
        if (getSelectedEntity() == null && getSelectedEntities().isEmpty()) {
            addNewEntity();
        }
    }
    
    /**
     * Sets {@link #getSelectedEntity()} with the entity provided and deactivates edit mode.
     */
    public void setAndViewSelectedEntity(T selectedEntity) {
        setSelectedEntity(selectedEntity);
        setEdit(false);
    }
    
    /**
     * Activates edit mode.
     */
    public void setEdit() {
        setEdit(true);
    }
    
    /**
     * Persists all pending changes to the service.
     */
    public void saveAllEntities() {
        for (T entity : getEntities()) {
            if (entity.getId() < 0) {
                entity.setId(null);
                getService().save(entity);
            }
            else {
                getService().save(entity);
            }
        }
        for (T removedEntity : getRemovedEntities()) {
            getService().delete(removedEntity.getId());
        }
        setRemovedEntities(new ArrayList<T>());
    }
    
    public List<T> getFilteredEntities() {
        return filteredEntities;
    }

    public void setFilteredEntities(List<T> filteredEntities) {
        this.filteredEntities = filteredEntities;
    }

    public Map<String, Object> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(Map<String, Object> filterValues) {
        this.filterValues = filterValues;
    }

    public List<T> getSelectedEntities() {
        return selectedEntities;
    }

    public void setSelectedEntities(List<T> selectedEntities) {
        this.selectedEntities = selectedEntities;
    }

    public T getNewEntity() {
        return newEntity;
    }

    public void setNewEntity(T newEntity) {
        this.newEntity = newEntity;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    private List<T> getRemovedEntities() {
        return removedEntities;
    }

    private void setRemovedEntities(List<T> removedEntities) {
        this.removedEntities = removedEntities;
    }
}
