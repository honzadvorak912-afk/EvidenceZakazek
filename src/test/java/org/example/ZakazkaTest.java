package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

class EvidenceTest {
    @Test
    void posunoutStavTest() {
        String popis = "kolo";
        int cena = 500;
        LocalDate odevzdani = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakaznika("Pepa", "777777777777");
        evidence.pridatZakazku(1, popis, cena, odevzdani);
        evidence.posunoutStav(1);
        assertEquals(Stav.ROZPRACOVANO, evidence.getZakazka().get(1).getStav());
        assertEquals("Pepa", evidence.getZakazka().get(1).getJmeno());
        assertEquals(500, evidence.getZakazka().get(1).getCena());
    }

    @Test
    void soucetCenHotovychZakazekTest() {
        String popis = "kolo";
        int cena = 500;
        String popis2 = "auto";
        int cena2 = 1000;
        LocalDate odevzdani = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakaznika("Pepa", "777777777");
        evidence.pridatZakaznika("Jirka", "777777777");
        evidence.pridatZakazku(1, popis, cena, odevzdani);
        evidence.pridatZakazku(2, popis2, cena2, odevzdani);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(2);
        evidence.posunoutStav(2);
        assertEquals(1500, evidence.soucetCenHotovychZakazek());
    }

    @Test
    void soucetCenZaplacenychZakazekTest() {
        String popis = "kolo";
        int cena = 500;
        String popis2 = "auto";
        int cena2 = 1000;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        LocalDate odevzdani2 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakaznika("Pepa", "777777777");
        evidence.pridatZakaznika("Jirka", "777777777");
        evidence.pridatZakazku(1, popis, cena, odevzdani1);
        evidence.pridatZakazku(2, popis2, cena2, odevzdani2);
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
        String popis = "kolo";
        int cena = 500;
        String popis2 = "auto";
        int cena2 = 1000;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        LocalDate odevzdani2 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakaznika("Pepa", "777777777");
        evidence.pridatZakaznika("Jirka", "777777777");
        evidence.pridatZakazku(1, popis, cena, odevzdani1);
        evidence.pridatZakazku(2, popis2, cena2, odevzdani2);
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
        evidence.pridatZakaznika("Pepa", "777777777");
        evidence.pridatZakaznika("Jirka", "777777777");
        evidence.pridatZakazku(1, "kolo", 500, LocalDate.of(2026, 2, 1));
        evidence.pridatZakazku(2, "auto", 1000, LocalDate.of(2026, 12, 1));

        assertEquals(0, evidence.soucetZakazekPoTerminu());

        hodiny.posunO(Duration.ofDays(60));

        assertEquals(1, evidence.soucetZakazekPoTerminu());
        assertEquals(1, evidence.zakazkyPoTerminu().size());
    }

    @Test
    void zruseniZakazkyTest() {
        String popis = "kolo";
        int cena = 500;
        String popis2 = "auto";
        int cena2 = 1000;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        LocalDate odevzdani2 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakaznika("Pepa", "777777777");
        evidence.pridatZakaznika("Jirka", "777777777");
        evidence.pridatZakazku(1, popis, cena, odevzdani1);
        evidence.pridatZakazku(2, popis2, cena2, odevzdani2);
        evidence.zrusZakazku(1);
        assertEquals(1, evidence.getZakazka().size());
    }

    @Test
    void posunZeZaplacenoTest() {
        String popis = "kolo";
        int cena = 500;
        String popis2 = "auto";
        int cena2 = 1000;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        LocalDate odevzdani2 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakaznika("Pepa", "777777777");
        evidence.pridatZakaznika("Jirka", "777777777");
        evidence.pridatZakazku(1, popis, cena, odevzdani1);
        evidence.pridatZakazku(2, popis2, cena2, odevzdani2);
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
        String popis = "kolo";
        int cena = 500;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakaznika("Pepa", "777777777");
        evidence.pridatZakazku(1, popis, cena, odevzdani1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        assertFalse(evidence.zrusZakazku(1));
        assertEquals(1, evidence.getZakazka().size());
    }

    @Test
    void zakazkaSNeplatnouHodnotouSeNepridalaTest() {
        String popisSpatne = "";
        String popis = "kolo";
        int cenaSpatne = -1;
        int cena = 500;
        LocalDate odevzdaniSpatne = LocalDate.now().minusDays(1);
        LocalDate odevzdani = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        assertThrows(IllegalArgumentException.class, () -> evidence.pridatZakazku(1, popis, cena, odevzdani));
        assertThrows(IllegalArgumentException.class, () -> evidence.pridatZakazku(1, popisSpatne, cena, odevzdani));
        assertThrows(IllegalArgumentException.class, () -> evidence.pridatZakazku(1, popis, cenaSpatne, odevzdani));
        assertThrows(IllegalArgumentException.class, () -> evidence.pridatZakazku(1, popis, cena, odevzdaniSpatne));
        assertEquals(0, evidence.getZakazka().size());
    }

    @Test
    void zruseniZakazkyNezmeniIdTest() {
        String popis = "kolo";
        int cena = 500;
        String popis2 = "auto";
        int cena2 = 1000;
        String popis3 = "pracka";
        int cena3 = 2000;
        LocalDate odevzdani = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakaznika("Pepa", "777777777");
        evidence.pridatZakaznika("Jirka", "777777777");
        evidence.pridatZakaznika("Tomáš", "777777777");
        evidence.pridatZakazku(1, popis, cena, odevzdani);
        evidence.pridatZakazku(2, popis2, cena2, odevzdani);
        evidence.pridatZakazku(3, popis3, cena3, odevzdani);
        evidence.zrusZakazku(2);
        assertTrue(evidence.getZakazka().containsKey(1));
        assertTrue(evidence.getZakazka().containsKey(3));
        assertFalse(evidence.getZakazka().containsKey(2));
    }

    @Test
    void neexistujiciIdTest() {
        String popis = "kolo";
        int cena = 500;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakaznika("Pepa", "777777777");
        evidence.pridatZakazku(1, popis, cena, odevzdani1);
        assertFalse(evidence.posunoutStav(99));
        assertFalse(evidence.zrusZakazku(99));
        assertEquals(1, evidence.getZakazka().size());
        assertEquals(Stav.POPTAVKA, evidence.getZakazka().get(1).getStav());
    }

    @Test
    void posunZeZaplacenoNejdeUzivatelDostaneZpravuTest() {
        Uloz uloz = new SouboroveUloziste(Path.of("zakazkyTest.csv"), Path.of("zakazniciTest.csv"));
        String popis = "kolo";
        int cena = 500;
        LocalDate odevzdani1 = LocalDate.now().plusDays(5);
        Evidence evidence = new Evidence();
        evidence.pridatZakaznika("Pepa", "777777777");
        evidence.pridatZakazku(1, popis, cena, odevzdani1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        evidence.posunoutStav(1);
        SberacVystup sberac = new SberacVystup();
        ZakazkaService.zrusZakazku(sberac, evidence, 1, uloz);
        assertEquals(Stav.ZAPLACENO, evidence.getZakazka().get(1).getStav());
        assertEquals(List.of("Zakázku nebylo možné zrušit."), sberac.getZpravy());
    }

    @Test
    void ukladaniZakazekTest() {
        Map<Integer, Zakaznik> zakaznik = new HashMap<>();
        zakaznik.put(1, new Zakaznik("Pepa", "774687938", 1));
        Zakazka zakazka1 = new Zakazka(1, zakaznik.get(1), "Kolo", 500, Stav.POPTAVKA, LocalDate.now(), LocalDate.of(2026, 12, 10));
        String radek = FormatZakazky.zakazkyNaRadek(zakazka1);
        Zakazka zakazka2 = FormatZakazky.naZakazku(radek, zakaznik);
        assertEquals(zakazka1.getId(), zakazka2.getId());
        assertEquals(zakazka1.getJmeno(), zakazka2.getJmeno());
        assertEquals(zakazka1.getPopis(), zakazka2.getPopis());
        assertEquals(zakazka1.getCena(), zakazka2.getCena());
        assertEquals(zakazka1.getStav(), zakazka2.getStav());
        assertEquals(zakazka1.getVytvoreni(), zakazka2.getVytvoreni());
        assertEquals(zakazka1.getOdevzdani(), zakazka2.getOdevzdani());

    }
    @Test
    void ulozeniNacteniZakazkyTest(@TempDir Path slozka) throws IOException {
        Path soubor1 = slozka.resolve("zakazkyTest2.csv");
        Path soubor2 = slozka.resolve("zakazniciTest2.csv");
        Uloz uloz = new SouboroveUloziste(soubor1, soubor2);
        Evidence evidence = new Evidence(uloz, Clock.systemDefaultZone());

        int pepa = evidence.pridatZakaznika("Pepa", "11111111111");
        uloz.pridejZakaznika(evidence.getZakaznik().get(pepa));
        int jirka = evidence.pridatZakaznika("Jirka", "22222222");
        uloz.pridejZakaznika(evidence.getZakaznik().get(jirka));
        int petr = evidence.pridatZakaznika("Petr", "3333333333333");
        uloz.pridejZakaznika(evidence.getZakaznik().get(petr));
        int tomas = evidence.pridatZakaznika("Tomáš", "444444444");
        uloz.pridejZakaznika(evidence.getZakaznik().get(tomas));

        int kolo = evidence.pridatZakazku(1, "Kolo", 500, LocalDate.of(2026, 12, 10));
        uloz.ulozZakazku(evidence.getZakazka().get(kolo));
        int auto = evidence.pridatZakazku(2, "auto", 8000, LocalDate.of(2026, 12, 10));
        uloz.ulozZakazku(evidence.getZakazka().get(auto));
        int motorka = evidence.pridatZakazku(3, "motorka", 12000, LocalDate.of(2026, 12, 10));
        uloz.ulozZakazku(evidence.getZakazka().get(motorka));
        int kolobezka = evidence.pridatZakazku(4, "koloběžka", 2000, LocalDate.of(2026, 12, 10));
        uloz.ulozZakazku(evidence.getZakazka().get(kolobezka));

        evidence.zrusZakazku(2);
        uloz.odeberZakazku(2);
        evidence.zrusZakaznika(2);
        uloz.odeberZakaznika(2);

        Map<Integer, Zakaznik> nacteneZakaznici = uloz.nactiZakazniky();
        Map<Integer, Zakazka> nacteneZakazky = uloz.nactiZakazky(nacteneZakaznici);
        assertEquals(3, nacteneZakaznici.size());
        assertEquals(3, nacteneZakazky.size());
        assertFalse(evidence.getZakazka().containsKey(2));
        assertFalse(evidence.getZakaznik().containsKey(2));

        Evidence druha = new Evidence(uloz, Clock.systemDefaultZone());

        assertEquals(3, druha.getZakazka().size());
        assertEquals(3, druha.getZakaznik().size());
        assertFalse(druha.getZakazka().containsKey(2));
        assertFalse(druha.getZakaznik().containsKey(2));
        assertEquals("Pepa", druha.getZakazka().get(1).getJmeno());

        int id = druha.pridatZakazku(1, "pračka", 2000, LocalDate.of(2026, 12, 10));
        assertEquals(5, id);
        int id2 = druha.pridatZakaznika("Petr", "444444444444");
        assertEquals(5, id2);
    }
    @Test
    void neexistujiciSoubor (@TempDir Path slozka) {
        Path soubor1 = slozka.resolve("neexistuje1.csv");
        Path soubor2 = slozka.resolve("neexistuje2.csv");
        SouboroveUloziste uloziste = new SouboroveUloziste(soubor1, soubor2);
        Evidence evidence = new Evidence(uloziste, Clock.systemDefaultZone());
        assertEquals(0, evidence.getZakazka().size());
    }
}
