package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

class EvidenceTest {
    @Test
    void posunoutStavTest() {
        String jmeno = "pepa";
        String popis = "kolo";
        int cena = 500;
        LocalDate odevzdani = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(jmeno, popis, cena, odevzdani);
        evidence.posunoutStav(1);
        assertEquals(Stav.ROZPRACOVANO, evidence.getZakazka().get(1).getStav());
        assertEquals("pepa", evidence.getZakazka().get(1).getJmeno());
        assertEquals(500, evidence.getZakazka().get(1).getCena());
    }

    @Test
    void soucetCenHotovychZakazekTest() {
        String jmeno = "pepa";
        String popis = "kolo";
        int cena = 500;
        String jmeno2 = "jirka";
        String popis2 = "auto";
        int cena2 = 1000;
        LocalDate odevzdani = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(jmeno, popis, cena, odevzdani);
        evidence.pridatZakazku(jmeno2, popis2, cena2, odevzdani);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(2);
        evidence.posunoutStav(2);
        assertEquals(1500, evidence.soucetCenHotovychZakazek());
    }

    @Test
    void soucetCenZaplacenychZakazekTest() {
        String jmeno = "pepa";
        String popis = "kolo";
        int cena = 500;
        String jmeno2 = "jirka";
        String popis2 = "auto";
        int cena2 = 1000;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        LocalDate odevzdani2 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(jmeno, popis, cena, odevzdani1);
        evidence.pridatZakazku(jmeno2, popis2, cena2, odevzdani2);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(2);
        evidence.posunoutStav(2);
        evidence.posunoutStav(2);
        assertEquals(1500, evidence.soucetCenZaplacenychZakazek());
    }

    @Test
    void soucetZakazekVJednotlivemStavuTest() {
        String jmeno = "pepa";
        String popis = "kolo";
        int cena = 500;
        String jmeno2 = "jirka";
        String popis2 = "auto";
        int cena2 = 1000;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        LocalDate odevzdani2 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(jmeno, popis, cena, odevzdani1);
        evidence.pridatZakazku(jmeno2, popis2, cena2, odevzdani2);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(2);
        Map<Stav, Integer> m = new HashMap<>();
        m.put(Stav.POPTAVKA, 0);
        m.put(Stav.ROZPRACOVANO, 1);
        m.put(Stav.HOTOVO, 0);
        m.put(Stav.ZAPLACENO, 1);
        assertEquals(m, evidence.pocetZakazekVJednotlivemStavu());
    }

    @Test
    void soucetZakazekPoTerminuTest() {
        PosuvneHodiny hodiny = new PosuvneHodiny(Instant.parse("2026-01-01T12:00:00Z"));
        Evidence evidence = new Evidence(hodiny);

        evidence.pridatZakazku("pepa", "kolo", 500, LocalDate.of(2026, 2, 1));
        evidence.pridatZakazku("jirka", "auto", 1000, LocalDate.of(2026, 12, 1));

        assertEquals(0, evidence.soucetZakazekPoTerminu());

        hodiny.posunO(Duration.ofDays(60));

        assertEquals(1, evidence.soucetZakazekPoTerminu());
        assertEquals(1, evidence.zakazkyPoTerminu().size());
    }

    @Test
    void zruseniZakazkyTest() {
        String jmeno = "pepa";
        String popis = "kolo";
        int cena = 500;
        String jmeno2 = "jirka";
        String popis2 = "auto";
        int cena2 = 1000;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        LocalDate odevzdani2 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(jmeno, popis, cena, odevzdani1);
        evidence.pridatZakazku(jmeno2, popis2, cena2, odevzdani2);
        evidence.zrusZakazku(1);
        assertEquals(1, evidence.getZakazka().size());
    }

    @Test
    void posunZeZaplacenoTest() {
        String jmeno = "pepa";
        String popis = "kolo";
        int cena = 500;
        String jmeno2 = "jirka";
        String popis2 = "auto";
        int cena2 = 1000;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        LocalDate odevzdani2 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(jmeno, popis, cena, odevzdani1);
        evidence.pridatZakazku(jmeno2, popis2, cena2, odevzdani2);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        assertEquals(1, evidence.pocetZakazekVJednotlivemStavu().get(Stav.POPTAVKA));
        assertFalse(evidence.posunoutStav(1));
        assertEquals(Stav.ZAPLACENO, evidence.getZakazka().get(1).getStav());
    }

    @Test
    void zruseniZaplaceneZakazkyTest() {
        String jmeno = "pepa";
        String popis = "kolo";
        int cena = 500;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(jmeno, popis, cena, odevzdani1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        assertFalse(evidence.zrusZakazku(1));
        assertEquals(1, evidence.getZakazka().size());
    }

    @Test
    void zakazkaSNeplatnouHodnotouSeNepridalaTest() {
        String jmenoSpatne = "";
        String jmeno = "pepa";
        String popisSpatne = "";
        String popis = "kolo";
        int cenaSpatne = -1;
        int cena = 500;
        LocalDate odevzdaniSpatne = LocalDate.now().minusDays(1);
        LocalDate odevzdani = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        assertThrows(IllegalArgumentException.class, () -> evidence.pridatZakazku(jmenoSpatne, popis, cena, odevzdani));
        assertThrows(IllegalArgumentException.class, () -> evidence.pridatZakazku(jmeno, popisSpatne, cena, odevzdani));
        assertThrows(IllegalArgumentException.class, () -> evidence.pridatZakazku(jmeno, popis, cenaSpatne, odevzdani));
        assertThrows(IllegalArgumentException.class, () -> evidence.pridatZakazku(jmeno, popis, cena, odevzdaniSpatne));
        assertEquals(0, evidence.getZakazka().size());
    }

    @Test
    void zruseniZakazkyNezmeniIdTest() {
        String jmeno = "pepa";
        String popis = "kolo";
        int cena = 500;
        String jmeno2 = "jirka";
        String popis2 = "auto";
        int cena2 = 1000;
        String jmeno3 = "Franta";
        String popis3 = "pracka";
        int cena3 = 2000;
        LocalDate odevzdani = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(jmeno, popis, cena, odevzdani);
        evidence.pridatZakazku(jmeno2, popis2, cena2, odevzdani);
        evidence.pridatZakazku(jmeno3, popis3, cena3, odevzdani);
        evidence.zrusZakazku(2);
        assertTrue(evidence.getZakazka().containsKey(1));
        assertTrue(evidence.getZakazka().containsKey(3));
        assertFalse(evidence.getZakazka().containsKey(2));
    }

    @Test
    void neexistujiciIdTest() {
        String jmeno = "pepa";
        String popis = "kolo";
        int cena = 500;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(jmeno, popis, cena, odevzdani1);
        assertFalse(evidence.posunoutStav(99));
        assertFalse(evidence.zrusZakazku(99));
        assertEquals(1, evidence.getZakazka().size());
        assertEquals(Stav.POPTAVKA, evidence.getZakazka().get(1).getStav());
    }

    @Test
    void posunZeZaplacenoNejdeUzivatelDostaneZpravuTest() {
        String jmeno = "pepa";
        String popis = "kolo";
        int cena = 500;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakazku(jmeno, popis, cena, odevzdani1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        SberacVystup sberac = new SberacVystup();
        ZakazkaService.zrusZakazku(sberac, evidence, 1);
        assertEquals(Stav.ZAPLACENO, evidence.getZakazka().get(1).getStav());
        assertEquals(List.of("Zakázku nebylo možné zrušit."), sberac.getZpravy());
    }

    @Test
    void ukladaniZakazekTest() {
        Zakazka zakazka1 = new Zakazka(1, "Pepa", "Kolo", 500, Stav.POPTAVKA, LocalDate.now(), LocalDate.of(2026, 12, 10));
        String radek = FormatZakazky.naRadek(zakazka1);
        Zakazka zakazka2 = FormatZakazky.naZakazku(radek);
        assertEquals(zakazka1.getId(), zakazka2.getId());
        assertEquals(zakazka1.getJmeno(), zakazka2.getJmeno());
        assertEquals(zakazka1.getPopis(), zakazka2.getPopis());
        assertEquals(zakazka1.getCena(), zakazka2.getCena());
        assertEquals(zakazka1.getStav(), zakazka2.getStav());
        assertEquals(zakazka1.getVytvoreni(), zakazka2.getVytvoreni());
        assertEquals(zakazka1.getOdevzdani(), zakazka2.getOdevzdani());

    }
}
