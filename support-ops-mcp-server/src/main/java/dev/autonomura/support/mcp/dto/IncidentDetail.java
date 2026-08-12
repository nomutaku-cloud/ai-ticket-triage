package dev.autonomura.support.mcp.dto;

public record IncidentDetail(
        String incidentId,
        String title,
        String cause,
        String resolution
) {}
