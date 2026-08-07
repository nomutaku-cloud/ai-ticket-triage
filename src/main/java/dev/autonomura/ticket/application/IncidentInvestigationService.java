package dev.autonomura.ticket.application;

import dev.autonomura.ticket.domain.IncidentRecord;
import dev.autonomura.ticket.domain.ServiceStatus;
import dev.autonomura.ticket.domain.SupportTeam;
import dev.autonomura.ticket.infrastructure.incident.IncidentDataSource;
import dev.autonomura.ticket.infrastructure.monitoring.ServiceStatusDataSource;
import dev.autonomura.ticket.infrastructure.support.SupportTeamDataSource;
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
     * サービス名でサービスの稼働状況を返す。見つからない場合は UNKNOWN を返す。
     */
    public ServiceStatus checkServiceStatus(String serviceName) {
        return serviceStatusDataSource.findByName(serviceName)
                .orElse(new ServiceStatus(serviceName, "UNKNOWN", "該当するサービスの情報は確認できませんでした", null, null));
    }

    /**
     * サービス名とキーワードで類似インシデントを検索し、最大3件返す。
     */
    public List<IncidentRecord> searchSimilarIncidents(String serviceName, String keyword) {
        return incidentDataSource.search(serviceName, keyword);
    }

    /**
     * チーム名でサポートチームの連絡先を返す。見つからない場合は UNKNOWN を返す。
     */
    public SupportTeam getSupportTeamContact(String teamName) {
        return supportTeamDataSource.findByTeamName(teamName)
                .orElse(new SupportTeam("UNKNOWN", "", "", ""));
    }
}
