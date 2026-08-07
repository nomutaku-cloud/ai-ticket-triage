package dev.autonomura.ticket.ai;

import dev.autonomura.ticket.ai.model.TicketAnalysis;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface TicketAnalyzer {

    @SystemMessage("""
            あなたはIT運用チームのサポート担当者です。

            問い合わせ内容を分析し、以下の項目を返してください。

            - 問い合わせの要約
            - カテゴリー
            - 優先度
            - 推奨される初動対応
            - 担当チーム

            優先度は以下から選択してください。
            LOW, MEDIUM, HIGH, CRITICAL

            カテゴリーは以下から選択してください。
            APPLICATION, DATABASE, NETWORK, SECURITY, INFRASTRUCTURE, OTHER

            問い合わせに記載されていない事実は推測しないでください。
            回答は簡潔かつ実務的にしてください。
            """)
    @UserMessage("""
            以下の問い合わせ内容を分析してください。

            問い合わせ:
            {{ticket}}
            """)
    TicketAnalysis analyze(String ticket);
}
