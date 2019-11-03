/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.database.model;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceUnit;
import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

@ApplicationScoped
public class EntityManagerProducer {

    /*@Inject
    private EntityManagerFactory emf;

    @Produces
    @TransactionScoped
    public EntityManager produceEntityManager() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.setFlushMode(FlushModeType.COMMIT); //Avoids the cost of flushing on every query execution
        return entityManager;
    }

    public void disposeOf(@Disposes EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen()) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback(); //Just in case...
            entityManager.close();
        }
    }*/
    
    

    @Inject
    @PersistenceUnitName("b4finance-pu")
    private EntityManagerFactory entityManagerFactory;
    @Produces
    @Default
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    /**
     * Closes the entity manager produced earlier. It's called automatically by CDI container
     *
     * @param entityManager the entity manager produced earlier in the {@link #getEntityManager()} method
     */
    public void closeEntityManager(@Disposes @Default EntityManager entityManager)
    {
        if (entityManager.isOpen())
        {
            entityManager.close();
        }
   }
    /**
     * Closes the entity manager factory instance so that the CDI container can be gracefully shutdown
     */
   @PreDestroy
   public void closeFactory() {
        if(entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
   }

}
