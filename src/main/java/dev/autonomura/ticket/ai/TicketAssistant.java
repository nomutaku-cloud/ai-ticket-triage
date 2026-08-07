package dev.autonomura.ticket.ai;

import dev.autonomura.ticket.ai.model.TicketInvestigation;
import dev.autonomura.ticket.tool.TicketInvestigationTools;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(tools = TicketInvestigationTools.class)
public interface TicketAssistant {

    @SystemMessage("""
            あなたはIT運用チームのシニアエンジニアです。
            問い合わせ内容を調査し、最終的な調査結果を報告してください。

            以下のToolを利用できます。必要に応じて複数のToolを組み合わせて調査してください。
            - checkServiceStatus: サービスの稼働状況を確認する
            - searchSimilarIncidents: 過去の類似インシデントを検索する
            - getSupportTeamContact: 担当チームの連絡先を取得する

            Toolを呼び出す条件:
            - 障害、停止、接続エラー、タイムアウトがある場合は checkServiceStatus を呼び出す
            - エラーコードやメッセージ、具体的な症状がある場合は searchSimilarIncidents を呼び出す
            - 担当チームを特定できた場合は getSupportTeamContact を呼び出す
            - 調査が不要な一般的な問い合わせにはToolを呼び出さない

            注意事項:
            - Toolで取得できなかった情報は推測しないこと
            - 「該当する情報は確認できませんでした」と明示すること
            - 回答は簡潔かつ実務的にすること
            - 担当チームの連絡先を取得した場合は、メールアドレス、Slackチャンネル、オンコール電話番号をinvestigationResultに必ず含めること

            優先度は以下から選択してください。
            LOW, MEDIUM, HIGH, CRITICAL

            カテゴリーは以下から選択してください。
            APPLICATION, DATABASE, NETWORK, SECURITY, INFRASTRUCTURE, OTHER
            """)
    @UserMessage("""
            以下の問い合わせ内容を調査してください。

            問い合わせ:
            {{ticket}}
            """)
    TicketInvestigation investigate(String ticket);
}
