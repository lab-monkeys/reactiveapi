package org.labmonkeys.reactiveapi;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.logging.Logger;
import org.labmonkeys.reactiveapi.api.ServerApi;
import org.labmonkeys.reactiveapi.dto.MessageDto;
import org.labmonkeys.reactiveapi.dto.ReplyDto;
import org.labmonkeys.reactiveapi.mapper.MessageMapper;
import org.labmonkeys.reactiveapi.model.Message;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.vertx.ConsumeEvent;

@Singleton
public class ReactiveApp {

    final Logger LOG = Logger.getLogger(ReactiveApp.class);

    List<Message> sentMessages;

    List<Message> receivedMessages;

    List<Message> responses;

    List<Message> failedMessages;

    boolean pause;

    @Inject
    MessageMapper mapper;

    void startUp(@Observes StartupEvent startupEvent) {

        this.sentMessages = new ArrayList<Message>();
        this.receivedMessages = new ArrayList<Message>();
        this.responses = new ArrayList<Message>();
        this.failedMessages =  new ArrayList<Message>();
        this.pause = false;

    }

    @ConsumeEvent("message")
    public ReplyDto processMessage(MessageDto request) {

        LOG.info("Received Message From Bus: " + request);
        if (pause) {
            try {
                Thread.sleep(2500);
            } catch (Exception e) {
                //TODO: handle exception
            }
            
        }
        this.receivedMessages.add(mapper.dtoToMessage(request));
        ReplyDto response = new ReplyDto(request.messageId(), UUID.randomUUID(), "Hello To You!");
        return response;
    }

    @ConfigProperty(name = "api-server.url")
    private String url;
    
    @Scheduled(every = "{api-server.schedule}")
    public void sendMessage() {

        LOG.info("Scheduler Fired");
        MessageDto message = new MessageDto(UUID.randomUUID(), "Hello There");
        LOG.info("Sending message: " + message);
        this.sentMessages.add(mapper.dtoToMessage(message));
        ServerApi api = RestClientBuilder.newBuilder().baseUri(URI.create(this.url)).build(ServerApi.class);
        api.receiveMessage(message).ifNoItem().after(Duration.ofMillis(2000)).failWith(new Exception("Request Timeout")).subscribe().with(item -> processResponse(item), fail -> handleFailure(message, fail));
    }

    private void processResponse(Response response) {
        LOG.info(response.getStatus());
        MessageDto responseMessage = response.readEntity(MessageDto.class);
        LOG.info(responseMessage);
        this.responses.add(mapper.dtoToMessage(responseMessage));
    }

    private void handleFailure(MessageDto message, Throwable error) {
        LOG.error("Failed sending message: " + message + error.getMessage());
        failedMessages.add(mapper.dtoToMessage(message));
    }

    public List<MessageDto> getSentMessages() {
        return mapper.messagesToDtos(this.sentMessages);
    }

    public List<MessageDto> getReceivedMessages() {
        return mapper.messagesToDtos(this.receivedMessages);
    }

    public List<MessageDto> getResponseMessages() {
        return mapper.messagesToDtos(this.responses);
    }

    public void clearMessages() {
        this.sentMessages.clear();
        this.receivedMessages.clear();
        this.responses.clear();
    }

    public void sleep() {
        this.pause = true;
    }

    public void wakeUp() {
        this.pause = false;
    }
}
