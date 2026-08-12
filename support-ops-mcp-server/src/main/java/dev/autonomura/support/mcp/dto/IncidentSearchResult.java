package dev.autonomura.support.mcp.dto;

import java.util.List;

public record IncidentSearchResult(
        List<IncidentDetail> incidents,
        int totalFound,
        String resultType   // "FOUND" | "ERROR"
) {

    public static IncidentSearchResult found(List<IncidentDetail> incidents) {
        return new IncidentSearchResult(incidents, incidents.size(), "FOUND");
    }

    public static IncidentSearchResult error() {
        return new IncidentSearchResult(List.of(), 0, "ERROR");
    }
}
