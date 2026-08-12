package dev.autonomura.support.mcp.dto;

public record ServiceStatusResult(
        String serviceName,
        String status,
        String message,
        String resultType   // "FOUND" | "NOT_FOUND" | "ERROR"
) {

    public static ServiceStatusResult found(String serviceName, String status, String message) {
        return new ServiceStatusResult(serviceName, status, message, "FOUND");
    }

    public static ServiceStatusResult notFound(String serviceName) {
        return new ServiceStatusResult(
                serviceName, "UNKNOWN", "該当するサービスの情報は確認できませんでした", "NOT_FOUND");
    }

    public static ServiceStatusResult error(String serviceName) {
        return new ServiceStatusResult(
                serviceName, "UNKNOWN", "調査中にエラーが発生しました", "ERROR");
    }
}
