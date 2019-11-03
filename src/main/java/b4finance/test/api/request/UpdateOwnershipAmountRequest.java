/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.api.request;

import javax.validation.constraints.NotNull;

/**
 *
 * @author g578689
 */
public class UpdateOwnershipAmountRequest {
    
    @NotNull
    private String childName;
    
    @NotNull
    private String parentName;
    
    @NotNull
    private double percentage;

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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    
}
