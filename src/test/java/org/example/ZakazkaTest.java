package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class ZakazkaTest {
    @Test
    void soucetHotovychZakazekTest () {
        Zakazka zakazka1 = new Zakazka("PEPA", "kolo", 500, LocalDate.of(2026, 12, 20), Stav.HOTOVO, LocalDate.now());
        Zakazka zakazka2 = new Zakazka("PEPA", "kolo", 1000, LocalDate.of(2026, 12, 20), Stav.HOTOVO, LocalDate.now());
        Zakazka zakazka3 = new Zakazka("PEPA", "kolo", 2000, LocalDate.of(2026, 12, 20), Stav.ZAPLACENO, LocalDate.now());
        Zakazka zakazka4 = new Zakazka("PEPA", "kolo", 3000, LocalDate.of(2026, 12, 20), Stav.ZAPLACENO, LocalDate.now());
        Evidence evidence = new Evidence();
        evidence.soucetCenZaplacenychZakazek();
        assertEquals(5000, evidence.);
    }
    
}
