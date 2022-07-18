package org.labmonkeys.reactiveapi;

import java.net.URI;
import java.util.UUID;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.logging.Logger;
import org.labmonkeys.reactiveapi.api.ServerApi;
import org.labmonkeys.reactiveapi.dto.MessageDto;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.vertx.ConsumeEvent;

@Singleton
public class ReactiveApp {

    final Logger LOG = Logger.getLogger(ReactiveApp.class);

    @ConsumeEvent("message")
    public MessageDto processMessage(MessageDto request) {

        LOG.info("Received Message From Bus: " + request);

        MessageDto response = new MessageDto();
        response.setMessage("Hello To You!");
        response.setMessageId(UUID.randomUUID());
        return response;
    }

    @ConfigProperty(name = "api-server.url")
    private String url;
    
    @Scheduled(every = "{api-server.schedule}")
    public void sendMessage() {

        LOG.info("Scheduler Fired");
        MessageDto message = new MessageDto();
        message.setMessageId(UUID.randomUUID());
        message.setMessage("Hello There");
        LOG.info("Sending message: " + message);
        ServerApi api = RestClientBuilder.newBuilder().baseUri(URI.create(this.url)).build(ServerApi.class);
        api.receiveMessage(message).subscribe().with(item -> processResponse(item));
    }

    private void processResponse(Response response) {
        LOG.info(response.getStatus());
        MessageDto responseMessage = response.readEntity(MessageDto.class);
        LOG.info(responseMessage);
    }
}
