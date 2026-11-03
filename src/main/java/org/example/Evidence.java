package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Evidence {
    private Map<Integer, Zakazka> zakazka = new HashMap<>();
    private Map<Integer, Zakaznik> zakaznik = new HashMap<>();
    private int keyZakazka = 1;
    private int keyZakaznik = 1;
    private final Clock hodiny;

    public Evidence() {
        this(Clock.systemDefaultZone());
    }

    public Evidence(Clock hodiny) {
        this.hodiny = hodiny;
    }

    public Evidence(Uloz uloz, Clock hodiny) {
        this.hodiny = hodiny;
        this.zakaznik = uloz.nactiZakazniky();
        this.zakazka = uloz.nactiZakazky(zakaznik);
        for (Zakazka z : zakazka.values()) {
            keyZakazka = Math.max(z.getId() + 1, keyZakazka);
        }
        for (Zakaznik z : zakaznik.values()) {
            keyZakaznik = Math.max(z.getId() + 1, keyZakaznik);
        }
    }

    public int pridatZakazku(int id_zakaznika, String popis, int cena, LocalDate odevzdani) {
        if (popis.isBlank() || popis.contains(";")) {
            throw new IllegalArgumentException("Popis nesmí být prázdný a nesmí obsahovat ;.");
        }
        if (cena <= 0) {
            throw new IllegalArgumentException("Cena nesmí být záporná.");
        }
        if (odevzdani.isBefore(LocalDate.now(hodiny))) {
            throw new IllegalArgumentException("Datum odevzdání nemůže být před datem zadání.");
        }
        if (!zakaznik.containsKey(id_zakaznika)) {
            throw new IllegalArgumentException("Zákazník neexistuje.");
        }
        int cislo = keyZakazka++;
        Zakazka novaZakazka = new Zakazka(cislo, zakaznik.get(id_zakaznika), popis, cena, Stav.POPTAVKA, LocalDate.now(hodiny), odevzdani);
        zakazka.put(cislo, novaZakazka);
        return cislo;
    }

    public int pridatZakaznika(String jmeno, String telefon) {
        int cislo = keyZakaznik++;
        Zakaznik novyZakaznik = new Zakaznik(jmeno, telefon, cislo);
        zakaznik.put(cislo, novyZakaznik);
        return cislo;
    }

    public Map<Integer, Zakaznik> getZakaznik() {
        return Map.copyOf(zakaznik);
    }

    public Map<Integer, Zakazka> getZakazka() {
        return Map.copyOf(zakazka);
    }

    public boolean posunoutStav(int cislo) {
        if (zakazka.containsKey(cislo) && zakazka.get(cislo).getStav() != Stav.ZAPLACENO) {
            zakazka.get(cislo).posunStav();
            return true;
        }
        return false;
    }


    public Map<Stav, Integer> pocetZakazekVJednotlivemStavu() {
        Map<Stav, Integer> pocetZakazekVJednotlivemStavu = new HashMap<>();
        for (Stav s : Stav.values()) {
            pocetZakazekVJednotlivemStavu.put(s, 0);
        }
        for (Zakazka z : zakazka.values()) {
            pocetZakazekVJednotlivemStavu.put(z.getStav(), pocetZakazekVJednotlivemStavu.get(z.getStav()) + 1);
        }
        return pocetZakazekVJednotlivemStavu;
    }

    public int soucetCenZaplacenychZakazek() {
        int vydelano = 0;
        for (Zakazka z : zakazka.values()) {
            if (z.getStav() == Stav.ZAPLACENO) {
                vydelano = vydelano + z.getCena();
            }
        }
        return vydelano;
    }

    public int soucetCenHotovychZakazek() {
        return zakazka.values().stream()
                .filter(a -> a.getStav() == Stav.HOTOVO)
                .mapToInt(a -> a.getCena())
                .sum();
    }

    public int soucetZakazekPoTerminu() {
        return zakazkyPoTerminu().size();
    }

    public boolean zrusZakazku(int cislo) {
        if (!zakazka.containsKey(cislo) || zakazka.get(cislo).getStav() == Stav.ZAPLACENO) {
            return false;
        } else {
            zakazka.remove(cislo);
            return true;
        }
    }

    public List<Zakazka> zakazkyPoTerminu() {
        return zakazka.values().stream()
                .filter(z -> z.getOdevzdani().isBefore(LocalDate.now(hodiny))
                        && z.getStav() != Stav.ZAPLACENO
                        && z.getStav() != Stav.HOTOVO)
                .toList();
    }

    public boolean zrusZakaznika(int id) {
        if (!zakaznik.containsKey(id)) {
            return false;
        }
        for (Zakazka z : zakazka.values()) {
            if (z.getZakaznikId() == id) {
                return false;
            }
        }
        zakaznik.remove(id);
        return true;
    }
}

