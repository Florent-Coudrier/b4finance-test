/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.api.mapper;

import b4finance.test.api.EntityModelDTO;
import b4finance.test.api.OwnershipEdgeDTO;
import b4finance.test.database.model.EntityNode;
import b4finance.test.database.model.OwnershipEdge;

/**
 *
 * @author g578689
 */
public class EntityModelMapper {
    
    public static EntityModelDTO toDTO(EntityNode entityModel) {
        EntityModelDTO entityModelDTO = new EntityModelDTO();
        entityModelDTO.setName(entityModel.getName());
        return entityModelDTO;
    }
    
}
