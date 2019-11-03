/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.api.mapper;

import b4finance.test.api.OwnershipEdgeDTO;
import b4finance.test.database.model.OwnershipEdge;

/**
 *
 * @author g578689
 */
public class OwnershipEdgeMapper {
    
    public static OwnershipEdgeDTO toDTO(OwnershipEdge ownerNode) {
        OwnershipEdgeDTO ownerNodeDTO = new OwnershipEdgeDTO();
        //ownerNodeDTO.setChild(EntityModelMapper.toDTO(ownerNode.getChild()));
        if (ownerNode.getChild() != null) {
            ownerNodeDTO.setChildName(ownerNode.getChild().getName());
        }
        if (ownerNode.getParent() != null) {
            ownerNodeDTO.setParentName(ownerNode.getParent().getName());
        }
        
        ownerNodeDTO.setPercentage(ownerNode.getPercentage());
        return ownerNodeDTO;
    }
    
}
