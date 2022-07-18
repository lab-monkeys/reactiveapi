package org.labmonkeys.reactiveapi.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.labmonkeys.reactiveapi.dto.MessageDto;

import io.smallrye.mutiny.Uni;

@Path("/api-test")
public interface ServerApi {

    @POST
    @Path("/message")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> receiveMessage(MessageDto message);

    @GET
    @Path("/sent")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageDto> getSentMessages();

    @GET
    @Path("/received")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageDto> getReceivedMessages();

    @GET
    @Path("/response")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageDto> getResponseMessages();

}
