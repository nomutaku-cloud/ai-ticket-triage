package dev.autonomura.support.mcp.infrastructure.support;

import dev.autonomura.support.mcp.domain.SupportTeam;

import java.util.Optional;

public interface SupportTeamDataSource {

    /**
     * チーム名でサポートチームの連絡先を検索する。
     *
     * @param teamName チーム名（完全一致・大文字小文字無視）
     * @return チーム情報。見つからない場合は {@link Optional#empty()}
     */
    Optional<SupportTeam> findByTeamName(String teamName);

    /**
     * サービス名でサポートチームの連絡先を検索する。
     *
     * @param serviceName サービス名（完全一致・大文字小文字無視）
     * @return チーム情報。見つからない場合は {@link Optional#empty()}
     */
    Optional<SupportTeam> findByServiceName(String serviceName);
}
