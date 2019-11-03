
package b4finance.test.database.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({
    @NamedQuery(name = EntityNode.GET_BY_NAME, query = "SELECT e FROM EntityNode e WHERE e.name=:name"),
    @NamedQuery(name = EntityNode.GET_BY_NAME_FETCH_PARENTS, query = "SELECT e FROM EntityNode e LEFT JOIN FETCH e.parents WHERE e.name=:name"),
    @NamedQuery(name = EntityNode.GET_BY_NAME_FETCH_PARENTS_CHILDS, query = "SELECT e FROM EntityNode e "
        + "LEFT JOIN FETCH e.parents LEFT JOIN FETCH e.childs WHERE e.name=:name")
})
@Table(name = "ENTITY_NODE")
public class EntityNode extends PersistableObject implements Serializable {
    
    public final static String GET_BY_NAME = "EntityModel.GET_BY_NAME";
    public final static String GET_BY_NAME_FETCH_PARENTS = "EntityModel.GET_BY_NAME_FETCH_PARENTS";
    public final static String GET_BY_NAME_FETCH_PARENTS_CHILDS = "EntityModel.GET_BY_NAME_FETCH_PARENTS_CHILDS";
    
    public enum CategoryType {
        USER,
        COMPANY
    }
    
    public EntityNode() {
        
    }
    
    @Column(name = "NAME", nullable = false, unique = true, length = 64)
    private String name;
    
    @Column(name = "CATEGORY", nullable = false)
    private CategoryType categoryType = CategoryType.COMPANY;
    
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<OwnershipEdge> childs = new HashSet();
    
    @OneToMany(mappedBy = "child", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<OwnershipEdge> parents = new HashSet();;
    
    public EntityNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public Set<OwnershipEdge> getChilds() {
        return childs;
    }

    public void setChilds(Set<OwnershipEdge> childs) {
        this.childs = childs;
    }
    
    public void addChild(OwnershipEdge child) {
        this.childs.add(child);
    }

    public Set<OwnershipEdge> getParents() {
        return parents;
    }

    public void setParents(Set<OwnershipEdge> parents) {
        this.parents = parents;
    }
    
    public void addParent(OwnershipEdge parent) {
        this.parents.add(parent);
    }

    @Override
    public String toString() {
        return "EntityNode{" + "name=" + name + ", categoryType=" + categoryType + '}';
    }

}
