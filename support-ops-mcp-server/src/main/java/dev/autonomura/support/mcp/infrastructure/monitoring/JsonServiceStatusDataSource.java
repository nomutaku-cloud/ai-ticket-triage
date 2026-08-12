package dev.autonomura.support.mcp.infrastructure.monitoring;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.autonomura.support.mcp.domain.ServiceStatus;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JsonServiceStatusDataSource implements ServiceStatusDataSource {

    private static final org.jboss.logging.Logger LOG =
            org.jboss.logging.Logger.getLogger(JsonServiceStatusDataSource.class);

    private List<ServiceStatus> cache;

    @PostConstruct
    void init() {
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("data/service-status.json")) {
            if (in == null) {
                throw new IllegalStateException("data/service-status.json が見つかりません");
            }
            cache = new ObjectMapper().readValue(in, new TypeReference<>() {});
            LOG.infof("service-status.json を読み込みました: %d 件", cache.size());
        } catch (Exception e) {
            LOG.error("data/service-status.json の読み込みに失敗しました", e);
            throw new RuntimeException("data/service-status.json の読み込みに失敗しました", e);
        }
    }

    @Override
    public Optional<ServiceStatus> findByName(String serviceName) {
        return cache.stream()
                .filter(s -> s.serviceName().equalsIgnoreCase(serviceName))
                .findFirst();
    }
}
