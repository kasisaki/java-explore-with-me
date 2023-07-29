package ru.practicum.statsClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.StatHitDto;

import java.util.List;

@Service
public class StatClient extends BaseClient {

    @Autowired
    public StatClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> appendStats(StatHitDto requestDto) {
        return post("/hit", requestDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, Boolean unique, List<String> uris) {
        String urisString = "";
        if (!(uris == null) && !uris.isEmpty()) {
            urisString = "&uris=" + String.join("&uris=", uris);
        }

        return get("/stats?start=" + start + "&end=" + end + "&unique=" + unique + urisString);
    }

    public ResponseEntity<Object> getEventViews(List<String> uris) {
        String urisString = "";
        if (!(uris == null) && !uris.isEmpty()) {
            urisString = "&uris=" + String.join("&uris=", uris);
        }

        return get("/stats/hits?" + urisString);
    }
}
