/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.api;

import java.io.Serializable;

/**
 *
 * @author g578689
 */
public class OwnershipEdgeDTO implements Serializable {
    
    private String childName;
    private String parentName;
    // Format: 0.xx
    private Double percentage;
    
    public String getId() {
        return childName + parentName;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    
    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

}
