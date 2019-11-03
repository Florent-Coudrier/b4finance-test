/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.database.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EntityManagerFactoryProducer {
    
    private static final Logger LOGGER = Logger.getLogger(EntityManagerFactoryProducer.class.getName());

    private static String unitName;


    @Produces
    @ApplicationScoped
    public EntityManagerFactory create() {
        LOGGER.info("Creating entity manager factory...");
        String actualUnitName = (null == unitName) ? this.getUnitName() : unitName;
        Map prop = new HashMap();
        return Persistence.createEntityManagerFactory(actualUnitName, prop);
    }

    private String getUnitName(){
        return "b4finance-pu";
    }

    public void destroy(@Disposes EntityManagerFactory factory) {
        factory.close();
    }

    public static void setUnitName(String unitName) {
        EntityManagerFactoryProducer.unitName = unitName;
    }
}
