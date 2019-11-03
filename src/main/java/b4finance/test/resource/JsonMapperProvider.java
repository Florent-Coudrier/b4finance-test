/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JsonMapperProvider implements ContextResolver<ObjectMapper> {
    
    private final ObjectMapper mapper;

    public JsonMapperProvider() {
        mapper = new ObjectMapper();
        // Prevents from sending `null` values to the client
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Write dates in ISO-8601
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override public ObjectMapper getContext(Class<?> aClass) {
        return mapper;
    }
    
}
