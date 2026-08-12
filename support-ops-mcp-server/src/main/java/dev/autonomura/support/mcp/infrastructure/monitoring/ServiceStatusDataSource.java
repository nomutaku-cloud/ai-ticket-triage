package dev.autonomura.support.mcp.infrastructure.monitoring;

import dev.autonomura.support.mcp.domain.ServiceStatus;

import java.util.Optional;

public interface ServiceStatusDataSource {

    /**
     * サービス名でサービスの稼働状況を検索する。
     *
     * @param serviceName サービス名（完全一致・大文字小文字無視）
     * @return 稼働状況。見つからない場合は {@link Optional#empty()}
     */
    Optional<ServiceStatus> findByName(String serviceName);
}
