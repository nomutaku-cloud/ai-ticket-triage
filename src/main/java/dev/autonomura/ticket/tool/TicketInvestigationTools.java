package dev.autonomura.ticket.tool;

import dev.autonomura.ticket.application.IncidentInvestigationService;
import dev.autonomura.ticket.domain.IncidentRecord;
import dev.autonomura.ticket.domain.ServiceStatus;
import dev.autonomura.ticket.domain.SupportTeam;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class TicketInvestigationTools {

    private static final org.jboss.logging.Logger LOG =
            org.jboss.logging.Logger.getLogger(TicketInvestigationTools.class);

    @Inject
    IncidentInvestigationService service;

    @Tool(name = "checkServiceStatus",
          value = "指定したサービスの現在の稼働状況を確認する。障害、停止、接続エラー、タイムアウトなどが疑われる場合に呼び出す。")
    public ServiceStatus checkServiceStatus(
            @P("稼働状況を確認したいサービスの名前（例: payment-service）") String serviceName) {
        try {
            return service.checkServiceStatus(serviceName);
        } catch (Exception e) {
            LOG.errorf(e, "checkServiceStatus でエラーが発生しました: serviceName=%s", serviceName);
            return new ServiceStatus(serviceName, "UNKNOWN", "調査中にエラーが発生しました", null, "UNKNOWN");
        }
    }

    @Tool(name = "searchSimilarIncidents",
          value = "過去の類似インシデントを検索する。エラーコード、エラーメッセージ、具体的な症状がある場合に呼び出す。")
    public List<IncidentRecord> searchSimilarIncidents(
            @P("検索対象のサービス名（例: payment-service）") String serviceName,
            @P("検索キーワード。エラーコードや症状を表す短い単語（例: timeout, connection refused）") String keyword) {
        try {
            return service.searchSimilarIncidents(serviceName, keyword);
        } catch (Exception e) {
            LOG.errorf(e, "searchSimilarIncidents でエラーが発生しました: serviceName=%s, keyword=%s", serviceName, keyword);
            return Collections.emptyList();
        }
    }

    @Tool(name = "getSupportTeamContact",
          value = "担当チームの連絡先（メール、Slackチャンネル、オンコール電話番号）を取得する。担当チームを特定できた場合に呼び出す。")
    public SupportTeam getSupportTeamContact(
            @P("連絡先を取得したいチームの名前（例: Payment Team）") String teamName) {
        try {
            return service.getSupportTeamContact(teamName);
        } catch (Exception e) {
            LOG.errorf(e, "getSupportTeamContact でエラーが発生しました: teamName=%s", teamName);
            return new SupportTeam("UNKNOWN", "", "", "");
        }
    }
}
