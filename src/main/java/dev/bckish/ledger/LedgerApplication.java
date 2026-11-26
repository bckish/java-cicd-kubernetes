package dev.bckish.ledger;

import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LedgerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LedgerApplication.class, args);
    }

    public record Entry(String reference, String currency, long amountCents) {}
    private static final List<Entry> ENTRIES = List.of(
        new Entry("demo-credit", "EUR", 12500),
        new Entry("demo-debit", "EUR", -2500));

    @GetMapping("/healthz")
    public Map<String, String> health() { return Map.of("status", "ok"); }

    @GetMapping("/readyz")
    public Map<String, String> ready() { return Map.of("status", "ready"); }

    @GetMapping("/api/ledger")
    public Map<String, Object> ledger() {
        long balanceCents = ENTRIES.stream().mapToLong(Entry::amountCents).sum();
        return Map.of("currency", "EUR", "balanceCents", balanceCents, "entries", ENTRIES, "synthetic", true);
    }
}
