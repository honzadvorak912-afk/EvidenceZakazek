package org.example;

import java.util.Map;

public interface Uloz {
    void pridejZakaznika(Zakaznik zakaznik);
    void ulozZakazku(Zakazka zakazka);
    Map<Integer, Zakaznik> nactiZakazniky();
    Map<Integer, Zakazka> nactiZakazky(Map<Integer, Zakaznik> zakaznici);
    void odeberZakaznika(int id);
    void odeberZakazku(int id);
}
