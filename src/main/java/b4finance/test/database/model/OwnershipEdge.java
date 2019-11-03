/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.database.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "OWNERSHIP_EDGE")
@NamedQueries({
    @NamedQuery(name = OwnershipEdge.GET_BY_CHILD_AND_PARENT, query = "SELECT o FROM OwnershipEdge o WHERE o.child.name=:childName AND o.parent.name=:parentName")
})
public class OwnershipEdge extends PersistableObject implements Serializable {
    
    public final static String GET_BY_CHILD_AND_PARENT = "OwnershipEdge.GET_BY_CHILD_AND_PARENT";
    
    public OwnershipEdge() {
        
    }
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CHILD", nullable = false)
    private EntityNode child;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PARENT", nullable = false)
    private EntityNode parent;
    
    // Ownership of parent node to the child node. Format: 0.xx
    @Column(name = "PERCENTAGE", nullable = false)
    private Double percentage;
    
    public OwnershipEdge(EntityNode parent, EntityNode child, Double percentage) {
        this.parent = parent;
        this.child = child;
        this.percentage = percentage;
    }

    public EntityNode getChild() {
        return child;
    }

    public void setChild(EntityNode child) {
        this.child = child;
    }

    public EntityNode getParent() {
        return parent;
    }

    public void setParent(EntityNode parent) {
        this.parent = parent;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
    
}
