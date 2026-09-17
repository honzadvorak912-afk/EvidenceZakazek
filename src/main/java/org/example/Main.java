package org.example;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        boolean bezi = true;
        int vydelano = 0;
        Evidence evidence = new Evidence();

        while (bezi) {
            vypisMenu();
            try {
                switch (Integer.parseInt(IO.readln("Vyberte jednu z možností: "))) {
                    case 1:
                        bezi = false;
                        break;
                    case 2:
                        evidence.pridatZakazku(infNovaZakazka());
                        break;
                    case 3:
                        vypisZakazky(evidence.getZakazka());
                        break;
                    case 4:
                        int cislo = evidence.posunoutStav(najitZakazku(evidence.getZakazka()));
                        if (cislo != -1) {
                            IO.println("Zakazka je ve stavu: " + evidence.getZakazka().get(cislo).getStav());
                        }
                        break;
                    case 5:
                        int[] pocetZakazekVJednotlivemStavu = evidence.pocetZakazekVJednotlivemStavu();
                        for (Stav s : Stav.values()) {
                            IO.println("Zakázek ve stavu " + s + ": " + pocetZakazekVJednotlivemStavu[s.ordinal()]);
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
                        if (evidence.zrusZakazku(Main.najitZakazku(evidence.getZakazka())) == 1) {
                            IO.println("Zakázka byla zrušena.");
                        }
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
        boolean najitaZakazka = true;
        try {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        IO.println("Pro vypsání celé evidence zakázek zmáčkněte 1.");
        IO.println("Pro vypsání zakázek v určitém stavu zmáčkněte 2.");
        IO.println("Pro vypsání zakázek po termínu, které ještě nejsou hotové zmáčkněte 3.");
        switch (Integer.parseInt(IO.readln("Vaše odpověď: "))) {
            case 1:
                for (Zakazka z : zakazka) {
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
                break;
            case 3:
                for (Zakazka z : zakazka) {
                    if (z.getOdevzdani().isBefore(LocalDate.now()) && z.getStav() != Stav.ZAPLACENO) {
                        najitaZakazka = false;
                        vypisZakazku(z);
                    }
                }
                break;
            default:
                IO.println("Nevybral jste ani jednu z možností.");
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
        IO.println("Vitejte v menu:");
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
        IO.println("Jméno zákazníka: " + z.getJmeno());
        IO.println("Popis zakázky: " + z.getPopis());
        IO.println("Stav zakázky: " + z.getStav());
        IO.println("Cena: " + z.getCena() + " Kč");
        IO.println("Datum zadání: " + z.getVytvoreni().format(format));
        IO.println("Datum odevzdání: " + z.getOdevzdani().format(format));
    }

    static int najitZakazku(List<Zakazka> zakazka) {
        try {
            String jmeno = IO.readln("Zadejte jméno na zakázce kterou chcete upravit: ");
            for (int i = 0; i < zakazka.size(); i++) {
                if (jmeno.equals(zakazka.get(i).getJmeno())) {
                    vypisZakazku(zakazka.get(i));
                    IO.println("Pokud se jedná o zakázku kterou jste chtěl upravit, zmáčkněte 1.");
                    IO.println("Pokud se nejedná o zakázku kterou jste chtěl upravit, zmáčkněte 2.");
                    if (Integer.parseInt(IO.readln("Vaše odpověď: ")) == 1) {
                        return i;
                    }
                }
            }
            IO.println("Nebyla nalezena žádná zakázka.");
            return -1;
        } catch (NumberFormatException e) {
            IO.println("Nezadal jste číslo.");
        }
        return -1;
    }

    static Zakazka infNovaZakazka() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            String jmeno = IO.readln("Zadej jméno zákazníka: ");
            if (jmeno.isBlank()) {
                throw new IllegalArgumentException("Jméno klienta nesmí být prázdné.");
            }
            String popis = IO.readln("Popiš co je potřeba opravit: ");
            if (popis.isBlank()) {
                throw new IllegalArgumentException("Popis nesmí být prázdný.");
            }
            int cena = Integer.parseInt(IO.readln("Odhadovaná cena: "));
            if (cena < 0) {
                throw new IllegalArgumentException("Cena nesmí být záporná.");
            }
            LocalDate datumZadání = LocalDate.now();
            LocalDate datumOdevzdání = LocalDate.parse(IO.readln("Zadej datum odevzdání ve tvaru (dd.MM.yyyy): "), format);
            if (datumOdevzdání.isBefore(datumZadání)) {
                throw new IllegalArgumentException("Datum odevzdání nemůže být před datem zadání.");
            }
            Stav stav = org.example.Stav.POPTAVKA;
            Zakazka zakazka = new Zakazka(jmeno, popis, cena, datumZadání, stav, datumOdevzdání);
            IO.println("Úspěšně jste přidal novou zakázku.");
            return zakazka;
        } catch (NumberFormatException e) {
            IO.println("Cena nesmí být nevyplněná a musí být zapsána číselně.");
            return null;
        } catch (IllegalArgumentException e) {
            IO.println(e.getMessage());
            return null;
        } catch (DateTimeParseException e) {
            IO.println("Zadal jsi neplatné datum.");
            return null;
        }
    }

}

