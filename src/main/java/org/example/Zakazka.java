package org.example;

import java.time.LocalDate;

public class Zakazka {
    private final int cisloZakazky;
    private final String jmeno;
    private final String popis;
    private final int cena;
    private Stav stav;
    private final LocalDate vytvoreni;
    private final LocalDate odevzdani;

    public int getId() {
        return cisloZakazky;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getPopis() {
        return popis;
    }

    public int getCena() {
        return cena;
    }

    public Stav getStav() {
        return stav;
    }

    public LocalDate getVytvoreni() {
        return vytvoreni;
    }

    public LocalDate getOdevzdani() {
        return odevzdani;
    }

    public void posunStav() {
        if (stav != Stav.ZAPLACENO) {
            stav = Stav.values()[stav.ordinal() + 1];
        }
    }

    public Zakazka(int cisloZakazky, String jmeno, String popis, int cena, Stav stav, LocalDate vytvoreni, LocalDate odevzdani) {
        this.cisloZakazky = cisloZakazky;
        this.jmeno = jmeno;
        this.popis = popis;
        this.cena = cena;
        this.vytvoreni = vytvoreni;
        this.stav = stav;
        this.odevzdani = odevzdani;
    }
}