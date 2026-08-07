package dev.autonomura.ticket.infrastructure.incident;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.autonomura.ticket.domain.IncidentRecord;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.util.List;

@ApplicationScoped
public class JsonIncidentDataSource implements IncidentDataSource {

    private static final org.jboss.logging.Logger LOG =
            org.jboss.logging.Logger.getLogger(JsonIncidentDataSource.class);

    private static final int MAX_RESULTS = 3;

    private List<IncidentRecord> cache;

    @PostConstruct
    void init() {
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("data/incidents.json")) {
            if (in == null) {
                throw new IllegalStateException("data/incidents.json が見つかりません");
            }
            cache = new ObjectMapper().readValue(in, new TypeReference<>() {});
            LOG.infof("incidents.json を読み込みました: %d 件", cache.size());
        } catch (Exception e) {
            LOG.error("data/incidents.json の読み込みに失敗しました", e);
            throw new RuntimeException("data/incidents.json の読み込みに失敗しました", e);
        }
    }

    @Override
    public List<IncidentRecord> search(String serviceName, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return cache.stream()
                .filter(r -> r.serviceName().equalsIgnoreCase(serviceName))
                .filter(r -> containsKeyword(r, lowerKeyword))
                .limit(MAX_RESULTS)
                .toList();
    }

    private boolean containsKeyword(IncidentRecord record, String lowerKeyword) {
        return contains(record.title(), lowerKeyword)
                || contains(record.description(), lowerKeyword)
                || contains(record.errorMessage(), lowerKeyword);
    }

    private boolean contains(String text, String keyword) {
        return text != null && text.toLowerCase().contains(keyword);
    }
}
