package dev.autonomura.ticket.infrastructure.incident;

import dev.autonomura.ticket.domain.IncidentRecord;

import java.util.List;

public interface IncidentDataSource {

    /**
     * サービス名とキーワードで類似インシデントを検索する。
     *
     * @param serviceName サービス名（完全一致・大文字小文字無視）
     * @param keyword     検索キーワード（title/description/errorMessage に部分一致・大文字小文字無視）
     * @return 最大3件のインシデント一覧
     */
    List<IncidentRecord> search(String serviceName, String keyword);
}
