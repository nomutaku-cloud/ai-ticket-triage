package dev.autonomura.ticket.ai.model;

public record TicketInvestigation(
        String summary,
        String category,
        String priority,
        String investigationResult,
        String recommendedAction,
        String assignedTeam) {
}
