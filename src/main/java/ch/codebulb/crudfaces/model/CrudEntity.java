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

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import ch.codebulb.crudfaces.service.CrudService;
import ch.codebulb.crudfaces.controller.CrudController;;

/**
 * <p>
 * An abstract generic base class for a persistent business entity (model).
 * </p>
 * <p>
 * Use either the {@link CrudIdentifiable} interface or the
 * <code>CrudEntity</code> class to derive your entity model classes from. This
 * is the only prerequisite to use them with a {@link CrudService} and a
 * {@link CrudController}.</p>
 * <p>
 * The difference between the interface and the class is that the latter
 * provides an auto-generated <code>Long id</code> field implementation
 * out-of-the-box.</p>
 * <p>
 * As an example, a <code>Customer</code> entity can be created by
 * deriving from
 * <code>CrudEntity</code> like so:</p>
 * <pre class="brush:java">
&#064;Entity
public class Customer extends CrudEntity {
    &#064;NotNull
    private String firstName;
    &#064;NotNull
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
</pre>
 * <p>
 * Apart from the <code>&#064;Entity</code> annotation, this class consists
 * solely of business logic.</p>
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
@MappedSuperclass
public abstract class CrudEntity implements CrudIdentifiable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrudIdentifiable)) {
            return false;
        }
        CrudIdentifiable other = (CrudIdentifiable) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + id + " ]";
    }
}
