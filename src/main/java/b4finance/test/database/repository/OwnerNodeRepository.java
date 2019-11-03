/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.database.repository;

import b4finance.test.database.model.OwnershipEdge;
import javax.transaction.Transactional;
import org.apache.deltaspike.data.api.*;

@Repository
@Transactional
public abstract class OwnerNodeRepository extends AbstractEntityRepository<OwnershipEdge, Long> {
    
    @Query(named= OwnershipEdge.GET_BY_CHILD_AND_PARENT, singleResult = SingleResultType.OPTIONAL)
    public abstract OwnershipEdge getOwnership(@QueryParam("childName") String childName, @QueryParam("parentName") String parentName);
    
}
