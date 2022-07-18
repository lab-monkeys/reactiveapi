package org.labmonkeys.reactiveapi.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.labmonkeys.reactiveapi.ReactiveApp;
import org.labmonkeys.reactiveapi.api.ServerApi;
import org.labmonkeys.reactiveapi.dto.MessageDto;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;

@ApplicationScoped
public class ApiService implements ServerApi {
    
    final Logger LOG = Logger.getLogger(ApiService.class);

    @Inject
    EventBus eventBus;

    @Inject
    ReactiveApp server;

    @Override
    public Uni<Response> receiveMessage(MessageDto message) {
        
        LOG.info("Received Message From API: " + message);
        return eventBus.<MessageDto>request("message", message).onItem().transform(item -> Response.ok(item.body()).build());
    }

    @Override
    public List<MessageDto> getSentMessages() {
        return server.getSentMessages();
    }

    @Override
    public List<MessageDto> getReceivedMessages() {
        return server.getReceivedMessages();
    }

    @Override
    public List<MessageDto> getResponseMessages() {
        return server.getResponseMessages();
    }
}
