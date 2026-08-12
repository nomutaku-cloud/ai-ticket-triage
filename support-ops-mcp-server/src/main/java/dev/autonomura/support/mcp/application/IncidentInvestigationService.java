package dev.autonomura.support.mcp.application;

import dev.autonomura.support.mcp.domain.IncidentRecord;
import dev.autonomura.support.mcp.domain.ServiceStatus;
import dev.autonomura.support.mcp.domain.SupportTeam;
import dev.autonomura.support.mcp.infrastructure.incident.IncidentDataSource;
import dev.autonomura.support.mcp.infrastructure.monitoring.ServiceStatusDataSource;
import dev.autonomura.support.mcp.infrastructure.support.SupportTeamDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class IncidentInvestigationService {

    @Inject
    ServiceStatusDataSource serviceStatusDataSource;

    @Inject
    IncidentDataSource incidentDataSource;

    @Inject
    SupportTeamDataSource supportTeamDataSource;

    /**
     * サービス名でサービスの稼働状況を返す。見つからない場合は empty を返す。
     */
    public java.util.Optional<ServiceStatus> checkServiceStatus(String serviceName) {
        return serviceStatusDataSource.findByName(serviceName);
    }

    /**
     * サービス名とキーワードで類似インシデントを検索し、最大3件返す。
     */
    public List<IncidentRecord> searchSimilarIncidents(String serviceName, String keyword) {
        return incidentDataSource.search(serviceName, keyword);
    }

    /**
     * サービス名でサポートチームの連絡先を返す。見つからない場合は empty を返す。
     */
    public java.util.Optional<SupportTeam> getTeamContactByServiceName(String serviceName) {
        return supportTeamDataSource.findByServiceName(serviceName);
    }
}
