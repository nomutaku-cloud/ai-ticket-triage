package dev.autonomura.ticket;

public record TicketAnalysis(
        String summary,
        String category,
        String priority,
        String recommendedAction,
        String assignedTeam) {
}