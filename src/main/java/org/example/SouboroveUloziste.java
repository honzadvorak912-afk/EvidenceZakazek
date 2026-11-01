package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SouboroveUloziste implements Uloz {
    private final Path cestaZakaznik;
    private final Path cestaZakazka;

    public SouboroveUloziste(Path cestaZakazka, Path cestaZakaznik) {
        this.cestaZakazka = cestaZakazka;
        this.cestaZakaznik = cestaZakaznik;
    }
    @Override
    public Map<Integer, Zakazka> nactiZakazky(Map<Integer, Zakaznik> zakaznik) {
        Map<Integer, Zakazka> zakazka = new HashMap<>();
        if (!Files.exists(cestaZakazka)) {
            return zakazka;
        }
        try {
            List<String> list = Files.readAllLines(cestaZakazka);
            for (String s : list) {
                Zakazka z = FormatZakazky.naZakazku(s, zakaznik);
                zakazka.put(z.getId(), z);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Zakázky se nepodařilo načíst ze souboru " + cestaZakazka, e);
        }
        return zakazka;
    }
    @Override
    public Map<Integer, Zakaznik> nactiZakazniky() {
        Map<Integer, Zakaznik> zakaznik = new HashMap<>();
        if (!Files.exists(cestaZakaznik)) {
            return zakaznik;
        }
        try {
            List<String> list = Files.readAllLines(cestaZakaznik);
            for (String s : list) {
                Zakaznik z = FormatZakazky.naZakaznika(s);
                zakaznik.put(z.getId(), z);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Zákazníky se nepodařilo načíst ze souboru " + cestaZakaznik, e);
        }
        return zakaznik;
    }
    @Override
    public void pridejZakaznika(Zakaznik zakaznik) {
        List<String> radkyZakaznici = new ArrayList<>();
        Map<Integer, Zakaznik> zakaznikMap = nactiZakazniky();
        int id = zakaznik.getId();
        zakaznikMap.put(id, zakaznik);
        for (Zakaznik z : zakaznikMap.values()) {
            radkyZakaznici.add(FormatZakazky.zakazniciNaRadek(z));
        }
        try {
            Files.write(cestaZakaznik, radkyZakaznici);
        } catch (IOException e) {
            throw new IllegalStateException("Při ukládání nastala chyba" + cestaZakaznik, e);
        }
    }
    @Override
    public void ulozZakazku(Zakazka zakazka) {
        List<String> radkyZakazky = new ArrayList<>();
        Map<Integer, Zakazka> zakazkaMap = nactiZakazky(nactiZakazniky());
        int id = zakazka.getId();
        zakazkaMap.put(id, zakazka);
        for (Zakazka z : zakazkaMap.values()) {
            radkyZakazky.add(FormatZakazky.zakazkyNaRadek(z));
        }
        try {
            Files.write(cestaZakazka, radkyZakazky);
        } catch (IOException e) {
            throw new IllegalStateException("Při ukládání nastala chyba" + cestaZakazka, e);
        }
    }
    @Override
    public void odeberZakaznika(int id) {
        Map<Integer, Zakaznik> zakaznikMap = nactiZakazniky();
        List<String> radkyZakaznici = new ArrayList<>();
        zakaznikMap.remove(id);
        for (Zakaznik z : zakaznikMap.values()) {
            radkyZakaznici.add(FormatZakazky.zakazniciNaRadek(z));
        }
        try {
            Files.write(cestaZakaznik, radkyZakaznici);
        } catch (IOException e) {
            throw new IllegalStateException("Při ukládání nastala chyba" + cestaZakaznik, e);
        }
    }
    @Override
    public void odeberZakazku(int id) {
        Map<Integer, Zakazka> zakazkaMap = nactiZakazky(nactiZakazniky());
        List<String> radkyZakazky = new ArrayList<>();
        zakazkaMap.remove(id);
        for (Zakazka z : zakazkaMap.values()) {
            radkyZakazky.add(FormatZakazky.zakazkyNaRadek(z));
        }
        try {
            Files.write(cestaZakazka, radkyZakazky);
        } catch (IOException e) {
            throw new IllegalStateException("Při ukládání nastala chyba" + cestaZakazka, e);
        }
    }
}
