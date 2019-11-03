/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test;

import b4finance.test.database.model.EntityNode;
import b4finance.test.database.model.OwnershipEdge;
import b4finance.test.database.repository.EntityModelRepository;
import b4finance.test.database.repository.OwnerNodeRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 *
 * @author g578689
 */
public class Init implements ServletContextListener {
    
    private static final Logger LOGGER = Logger.getLogger(Init.class.getName());
    
    private CdiContainer cdiContainer;
    
    private static boolean isPopulated = false;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {            
            //CDI container boot
            LOGGER.info("Starting CDI Container...");
            cdiContainer = CdiContainerLoader.getCdiContainer();
            cdiContainer.boot();
            ContextControl contextControl = cdiContainer.getContextControl();
            contextControl.startContexts();
            
            populateTestDatabase();
            
            LOGGER.info("B4finance test application started.");
        } catch (Throwable t) {
            LOGGER.log(SEVERE, "An internal error occurred while initializing the Usage application!", t);
            LOGGER.log(INFO, "B4finance test application stopped.");
        }
    }
    
    private synchronized void populateTestDatabase() {
        if (isPopulated) {
            return;
        }
        LOGGER.info("Populating database...");
        
        Map prop = new HashMap();
        prop.put("hibernate.hbm2ddl.auto", "update");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("b4finance-pu");
        EntityManager em = emf.createEntityManager(prop);
        
        EntityModelRepository entityModelRepo = BeanProvider.getContextualReference(EntityModelRepository.class, false);
        OwnerNodeRepository ownerNodeRepo = BeanProvider.getContextualReference(OwnerNodeRepository.class, false);
        
        List nodes = entityModelRepo.findAll();
        LOGGER.info("Before, all nodes: " + nodes);
        
        EntityNode clientA = new EntityNode("Client A");
        clientA = entityModelRepo.save(clientA);
        EntityNode abcHolding = new EntityNode("ABC Holding LLC");
        abcHolding = entityModelRepo.save(abcHolding);
        EntityNode xyz = new EntityNode("XYZ Investments");
        xyz = entityModelRepo.save(xyz);
        EntityNode sunnyIsl = new EntityNode("Sunny Islands");
        sunnyIsl = entityModelRepo.save(sunnyIsl);
        EntityNode smartInv = new EntityNode("Smart Investments");
        smartInv = entityModelRepo.save(smartInv);
        EntityNode familyCo = new EntityNode("Family Co LLC");
        familyCo = entityModelRepo.save(familyCo);
        EntityNode john = new EntityNode("John");
        john.setCategoryType(EntityNode.CategoryType.USER);
        john = entityModelRepo.save(john);
        EntityNode mary = new EntityNode("Mary");
        mary.setCategoryType(EntityNode.CategoryType.USER);
        mary = entityModelRepo.save(mary);
        
        nodes = entityModelRepo.findAll();
        LOGGER.info("All nodes: " + nodes);
        
        OwnershipEdge ownerNode = new OwnershipEdge(abcHolding, clientA, 0.50);
        ownerNodeRepo.save(ownerNode);
        ownerNode = new OwnershipEdge(smartInv, clientA, 0.10);
        ownerNodeRepo.save(ownerNode);
        ownerNode = new OwnershipEdge(smartInv, abcHolding, 0.50);
        ownerNodeRepo.save(ownerNode);
        ownerNode = new OwnershipEdge(xyz, clientA, 0.40);
        ownerNodeRepo.save(ownerNode);
        ownerNode = new OwnershipEdge(sunnyIsl, abcHolding, 0.50);
        ownerNodeRepo.save(ownerNode);
        ownerNode = new OwnershipEdge(john, sunnyIsl, Double.valueOf(1));
        ownerNodeRepo.save(ownerNode);
        ownerNode = new OwnershipEdge(john, smartInv, 0.50);
        ownerNodeRepo.save(ownerNode);
        ownerNode = new OwnershipEdge(john, familyCo, 0.10);
        ownerNodeRepo.save(ownerNode);
        ownerNode = new OwnershipEdge(mary, familyCo, 0.90);
        ownerNodeRepo.save(ownerNode);
        ownerNode = new OwnershipEdge(familyCo, xyz, 0.60);
        ownerNodeRepo.save(ownerNode);
        isPopulated = true;
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LOGGER.info("B4finance test application stopped.");
    }
    
}
