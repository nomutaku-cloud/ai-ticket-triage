package dev.autonomura.support.mcp.infrastructure.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.autonomura.support.mcp.domain.SupportTeam;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JsonSupportTeamDataSource implements SupportTeamDataSource {

    private static final org.jboss.logging.Logger LOG =
            org.jboss.logging.Logger.getLogger(JsonSupportTeamDataSource.class);

    private List<SupportTeam> cache;

    @PostConstruct
    void init() {
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("data/support-teams.json")) {
            if (in == null) {
                throw new IllegalStateException("data/support-teams.json が見つかりません");
            }
            cache = new ObjectMapper().readValue(in, new TypeReference<>() {});
            LOG.infof("support-teams.json を読み込みました: %d 件", cache.size());
        } catch (Exception e) {
            LOG.error("data/support-teams.json の読み込みに失敗しました", e);
            throw new RuntimeException("data/support-teams.json の読み込みに失敗しました", e);
        }
    }

    @Override
    public Optional<SupportTeam> findByTeamName(String teamName) {
        return cache.stream()
                .filter(t -> t.teamName().equalsIgnoreCase(teamName))
                .findFirst();
    }

    @Override
    public Optional<SupportTeam> findByServiceName(String serviceName) {
        return cache.stream()
                .filter(t -> t.serviceName() != null && t.serviceName().equalsIgnoreCase(serviceName))
                .findFirst();
    }
}
