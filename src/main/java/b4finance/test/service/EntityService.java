/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.service;

import b4finance.test.api.BeneficialOwnerDTO;
import b4finance.test.api.GraphDTO;
import b4finance.test.api.request.UpdateOwnershipAmountRequest;
import b4finance.test.api.error.ErrorCode;
import b4finance.test.api.error.SystemException;
import b4finance.test.api.mapper.EntityModelMapper;
import b4finance.test.api.mapper.OwnershipEdgeMapper;
import b4finance.test.api.request.CreateNewEntityRequest;
import b4finance.test.database.model.EntityNode;
import b4finance.test.database.model.EntityNode.CategoryType;
import b4finance.test.database.model.OwnershipEdge;
import b4finance.test.database.repository.EntityModelRepository;
import b4finance.test.database.repository.OwnerNodeRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.deltaspike.core.util.StringUtils;

@Dependent
public class EntityService {
    
    Map<String, BeneficialOwnerDTO> beneficialOwnersMap = new HashMap();
    
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(EntityService.class.getName());
    
    @Inject
    EntityModelRepository entityModelRepository;
    
    @Inject
    OwnerNodeRepository ownerNodeRepository;
    
    public GraphDTO getGraphSubSet(String companyId) {
        EntityNode entityModel = entityModelRepository.getByNameFetchParents(companyId);
        if (entityModel == null) {
            throw new SystemException(ErrorCode.ENTITY_NOT_FOUND, "Could not find entity with name: " + companyId);
        }
        if (!entityModel.getCategoryType().equals(CategoryType.COMPANY)) {
            throw new SystemException(ErrorCode.INVALID_ENTITY_TYPE, "Expected company");
        }

        GraphDTO graph = new GraphDTO();
        graph.addEntity(EntityModelMapper.toDTO(entityModel));
        
        for (OwnershipEdge ownerNode : entityModel.getParents()) {
            graph.addEdge(OwnershipEdgeMapper.toDTO(ownerNode));
            goDeeper(graph, ownerNode);
        }
        return graph;
    }
    
    public ArrayList<BeneficialOwnerDTO> getBeneficialOwners(String companyId) {        
        LOGGER.info("Getting beneficial owners...");
        EntityNode entityModel = entityModelRepository.getByNameFetchParents(companyId);
        if (entityModel == null) {
            throw new SystemException(ErrorCode.ENTITY_NOT_FOUND, "Could not find entity with name: " + companyId);
        }
        if (!entityModel.getCategoryType().equals(CategoryType.COMPANY)) {
            throw new SystemException(ErrorCode.INVALID_ENTITY_TYPE, "Expected company");
        }

        computeEntityModel(entityModel, 0, 0);
        ArrayList<BeneficialOwnerDTO> keyList = new ArrayList(beneficialOwnersMap.values());
        return keyList;
    }
    
    public void updateOwnershipAmount(UpdateOwnershipAmountRequest request) {        
        OwnershipEdge ownership = ownerNodeRepository.getOwnership(request.getChildName(), request.getParentName());
        if (ownership == null) {
            throw new SystemException(ErrorCode.OWNERSHIP_NOT_FOUND, 
                String.format("Could not find ownership with childName %s and parentName %s", request.getChildName(), request.getParentName()));
        }
        ownership.setPercentage(request.getPercentage());
        ownerNodeRepository.save(ownership);
    }
    
    public void createNewEntity(CreateNewEntityRequest request) {        
        EntityNode entity = entityModelRepository.getByName(request.getName());
        if (entity != null) {
            throw new SystemException(ErrorCode.ENTITY_ALREADY_EXISTS);
        }
        entity = new EntityNode(request.getName());
        entity.setCategoryType(CategoryType.valueOf(request.getCategory()));
        if (!StringUtils.isEmpty(request.getParentName())) {
            EntityNode parentEntity = entityModelRepository.getByName(request.getParentName());
            OwnershipEdge ownership = new OwnershipEdge();
            ownership.setChild(entity);
            ownership.setParent(parentEntity);
            ownership.setPercentage(request.getPercentage());
            entity.addParent(ownership);
        } else if (!StringUtils.isEmpty(request.getChildName())) {
            EntityNode childEntity = entityModelRepository.getByName(request.getChildName());
            OwnershipEdge ownership = new OwnershipEdge();
            ownership.setChild(childEntity);
            ownership.setParent(entity);
            ownership.setPercentage(request.getPercentage());
            entity.addChild(ownership);
        } else {
            throw new SystemException(ErrorCode.INVALID_REQUEST, "Expect child or parent name");
        }
        entityModelRepository.save(entity);
    }
    
    public void deleteEntity(String entityName) {        
        EntityNode entity = entityModelRepository.getByNameFetchParentsChilds(entityName);
        if (entity == null) {
            throw new SystemException(ErrorCode.ENTITY_NOT_FOUND);
        }
        
        entityModelRepository.attachAndRemove(entity);
    }
    
    private void goDeeper(GraphDTO graph, OwnershipEdge ownerNode) {
        EntityNode entityModel = entityModelRepository.getByNameFetchParents(ownerNode.getParent().getName());
        graph.addEntity(EntityModelMapper.toDTO(entityModel));
        for (OwnershipEdge parent : entityModel.getParents()) {
            graph.addEdge(OwnershipEdgeMapper.toDTO(ownerNode));
            goDeeper(graph, parent);
        }
    }
    
    private void computeEntityModel(EntityNode entityNode, double currentUoa, int depth) {
        LOGGER.log(Level.INFO, "Current node {0} has current uoa:{1}", new Object[]{entityNode, currentUoa});
        EntityNode entityModelParents = entityModelRepository.getByNameFetchParents(entityNode.getName());
        // If no parent: it's a beneficial owner
        if (entityModelParents.getParents() == null || entityModelParents.getParents().isEmpty()) {
            updateBeneficialOwnerInMap(entityNode, currentUoa);
        }
        depth++;
        for (OwnershipEdge parentNode : entityNode.getParents()) {
            if (depth == 1) {
                computeEntityModel(parentNode.getParent(), parentNode.getPercentage(), depth);
            } else {
                computeEntityModel(parentNode.getParent(), currentUoa * parentNode.getPercentage(), depth);
            } 
        }
    }
    
    private void updateBeneficialOwnerInMap(EntityNode entityNode, double currentUoa) {
        BeneficialOwnerDTO beneficialOwner = beneficialOwnersMap.get(entityNode.getName());
        if (beneficialOwner == null) {
            beneficialOwner = new BeneficialOwnerDTO();
            beneficialOwner.setName(entityNode.getName());
            beneficialOwner.setUoa(currentUoa);
            beneficialOwnersMap.put(entityNode.getName(), beneficialOwner);
        } else {
            double uoa = beneficialOwner.getUoa();
            uoa = uoa + currentUoa;
            beneficialOwner.setUoa(uoa);
            beneficialOwnersMap.put(entityNode.getName(), beneficialOwner);
        }
    }
    
}
