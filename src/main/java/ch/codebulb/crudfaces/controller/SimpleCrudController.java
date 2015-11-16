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
import ch.codebulb.crudfaces.service.CrudService;
import ch.codebulb.crudfaces.util.FacesHelper;
import ch.codebulb.crudfaces.util.StringsHelper;
import java.beans.Introspector;
import java.io.Serializable;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import org.omnifaces.util.Components;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 * Basic implementation of a CRUD controller.
 * 
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public abstract class SimpleCrudController<T extends CrudIdentifiable> implements Serializable {    
    private List<T> entities;
    private Long currentEntityId;
    private T currentEntity;
    private NavigationOutcome currentEntityLoadedOutcome;
    
    /**
     * Returns the service instance. The service should be dependency-injected into this controller.
     *
     * @return the service
     */
    protected abstract CrudService<T> getService();
    
    /**
     * Fetches the entities from the service.
     */
    public void initEntities() {
        this.entities = getService().findAll();
    }
    
    /**
     * Restores invalid values from submitted values as they are overridden
     * in preValidate (as a tradeoff for a <code>&#064;RequestScoped</code> controller)
     */
    public void postValidate() {
        Components.forEachComponent().invoke(new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent target) {
                if (target instanceof UIInput && !((UIInput)target).isValid()) {
                    ((UIInput) target).setValue(((UIInput) target).getSubmittedValue());
                    return VisitResult.REJECT;
                }
                return VisitResult.ACCEPT;
            }
        });
    }
    
    /**
     * Fetches the current entity from the service.
     */
    public String initCurrentEntity() {
        if (currentEntityLoadedOutcome != null) {
            NavigationOutcome ret = currentEntityLoadedOutcome;
            currentEntityLoadedOutcome = null;
            return resolveUrl(ret).navigateTo();
        }
        
        // load param during preValidate phase (if @RequestScoped)
        String idParam = Faces.getRequestParameterMap().get("id");
        if (!StringsHelper.isEmpty(idParam)) {
            currentEntityId = Long.parseLong(idParam);
        }
        
        // without id param: CREATE
        if (currentEntityId == null) {
            currentEntity = createNewEntity();
        }
        else {
            // with id param: READ
            currentEntity = getService().findById(currentEntityId);
            if (currentEntity == null) {
                String modelName = FacesHelper.i18nOrNullNoCaps(getModelBeanName());
                if (modelName != null) {
                    Messages.addGlobalError("error.read.notFound_", currentEntityId, modelName);
                }
                else {
                    Messages.addGlobalError("error.read.notFound", currentEntityId);
                }
                
                currentEntityLoadedOutcome = NavigationOutcome.LIST;
                return resolveUrl(currentEntityLoadedOutcome).navigateTo();
            }
        }
        currentEntityLoadedOutcome = NavigationOutcome.THIS;
        return resolveUrl(currentEntityLoadedOutcome).navigateTo();
    }
    
    /**
     * Holds all supported navigation outcomes of this view.
     */
    protected static enum NavigationOutcome {
        THIS, LIST
    }
    
    /**
     * Resolves a {@link NavigationOutcome}.
     */
    protected Url resolveUrl(NavigationOutcome outcome) {
        switch (outcome) {
            case THIS: return new Url(null);
            case LIST: return new Url("list.xhtml");
            default: throw new IllegalArgumentException("Navigation outcome not recognized: " + outcome);
        }
    }
    
    private T createNewEntity() {
        return getService().create();
    }
    
    /**
     * Saves the entity provided to the service and redirects to the master view.
     */
    public String save(T currentEntity) {
        getService().save(currentEntity);
        return resolveUrl(NavigationOutcome.LIST).redirect().navigateTo();
    }
    
    /**
     * Deletes the entity provided from the service and redirects to the master view.
     */
    public String delete(Long id) {
        getService().delete(id);
        return resolveUrl(NavigationOutcome.LIST).redirect().navigateTo();
    }
    
    /**
     * Deletes the entity with the id held by GET parameter <code>deleteId</code> from the service and redirects to the master view.
     * A workaround for use with <code>&#064;RequestScoped</code> controller only.
     */
    public String delete() {
        Long id = Long.parseLong(Faces.getRequestParameter("deleteId"));
        return delete(id);
    }
    
    private Class<T> getModelClass() {
        return getService().getModelClass();
    }
    
    private String getModelBeanName() {
        return Introspector.decapitalize(getModelClass().getSimpleName());
    }
    
    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public Long getCurrentEntityId() {
        return currentEntityId;
    }

    public void setCurrentEntityId(Long currentEntityId) {
        this.currentEntityId = currentEntityId;
    }

    public T getCurrentEntity() {
        return currentEntity;
    }

    public void setCurrentEntity(T currentEntity) {
        this.currentEntity = currentEntity;
    }
}
