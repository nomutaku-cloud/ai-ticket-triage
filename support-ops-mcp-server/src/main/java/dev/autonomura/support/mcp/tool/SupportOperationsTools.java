package dev.autonomura.support.mcp.tool;

import dev.autonomura.support.mcp.application.IncidentInvestigationService;
import dev.autonomura.support.mcp.domain.IncidentRecord;
import dev.autonomura.support.mcp.dto.IncidentDetail;
import dev.autonomura.support.mcp.dto.IncidentSearchResult;
import dev.autonomura.support.mcp.dto.ServiceStatusResult;
import dev.autonomura.support.mcp.dto.TeamContactResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SupportOperationsTools {

    private static final org.jboss.logging.Logger LOG =
            org.jboss.logging.Logger.getLogger(SupportOperationsTools.class);

    @Inject
    IncidentInvestigationService service;

    @Tool(description = "指定したサービスの現在の稼働状況を確認する。障害、停止、接続エラー、タイムアウトなどが疑われる場合に呼び出す。")
    public ServiceStatusResult getServiceStatus(
            @ToolArg(description = "稼働状況を確認したいサービスの名前（例: payment-service）") String serviceName) {
        LOG.infof("[MCP Tool] getServiceStatus start: serviceName=%s", serviceName);
        if (isBlank(serviceName)) {
            LOG.error("[MCP Tool] getServiceStatus error: serviceName is blank");
            return ServiceStatusResult.error(serviceName);
        }
        try {
            ServiceStatusResult result = service.checkServiceStatus(serviceName)
                    .map(s -> ServiceStatusResult.found(s.serviceName(), s.status(), s.message()))
                    .orElseGet(() -> ServiceStatusResult.notFound(serviceName));
            LOG.infof("[MCP Tool] getServiceStatus end: status=%s, resultType=%s", result.status(), result.resultType());
            return result;
        } catch (Exception e) {
            LOG.errorf(e, "[MCP Tool] getServiceStatus error: %s", e.getMessage());
            return ServiceStatusResult.error(serviceName);
        }
    }

    @Tool(description = "過去の類似インシデントを検索する。エラーコード、エラーメッセージ、具体的な症状がある場合に呼び出す。最大3件返す。")
    public IncidentSearchResult searchSimilarIncidents(
            @ToolArg(description = "検索対象のサービス名（例: payment-service）") String serviceName,
            @ToolArg(description = "検索キーワード。エラーコードや症状を表す短い単語（例: timeout, connection refused）") String keyword) {
        LOG.infof("[MCP Tool] searchSimilarIncidents start: serviceName=%s, keyword=%s", serviceName, keyword);
        if (isBlank(serviceName) || isBlank(keyword)) {
            LOG.error("[MCP Tool] searchSimilarIncidents error: serviceName or keyword is blank");
            return IncidentSearchResult.error();
        }
        try {
            List<IncidentDetail> details = service.searchSimilarIncidents(serviceName, keyword).stream()
                    .map(SupportOperationsTools::toDetail)
                    .toList();
            IncidentSearchResult result = IncidentSearchResult.found(details);
            LOG.infof("[MCP Tool] searchSimilarIncidents end: totalFound=%d, resultType=%s", result.totalFound(), result.resultType());
            return result;
        } catch (Exception e) {
            LOG.errorf(e, "[MCP Tool] searchSimilarIncidents error: %s", e.getMessage());
            return IncidentSearchResult.error();
        }
    }

    @Tool(description = "指定したサービスを担当するチームの連絡先（メールアドレス、Slackチャンネル）を取得する。担当チームへの連絡が必要な場合に呼び出す。")
    public TeamContactResult getTeamContact(
            @ToolArg(description = "連絡先を取得したいサービスの名前（例: payment-service）") String serviceName) {
        LOG.infof("[MCP Tool] getTeamContact start: serviceName=%s", serviceName);
        if (isBlank(serviceName)) {
            LOG.error("[MCP Tool] getTeamContact error: serviceName is blank");
            return TeamContactResult.error();
        }
        try {
            TeamContactResult result = service.getTeamContactByServiceName(serviceName)
                    .map(t -> TeamContactResult.found(t.teamName(), t.email(), t.slackChannel()))
                    .orElseGet(TeamContactResult::notFound);
            LOG.infof("[MCP Tool] getTeamContact end: teamName=%s, resultType=%s", result.teamName(), result.resultType());
            return result;
        } catch (Exception e) {
            LOG.errorf(e, "[MCP Tool] getTeamContact error: %s", e.getMessage());
            return TeamContactResult.error();
        }
    }

    private static IncidentDetail toDetail(IncidentRecord r) {
        return new IncidentDetail(r.id(), r.title(), r.description(), r.resolution());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
