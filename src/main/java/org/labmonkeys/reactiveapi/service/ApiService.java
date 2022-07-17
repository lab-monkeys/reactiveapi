package org.labmonkeys.reactiveapi.service;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.labmonkeys.reactiveapi.api.ServerApi;
import org.labmonkeys.reactiveapi.dto.MessageDto;

@ApplicationScoped
public class ApiService implements ServerApi {
    
    final Logger LOG = Logger.getLogger(ApiService.class);

    @Override
    public Response receiveMessage(MessageDto message) {
        
        LOG.info(message);
        MessageDto response = new MessageDto();
        response.setMessage("Hello To You!");
        response.setMessageId(UUID.randomUUID());
        return(Response.ok(response).build());
        
    }
}
