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

public class Evidence {
    private Map<Integer, Zakazka> zakazka = new HashMap<>();
    private int key = 1;
    private final Clock hodiny;

    public Evidence () {
        this(Clock.systemDefaultZone());
    }
    public Evidence(Clock hodiny) {
        this.hodiny = hodiny;
    }
    public Evidence (SouboroveUloziste uloziste, Clock hodiny) {
        this.hodiny = hodiny;
        this.zakazka = uloziste.nacti();
        for (Zakazka z : zakazka.values()) {
            key = Math.max(z.getId() + 1, key);
        }
    }

    public int pridatZakazku(String jmeno, String popis, int cena, LocalDate odevzdani) {
            if (jmeno.isBlank() || jmeno.contains(";")) {
                throw new IllegalArgumentException("Jméno klienta nesmí být prázdné a nesmí obsahovat ;.");
            }
            if (popis.isBlank() || popis.contains(";")) {
                throw new IllegalArgumentException("Popis nesmí být prázdný a nesmí obsahovat ;.");
            }
            if (cena <= 0) {
                throw new IllegalArgumentException("Cena nesmí být záporná.");
            }
            if (odevzdani.isBefore(LocalDate.now(hodiny))) {
                throw new IllegalArgumentException("Datum odevzdání nemůže být před datem zadání.");
            }
            int cislo = key++;
            Zakazka novaZakazka = new Zakazka(cislo, jmeno, popis, cena, Stav.POPTAVKA, LocalDate.now(hodiny), odevzdani);
            zakazka.put(cislo, novaZakazka);
                return cislo;
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
            pocetZakazekVJednotlivemStavu.put(z.getStav(), pocetZakazekVJednotlivemStavu.get(z.getStav())+1);
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
        int vydelano = 0;
        for (Zakazka z : zakazka.values()) {
            if (z.getStav() == Stav.HOTOVO) {
                vydelano = vydelano + z.getCena();
            }
        }
        return vydelano;
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
        List<Zakazka> vysledek = new ArrayList<>();
        for (Zakazka z : zakazka.values()) {
            if (z.getOdevzdani().isBefore(LocalDate.now(hodiny))
                    && z.getStav() != Stav.ZAPLACENO
                    && z.getStav() != Stav.HOTOVO) {
                vysledek.add(z);
            }
        }
        return vysledek;
    }
}

