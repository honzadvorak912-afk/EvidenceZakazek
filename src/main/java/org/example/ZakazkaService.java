package org.example;

public class ZakazkaService {
    static void zrusZakazku (Vystup vystup, Evidence evidence, int id, SouboroveUloziste uloziste) {
        if (evidence.zrusZakazku(id)) {
            vystup.zobraz("Zakázka byla zrušena.");
            uloziste.uloz(evidence);
        } else {
            vystup.zobraz("Zakázku nebylo možné zrušit.");
        }
    }

    static void posunStav (Vystup vystup, Evidence evidence, int id, SouboroveUloziste uloziste) {
        if (evidence.posunoutStav(id)) {
            vystup.zobraz("Zakázka byla posunuta.");
            uloziste.uloz(evidence);
        } else {
            vystup.zobraz("Zakázku nebylo možné posunout.");
        }
    }
    static int najitZakazku(Vystup vystup,Evidence evidence, String id) {
        try {
            int cislo = Integer.parseInt(id);
            if (evidence.getZakazka().containsKey(cislo)) {
                return cislo;
            }
            vystup.zobraz("Nebyla nalezena žádná zakázka.");
            return -1;
        } catch (NumberFormatException e) {
            vystup.zobraz("Nezadal jste číslo.");
        }
        return -1;
    }
}
