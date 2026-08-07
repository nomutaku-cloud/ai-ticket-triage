package dev.autonomura.ticket.domain;

public record SupportTeam(
        String teamName,
        String email,
        String slackChannel,
        String onCallPhone) {
}
