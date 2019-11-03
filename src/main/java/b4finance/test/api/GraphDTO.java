/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author g578689
 */
public class GraphDTO {
    
    private Map<String, EntityModelDTO> entities = new HashMap();
    private Map<String, OwnershipEdgeDTO> edges = new HashMap();

    public Map<String, EntityModelDTO> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, EntityModelDTO> entities) {
        this.entities = entities;
    }
    
    public void addEntity(EntityModelDTO entity) {
        entities.put(entity.getName(), entity);
    }

    public Map<String, OwnershipEdgeDTO> getEdges() {
        return edges;
    }

    public void setEdges(Map<String, OwnershipEdgeDTO> edges) {
        this.edges = edges;
    }
    
    public void addEdge(OwnershipEdgeDTO edge) {
        edges.put(edge.getId(), edge);
    }

}
