package org.example;

import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class Main {
    static void main(String[] args) {
        boolean bezi = true;
        int vydelano;
        SouboroveUloziste uloziste = new SouboroveUloziste(Path.of("zakazky.csv"));
        Evidence evidence;
        try {
            evidence = new Evidence(uloziste, Clock.systemDefaultZone());
        } catch (IllegalArgumentException e) {
            IO.println(e.getMessage());
            return;
        }

        while (bezi) {
            vypisMenu();
            try {
                switch (Integer.parseInt(IO.readln("Vyberte jednu z možností: "))) {
                    case 1:
                        bezi = false;
                        break;
                    case 2:
                        novaZakazka(evidence, uloziste);
                        break;
                    case 3:
                        vypisZakazky(evidence);
                        break;
                    case 4:
                        int cislo1 = ZakazkaService.najitZakazku(new KonzolovyVystup(), evidence, IO.readln("Zadejte id zakázky: "));
                        if (cislo1 != -1) {
                            ZakazkaService.posunStav(new KonzolovyVystup(), evidence, cislo1, uloziste);
                        }
                        break;
                    case 5:
                        Map<Stav, Integer> pocetZakazekVJednotlivemStavu = evidence.pocetZakazekVJednotlivemStavu();
                        for (Stav s : Stav.values()) {
                            IO.println("Zakázek ve stavu " + s + ": " + pocetZakazekVJednotlivemStavu.get(s));
                        }
                        break;
                    case 6:
                        vydelano = evidence.soucetCenHotovychZakazek();
                        IO.println("Součet cen hotových zakázek činí: " + vydelano + " Kč");
                        break;
                    case 7:
                        vydelano = evidence.soucetCenZaplacenychZakazek();
                        IO.println("Součet cen zaplacených zakázek činí: " + vydelano + " Kč");
                        break;
                    case 8:
                        vydelano = evidence.soucetZakazekPoTerminu();
                        IO.println("Zakázek po termínu odevzdání: " + vydelano);
                        break;
                    case 9:
                        int cislo2 = ZakazkaService.najitZakazku(new KonzolovyVystup(), evidence, IO.readln("Zadejte id zakázky: "));
                        if (cislo2 != -1) {
                            ZakazkaService.zrusZakazku(new KonzolovyVystup(), evidence, cislo2, uloziste);
                        }
                        break;
                    default:
                        IO.println("Nezadal jste ani jednu z možností.");
                        break;

                }
            } catch (NumberFormatException e) {
                IO.println("Zadejte prosím číslo.");
            } catch (IllegalArgumentException e) {
                IO.println(e.getMessage());
            }
        }
    }

    static void vypisZakazky(Evidence evidence) {
        boolean najitaZakazka = true;
        try {
            IO.println("Pro vypsání celé evidence zakázek zmáčkněte 1.");
            IO.println("Pro vypsání zakázek v určitém stavu zmáčkněte 2.");
            IO.println("Pro vypsání zakázek po termínu, které ještě nejsou hotové zmáčkněte 3.");
            switch (Integer.parseInt(IO.readln("Vaše odpověď: "))) {
                case 1:
                    for (Zakazka z : evidence.getZakazka().values()) {
                        vypisZakazku(z);
                        najitaZakazka = false;
                    }
                    break;
                case 2:
                    IO.println("Pro vypsání zakázek ve stavu POPTAVKA zmáčkněte 1.");
                    IO.println("Pro vypsání zakázek ve stavu ROZPRACOVANO zmáčkněte 2.");
                    IO.println("Pro vypsání zakázek ve stavu HOTOVO zmáčkněte 3.");
                    IO.println("Pro vypsání zakázek ve stavu ZAPLACENO zmáčkněte 4.");
                    switch (Integer.parseInt(IO.readln("Váše odpověď: "))) {
                        case 1:
                            for (Zakazka z : evidence.getZakazka().values()) {
                                if (z.getStav() == Stav.POPTAVKA) {
                                    najitaZakazka = false;
                                    vypisZakazku(z);
                                }
                            }
                            break;
                        case 2:
                            for (Zakazka z : evidence.getZakazka().values()) {
                                if (z.getStav() == Stav.ROZPRACOVANO) {
                                    najitaZakazka = false;
                                    vypisZakazku(z);
                                }
                            }
                            break;
                        case 3:
                            for (Zakazka z : evidence.getZakazka().values()) {
                                if (z.getStav() == Stav.HOTOVO) {
                                    najitaZakazka = false;
                                    vypisZakazku(z);
                                }
                            }
                            break;
                        case 4:
                            for (Zakazka z : evidence.getZakazka().values()) {
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
                    break;
                case 3:
                    for (Zakazka z : evidence.zakazkyPoTerminu()) {
                        najitaZakazka = false;
                        vypisZakazku(z);
                    }
                    break;
                default:
                    IO.println("Nevybral jste ani jednu z možností.");
                    najitaZakazka = false;
                    break;
            }
        } catch (NumberFormatException e) {
            IO.println("Nezadal jste číslo.");
        } catch (IllegalArgumentException e) {
            IO.println(e.getMessage());
        }
        if (najitaZakazka) {
            IO.println("Žádná zakázka nebyla nalezena.");
        }
    }

    static void vypisMenu() {
        IO.println("Vítejte v menu:");
        IO.println("Pro ukončení zmáčkněte 1");
        IO.println("Pro přidání zakázky zmáčkněte 2");
        IO.println("Pro výpis zakázek zmáčkněte 3");
        IO.println("Pro změnu stavu zakázky zmáčkněte 4");
        IO.println("Pro vypsání počtu zakázek v jednotlivém stavu zmáčkněte 5");
        IO.println("Pro vypsání součtu cen hotových zakázek zmáčkněte 6");
        IO.println("Pro vypsání součtu cen zaplacených zakázek zmáčkněte 7");
        IO.println("Pro vypsání součtu zakázek po termínu zmáčkněte 8");
        IO.println("Pro zrušení zakázky zmáčkněte 9");
    }

    static void vypisZakazku(Zakazka z) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        IO.println("id zakázky: " + z.getId());
        IO.println("Jméno zákazníka: " + z.getJmeno());
        IO.println("Popis zakázky: " + z.getPopis());
        IO.println("Stav zakázky: " + z.getStav());
        IO.println("Cena: " + z.getCena() + " Kč");
        IO.println("Datum zadání: " + z.getVytvoreni().format(format));
        IO.println("Datum odevzdání: " + z.getOdevzdani().format(format));
    }

    static void novaZakazka(Evidence evidence, SouboroveUloziste uloziste) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        boolean kontrolaCena = true;
        boolean kontrolaDatum = true;
        int cena = 0;
        LocalDate datumOdevzdani = null;
        String jmeno = IO.readln("Jméno zákazníka: ");
        String popis = IO.readln("Popis zakázky: ");
        while (kontrolaCena) {
            try {
                cena = Integer.parseInt(IO.readln("Odhadovaná cena: "));
                kontrolaCena = false;
            } catch (NumberFormatException e) {
                IO.println("Cena nesmí být nevyplněná a musí být zapsána číselně.");
            }
        }
        while (kontrolaDatum) {
            try {
                datumOdevzdani = LocalDate.parse(IO.readln("Zadej datum odevzdání ve tvaru (dd.MM.yyyy): "), format);
                kontrolaDatum = false;
            } catch (DateTimeParseException e) {
                IO.println("Zadal jsi neplatné datum.");
            } catch (IllegalArgumentException e) {
                IO.println(e.getMessage());
            }
        }
        try {
            int cislo = evidence.pridatZakazku(jmeno, popis, cena, datumOdevzdani);
            uloziste.uloz(evidence);
            IO.println("Zakázka byla úspěšně přidána pod id: " + cislo);
        } catch (IllegalArgumentException e) {
            IO.println(e.getMessage());
        }
    }
}

