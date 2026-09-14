package org.example;

import org.evidence;
import org.zakazka;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    public void main(String[] args) {
        boolean bezi = true;
        evidence evidence = new evidence();

        while (bezi) {
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
                default:
                    break;

            }
        }
    }

    void vypisZakazky(List<zakazka> zakazka) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        IO.println("Pro vypsání celé evidence zakázek zmáčkněte 1.");
        IO.println("Pro vypsání zakázek v určitém stavu zmáčkněte 2.");
        IO.println("Pro vypsání zakázek po termínu, které ještě nejsou hotové zmáčkněte 3.");
        switch (Integer.parseInt(IO.readln("Vae odpověď: "))) {
            case 1:
                for (zakazka z : zakazka) {
                    vypisZakazku(z);
                }
                break;
            case 2:
                IO.println("Pro vypsání zakázek ve stavu POPTAVKA zmáčkněte 1.");
                IO.println("Pro vypsání zakázek ve stavu ROZPRACOVANO zmáčkněte 2.");
                IO.println("Pro vypsání zakázek ve stavu HOTOVO zmáčkněte 3.");
                IO.println("Pro vypsání zakázek ve stavu ZAPLACENO zmáčkněte 4.");
                switch (Integer.parseInt(IO.readln("Váše odpověď: "))) {
                    case 1:
                        for (zakazka z : zakazka) {
                            if (z.stav() == stav.POPTAVKA) {
                                vypisZakazku(z);
                            }
                        }
                        break;
                    case 2:
                        for (zakazka z : zakazka) {
                            if (z.stav() == stav.ROZPRACOVÁNO) {
                                vypisZakazku(z);
                            }
                        }
                        break;
                    case 3:
                        for (zakazka z : zakazka) {
                            if (z.stav() == stav.HOTOVO) {
                                vypisZakazku(z);
                            }
                        }
                        break;
                    case 4:
                        for (zakazka z : zakazka) {
                            if (z.stav() == stav.ZAPLACENO) {
                                vypisZakazku(z);
                            }
                        }
                        break;
                    default:
                        IO.println("Nevybral jsi ani jedno z možností.");
                        break;
                }
                break;
            case 3:
                for (zakazka z: zakazka) {
                    if (z.odevzdani().isAfter(LocalDate.now())) {
                        vypisZakazku(z);
                    }
                }
                break;
            default:
                IO.println("Nevybral jste ani jednu z možností.");
                break;

        }

    }
    void vypisZakazku (zakazka z) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        IO.println("Jméno zákazníka: " + z.jmeno());
        IO.println("Popis zakázky: " + z.popis());
        IO.println("Stav zakázky: " + z.stav());
        IO.println("Cena: " + z.cena() + " Kč");
        IO.println("Datum zadání: " + z.vytvoreni().format(format));
        IO.println("Datum odevzdání: " + z.odevzdani().format(format));
    }
}

