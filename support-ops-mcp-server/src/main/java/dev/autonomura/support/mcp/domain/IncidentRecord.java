package dev.autonomura.support.mcp.domain;

public record IncidentRecord(
        String id,
        String serviceName,
        String title,
        String description,
        String errorMessage,
        String resolution,
        String team) {
}
