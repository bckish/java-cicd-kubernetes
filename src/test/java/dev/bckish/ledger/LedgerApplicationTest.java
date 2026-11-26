package dev.bckish.ledger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LedgerApplicationTest {
    @LocalServerPort int port;
    final HttpClient client = HttpClient.newHttpClient();

    HttpResponse<String> get(String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path)).GET().build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test void readsSyntheticLedger() throws Exception {
        var response = get("/api/ledger");
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"balanceCents\":10000", "\"synthetic\":true", "demo-credit", "demo-debit");
    }

    @Test void probesAndUnknownRoute() throws Exception {
        assertThat(get("/healthz").body()).contains("ok");
        assertThat(get("/readyz").body()).contains("ready");
        assertThat(get("/missing").statusCode()).isEqualTo(404);
    }
}
