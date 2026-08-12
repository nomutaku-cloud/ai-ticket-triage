package dev.autonomura.support.mcp.domain;

public record SupportTeam(
        String teamName,
        String serviceName,
        String email,
        String slackChannel,
        String onCallPhone) {
}
