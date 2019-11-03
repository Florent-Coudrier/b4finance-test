/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.database.repository;

import b4finance.test.database.model.EntityNode;
import javax.transaction.Transactional;
import org.apache.deltaspike.data.api.*;

@Repository
@Transactional
public abstract class EntityModelRepository extends AbstractEntityRepository<EntityNode, Long> {
    
    @Query(named= EntityNode.GET_BY_NAME, singleResult = SingleResultType.OPTIONAL)
    public abstract EntityNode getByName(@QueryParam("name") String name);
    
    @Query(named= EntityNode.GET_BY_NAME_FETCH_PARENTS, singleResult = SingleResultType.OPTIONAL)
    public abstract EntityNode getByNameFetchParents(@QueryParam("name") String name);
    
    @Query(named= EntityNode.GET_BY_NAME_FETCH_PARENTS_CHILDS, singleResult = SingleResultType.OPTIONAL)
    public abstract EntityNode getByNameFetchParentsChilds(@QueryParam("name") String name);
    
}
