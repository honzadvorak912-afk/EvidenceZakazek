package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class EvidenceTest {
    @Test

    void posunoutStavTest () {
        Zakazka zakazka = new Zakazka("Pepa", "kolo", 500, LocalDate.of(2026, 12, 20), Stav.POPTAVKA, LocalDate.now());
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(zakazka);
        evidence.posunoutStav(0);
        assertEquals(Stav.ROZPRACOVANO, zakazka.getStav());
    }
    @Test
    void soucetCenHotovychZakazekTest () {
        Zakazka zakazka3 = new Zakazka("PEPA", "kolo", 2000, LocalDate.of(2026, 12, 20), Stav.HOTOVO, LocalDate.now());
        Zakazka zakazka4 = new Zakazka("PEPA", "kolo", 3000, LocalDate.of(2026, 12, 20), Stav.HOTOVO, LocalDate.now());
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(zakazka3);
        evidence.pridatZakazku(zakazka4);
        assertEquals(5000, evidence.soucetCenHotovychZakazek());
    }
    @Test
    void soucetCenZaplacenychZakazekTest () {
        Zakazka zakazka1 = new Zakazka("PEPA", "kolo", 500, LocalDate.of(2026, 12, 20), Stav.ZAPLACENO, LocalDate.now());
        Zakazka zakazka2 = new Zakazka("PEPA", "kolo", 1000, LocalDate.of(2026, 12, 20), Stav.ZAPLACENO, LocalDate.now());
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(zakazka1);
        evidence.pridatZakazku(zakazka2);
        assertEquals(1500, evidence.soucetCenZaplacenychZakazek());
    }
    @Test
    void soucetZakazekVJednotlivemStavuTest () {
        Zakazka zakazka1 = new Zakazka("PEPA", "kolo", 500, LocalDate.of(2026, 12, 20), Stav.ZAPLACENO, LocalDate.now());
        Zakazka zakazka2 = new Zakazka("PEPA", "kolo", 1000, LocalDate.of(2026, 12, 20), Stav.ROZPRACOVANO, LocalDate.now());
        Zakazka zakazka3 = new Zakazka("PEPA", "kolo", 500, LocalDate.of(2026, 12, 20), Stav.HOTOVO, LocalDate.now());
        Zakazka zakazka4 = new Zakazka("PEPA", "kolo", 1000, LocalDate.of(2026, 12, 20), Stav.HOTOVO, LocalDate.now());
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(zakazka1);
        evidence.pridatZakazku(zakazka2);
        evidence.pridatZakazku(zakazka3);
        evidence.pridatZakazku(zakazka4);
        assertArrayEquals(new  int[]{0, 1, 2, 1}, evidence.pocetZakazekVJednotlivemStavu());
    }
    @Test
    void soucetZakazekPoTerminuTest () {
        Zakazka zakazka1 = new Zakazka("PEPA", "kolo", 500, LocalDate.now(), Stav.POPTAVKA, LocalDate.now().minusDays(5));
        Zakazka zakazka2 = new Zakazka("PEPA", "kolo", 1000, LocalDate.now(), Stav.ROZPRACOVANO, LocalDate.now().plusDays(5));
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(zakazka1);
        evidence.pridatZakazku(zakazka2);
        assertEquals(1, evidence.soucetZakazekPoTerminu());
    }
    @Test
    void zruseniZakazkyTest () {
        Zakazka zakazka1 = new Zakazka("PEPA", "kolo", 500, LocalDate.now(), Stav.POPTAVKA, LocalDate.of(2026, 9, 10));
        Zakazka zakazka2 = new Zakazka("PEPA", "kolo", 1000, LocalDate.now(), Stav.ROZPRACOVANO, LocalDate.of(2026, 9, 20));
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(zakazka1);
        evidence.pridatZakazku(zakazka2);
        evidence.zrusZakazku(0);
        assertEquals(1, evidence.getZakazka().size());
    }
}
