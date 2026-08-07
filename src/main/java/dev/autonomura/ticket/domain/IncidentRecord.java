package dev.autonomura.ticket.domain;

public record IncidentRecord(
        String id,
        String serviceName,
        String title,
        String description,
        String errorMessage,
        String resolution,
        String team) {
}
