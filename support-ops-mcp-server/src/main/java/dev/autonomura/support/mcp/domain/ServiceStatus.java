package dev.autonomura.support.mcp.domain;

public record ServiceStatus(
        String serviceName,
        String status,
        String message,
        String lastChecked,
        String ownerTeam) {
}
