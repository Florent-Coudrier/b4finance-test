/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.resource;

import b4finance.test.api.BeneficialOwnerDTO;
import b4finance.test.api.GraphDTO;
import b4finance.test.api.request.CreateNewEntityRequest;
import b4finance.test.api.request.UpdateOwnershipAmountRequest;
import b4finance.test.service.EntityService;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.deltaspike.core.api.provider.BeanProvider;

@Path("/entities")
public class EntityResource {
    
    private EntityService userService;

    public EntityResource() {
        userService = BeanProvider.getContextualReference(EntityService.class, false);
    }
    
    @GET
    @Path("/{companyId}/graph")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public GraphDTO getGraphSubSet(@PathParam("companyId") String companyId) {
        return userService.getGraphSubSet(companyId);
    }
    
    @GET
    @Path("/{companyId}/owners")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ArrayList<BeneficialOwnerDTO> getBeneficialOwners(@PathParam("companyId") String companyId) {
        return userService.getBeneficialOwners(companyId);
    }
    
    @POST
    @Path("/ownership")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public void updateOwnershipAmount(UpdateOwnershipAmountRequest request) {
        userService.updateOwnershipAmount(request);
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public void addNewEntity(CreateNewEntityRequest request) {
        userService.createNewEntity(request);
    }
    
    @DELETE
    @Path("/{entityId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public void deleteEntity(@PathParam("entityId") String entityId) {
        userService.deleteEntity(entityId);
    }
    
}
