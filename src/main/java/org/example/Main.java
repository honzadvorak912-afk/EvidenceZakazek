package org.example;

import org.Zakazka;
import org.Evidence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        boolean bezi = true;
        Evidence evidence = new Evidence();

        while (bezi) {
            vypisMenu();
            try {
            switch (Integer.parseInt(IO.readln("Vyberte jednu z možností: "))) {
                case 1:
                    bezi = false;
                    break;
                case 2:
                    evidence.pridatZakazku();
                    break;
                case 3:
                    vypisZakazky(evidence.getZakazka());
                    break;
                case 4:
                    evidence.posunoutStav();
                    break;
                default:
                    IO.println("Nezadal jste ani jednu z možností.");
                    break;

            }
            } catch (NumberFormatException e) {
                IO.println("Zadejte prosím číslo.");
            }
        }
    }

    static void vypisZakazky(List<Zakazka> zakazka) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        IO.println("Pro vypsání celé evidence zakázek zmáčkněte 1.");
        IO.println("Pro vypsání zakázek v určitém stavu zmáčkněte 2.");
        IO.println("Pro vypsání zakázek po termínu, které ještě nejsou hotové zmáčkněte 3.");
        switch (Integer.parseInt(IO.readln("Vaše odpověď: "))) {
            case 1:
                for (Zakazka z : zakazka) {
                    vypisZakazku(z);
                }
                break;
            case 2:
                IO.println("Pro vypsání zakázek ve stavu POPTAVKA zmáčkněte 1.");
                IO.println("Pro vypsání zakázek ve stavu ROZPRACOVANO zmáčkněte 2.");
                IO.println("Pro vypsání zakázek ve stavu HOTOVO zmáčkněte 3.");
                IO.println("Pro vypsání zakázek ve stavu ZAPLACENO zmáčkněte 4.");
                boolean najitaZakazka = true;
                switch (Integer.parseInt(IO.readln("Váše odpověď: "))) {
                    case 1:
                        for (Zakazka z : zakazka) {
                            if (z.getStav() == Stav.POPTAVKA) {
                                najitaZakazka = false;
                                vypisZakazku(z);
                            }
                        }
                        break;
                    case 2:
                        for (Zakazka z : zakazka) {
                            if (z.getStav() == Stav.ROZPRACOVÁNO) {
                                najitaZakazka = false;
                                vypisZakazku(z);
                            }
                        }
                        break;
                    case 3:
                        for (Zakazka z : zakazka) {
                            if (z.getStav() == Stav.HOTOVO) {
                                najitaZakazka = false;
                                vypisZakazku(z);
                            }
                        }
                        break;
                    case 4:
                        for (Zakazka z : zakazka) {
                            if (z.getStav() == Stav.ZAPLACENO) {
                                najitaZakazka = false;
                                vypisZakazku(z);
                            }
                        }
                        break;
                    default:
                        IO.println("Nevybral jsi ani jednu z možností.");
                        break;
                }
                if (najitaZakazka) {
                    IO.println("Ani jedna zakázka není v tomto stavu.");
                }
                break;
            case 3:
                boolean naslaZakazka = true;
                for (Zakazka z: zakazka) {
                    if (z.getOdevzdani().isBefore(LocalDate.now()) && z.getStav() != Stav.ZAPLACENO) {
                        naslaZakazka = false;
                        vypisZakazku(z);
                    }
                }
                if (naslaZakazka) {
                    IO.println("Žádná zakázka není po datumu odevzdání.");
                }
                break;
            default:
                IO.println("Nevybral jste ani jednu z možností.");
                break;

        }

    }
    static void vypisMenu () {
        IO.println("Vitejte v menu:");
        IO.println("Pro ukončení zmáčkněte 1");
        IO.println("Pro přidání zakázky zmáčkněte 2");
        IO.println("Pro výpis zakázek zmáčkněte 3");
        IO.println("Pro změnu stavu zakázky zmáčkněte 4");
    }

    static void vypisZakazku (Zakazka z) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        IO.println("Jméno zákazníka: " + z.getJmeno());
        IO.println("Popis zakázky: " + z.getPopis());
        IO.println("Stav zakázky: " + z.getStav());
        IO.println("Cena: " + z.getCena() + " Kč");
        IO.println("Datum zadání: " + z.getVytvoreni().format(format));
        IO.println("Datum odevzdání: " + z.getOdevzdani().format(format));
    }
}

