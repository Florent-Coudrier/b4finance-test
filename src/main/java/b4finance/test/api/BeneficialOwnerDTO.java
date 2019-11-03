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
public class BeneficialOwnerDTO extends EntityModelDTO implements Serializable {
    
    private double uoa;

    public double getUoa() {
        return uoa;
    }

    public void setUoa(double uoa) {
        this.uoa = uoa;
    }

}
