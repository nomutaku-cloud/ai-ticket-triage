package dev.autonomura.support.mcp.dto;

public record TeamContactResult(
        String teamName,
        String email,
        String slackChannel,
        String resultType   // "FOUND" | "NOT_FOUND" | "ERROR"
) {

    public static TeamContactResult found(String teamName, String email, String slackChannel) {
        return new TeamContactResult(teamName, email, slackChannel, "FOUND");
    }

    public static TeamContactResult notFound() {
        return new TeamContactResult("", "", "", "NOT_FOUND");
    }

    public static TeamContactResult error() {
        return new TeamContactResult("", "", "", "ERROR");
    }
}
