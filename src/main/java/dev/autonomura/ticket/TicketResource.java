package dev.autonomura.ticket;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/tickets")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class TicketResource {

    @Inject
    TicketAnalyzer ticketAnalyzer;

    @POST
    @Path("/analyze")
    public TicketAnalysis analyze(String ticket) {
        return ticketAnalyzer.analyze(ticket);
    }
}