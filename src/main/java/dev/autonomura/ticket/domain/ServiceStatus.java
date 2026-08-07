package dev.autonomura.ticket.domain;

public record ServiceStatus(
        String serviceName,
        String status,
        String message,
        String lastChecked,
        String ownerTeam) {
}
