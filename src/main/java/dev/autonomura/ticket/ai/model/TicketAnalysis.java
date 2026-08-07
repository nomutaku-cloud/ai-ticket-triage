package dev.autonomura.ticket.ai.model;

public record TicketAnalysis(
        String summary,
        String category,
        String priority,
        String recommendedAction,
        String assignedTeam) {
}
