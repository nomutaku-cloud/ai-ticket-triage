package dev.autonomura.ticket.api;

import dev.autonomura.ticket.ai.TicketAnalyzer;
import dev.autonomura.ticket.ai.TicketAssistant;
import dev.autonomura.ticket.ai.model.TicketAnalysis;
import dev.autonomura.ticket.ai.model.TicketInvestigation;
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

    @Inject
    TicketAssistant ticketAssistant;

    @POST
    @Path("/analyze")
    public TicketAnalysis analyze(String ticket) {
        return ticketAnalyzer.analyze(ticket);
    }

    @POST
    @Path("/investigate")
    public TicketInvestigation investigate(String ticket) {
        return ticketAssistant.investigate(ticket);
    }
}
