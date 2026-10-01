package org.example;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class OknoEvidence {
    public static void main(String[] args) {
        SouboroveUloziste uloziste = new SouboroveUloziste(Path.of("zakazky.csv"));
        JFrame okno = new JFrame("Evidence");
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setSize(400, 300);
        okno.setLocationRelativeTo(null);
        Evidence evidence;
        Vystup vystup = new OknovyVystup();
        try {
            evidence = new Evidence(uloziste, Clock.systemDefaultZone());
        } catch (IllegalArgumentException e) {
            vystup.zobraz(e.getMessage());
            return;
        }

        JButton konec = new JButton("Konec");
        okno.add(konec, BorderLayout.SOUTH);
        konec.addActionListener(e -> {
            System.exit(0);
        });

        CardLayout menuKarty = new CardLayout();
        JPanel upravaZakazek = new JPanel(new GridBagLayout());
        upravaZakazek.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel vypisInformaci = new JPanel(new GridBagLayout());
        vypisInformaci.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel vypisZakazek = new JPanel();
        vypisZakazek.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel menu = new JPanel(menuKarty);

        JButton pridatZakazku = new JButton("Přidat zakázku");
        JButton odebratZakazku = new JButton("Odebrat zakázku");
        JButton zmenitStavZakazky = new JButton("Změnit stav");
        JButton dalsi1 = new JButton("Další");
        upravaZakazek.add(pridatZakazku);
        upravaZakazek.add(odebratZakazku);
        upravaZakazek.add(zmenitStavZakazky);
        upravaZakazek.add(dalsi1);

        JButton pocetZakazekVJednotliveStavu = new JButton("Počet zakázek v jednotlivém stavu");
        JButton pocetZakazekPoTerminu = new JButton("Počet zakázek po termínu");
        JButton soucetCenHotovychZakazek = new JButton("Součet cen hotových zakázek");
        JButton soucetCenZaplacenychZakazek = new JButton("Součet cen zaplacených zakázek");
        JButton dalsi2 = new JButton("Další");
        vypisInformaci.add(pocetZakazekVJednotliveStavu);
        vypisInformaci.add(pocetZakazekPoTerminu);
        vypisInformaci.add(soucetCenHotovychZakazek);
        vypisInformaci.add(soucetCenZaplacenychZakazek);
        vypisInformaci.add(dalsi2);

        JButton vypisVsechnZakazek = new JButton("Vypsat všechny zakázky");
        JButton vypisZakazekVJednotlivemStavu = new JButton("Vypsat zakázky v jedotlivém stavu");
        JButton vypisZakazekPoTerminuKtereNejsouHotove = new JButton("Nepřipravené zakázyk po termínu");
        JButton dalsi3 = new JButton("Další");
        vypisZakazek.add(vypisVsechnZakazek);
        vypisZakazek.add(vypisZakazekVJednotlivemStavu);
        vypisZakazek.add(vypisZakazekPoTerminuKtereNejsouHotove);
        vypisZakazek.add(dalsi3);

        CardLayout stredKarty = new CardLayout();
        JPanel stred = new JPanel(stredKarty);
        JPanel uvod = new JPanel();
        JPanel pridatZakazkuPanel = new JPanel();
        pridatZakazkuPanel.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel odebratZakazkuPanel = new JPanel();
        odebratZakazkuPanel.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel posunoutStavPanel = new JPanel();
        posunoutStavPanel.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel pocetZakazekVJednotlivemStavuPanel = new JPanel();
        pocetZakazekVJednotlivemStavuPanel.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel pocetZakazekPoTerminuPanel = new JPanel();
        JPanel soucetCenHotovychZakazekPanel = new JPanel();
        JPanel soucetCenZaplacenychZakazekPanel = new JPanel();
        JPanel vypisVsechZakazekPanel = new JPanel();
        vypisVsechZakazekPanel.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel vypisZakazekVJednotlivemStavuZaklad = new JPanel();
        vypisZakazekVJednotlivemStavuZaklad.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel vypisZakazekPoTerminuKtereNejsouHotovePanel = new JPanel();
        stred.add(uvod, "uvod");
        stred.add(pridatZakazkuPanel, "pridatZakazkuPanel");
        stred.add(odebratZakazkuPanel, "odebratZakazkuPanel");
        stred.add(posunoutStavPanel, "posunoutStavPanel");
        stred.add(pocetZakazekVJednotlivemStavuPanel, "pocetZakazekVJednotlivemStavuPanel");
        stred.add(pocetZakazekPoTerminuPanel, "pocetZakazekPoTerminuPanel");
        stred.add(soucetCenHotovychZakazekPanel, "soucetCenHotovychZakazekPanel");
        stred.add(soucetCenZaplacenychZakazekPanel, "soucetCenZaplacenychZakazekPanel");
        stred.add(vypisVsechZakazekPanel, "vypisVsechZakazekPanel");
        stred.add(vypisZakazekVJednotlivemStavuZaklad, "vypisZakazekVJednotlivemStavuZaklad");
        stred.add(vypisZakazekPoTerminuKtereNejsouHotovePanel, "vypisZakazekPoTerminuKtereNejsouHotovePanel");
        okno.add(stred, BorderLayout.CENTER);

        //PŘIDAT ZAKÁZKU

        JTextField jmenoPridatZakazku = new JTextField(10);
        JTextField popisPridatZakazku = new JTextField(10);
        JTextField cenaPridatZakazku = new JTextField(10);
        JTextField datumPridatZakazku = new JTextField(10);
        JButton potvrditPridatZakazku = new JButton("Potvrdit");
        JLabel jmenoNapoveda = new JLabel("Jméno");
        JLabel popisNapoveda = new JLabel("Popis");
        JLabel cenaNapoveda = new JLabel("Cena");
        JLabel datumNapoveda = new JLabel("Datum (dd.MM.yyyy)");
        pridatZakazkuPanel.add(jmenoNapoveda);
        pridatZakazkuPanel.add(jmenoPridatZakazku);
        pridatZakazkuPanel.add(popisNapoveda);
        pridatZakazkuPanel.add(popisPridatZakazku);
        pridatZakazkuPanel.add(cenaNapoveda);
        pridatZakazkuPanel.add(cenaPridatZakazku);
        pridatZakazkuPanel.add(datumNapoveda);
        pridatZakazkuPanel.add(datumPridatZakazku);
        pridatZakazkuPanel.add(potvrditPridatZakazku);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        pridatZakazku.addActionListener(e -> {
            stredKarty.show(stred, "pridatZakazkuPanel");
        });

        potvrditPridatZakazku.addActionListener(e -> {
            try {
                String jmeno = jmenoPridatZakazku.getText();
                String popis = popisPridatZakazku.getText();
                int cena = Integer.parseInt(cenaPridatZakazku.getText());
                LocalDate datumOdevzdání = LocalDate.parse(datumPridatZakazku.getText(), format);
                int cislo = evidence.pridatZakazku(jmeno, popis, cena, datumOdevzdání);
                JOptionPane.showMessageDialog(okno, "Úspěšně jste přidal novou zakázku pod id " + cislo);
                uloziste.uloz(evidence);
                jmenoPridatZakazku.setText("");
                popisPridatZakazku.setText("");
                cenaPridatZakazku.setText("");
                datumPridatZakazku.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(okno, "Cena nesmí být nevyplněná a musí být zapsána číselně.");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(okno, "Zadal jsi neplatné datum.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(okno, ex.getMessage());
            }
        });

        //ODEBRAT ZAKÁZKU

        JLabel jmenoHledaneZakazkyLabel = new JLabel("ID zakázky");
        JTextField jmenoHledaneZakazky = new JTextField(10);
        JButton hledatZakazku = new JButton("Odebrat");
        odebratZakazkuPanel.add(jmenoHledaneZakazkyLabel);
        odebratZakazkuPanel.add(jmenoHledaneZakazky);
        odebratZakazkuPanel.add(hledatZakazku);

        odebratZakazku.addActionListener(e -> {
            stredKarty.show(stred, "odebratZakazkuPanel");
        });

        hledatZakazku.addActionListener(e -> {
            int cislo = ZakazkaService.najitZakazku(new OknovyVystup(), evidence, jmenoHledaneZakazky.getText());
            if (cislo != -1) {
                try {
                    ZakazkaService.zrusZakazku(new OknovyVystup(), evidence, cislo, uloziste);
                } catch (IllegalArgumentException ex) {
                    vystup.zobraz(ex.getMessage());
                }
            }
            jmenoHledaneZakazky.setText("");
        });

        //ZMĚNIT STAV ZAKAZKY

        JLabel jmenoHledaneZakazkyLabel2 = new JLabel("ID zakázky");
        JTextField jmenoHledaneZakazky2 = new JTextField(10);
        posunoutStavPanel.add(jmenoHledaneZakazkyLabel2);
        posunoutStavPanel.add(jmenoHledaneZakazky2);
        JButton zmenit = new JButton("Posunout stav");
        posunoutStavPanel.add(zmenit);

        zmenitStavZakazky.addActionListener(e -> {
            stredKarty.show(stred, "posunoutStavPanel");
        });

        zmenit.addActionListener(e -> {
            int cislo = ZakazkaService.najitZakazku(new OknovyVystup(), evidence, jmenoHledaneZakazky2.getText());
            if (cislo != -1) {
                try {
                    ZakazkaService.posunStav(new OknovyVystup(), evidence, cislo, uloziste);
                } catch (IllegalArgumentException ex) {
                    vystup.zobraz(ex.getMessage());
                }
            }
            jmenoHledaneZakazky2.setText("");
        });

        //POCET ZAKÁZEK V JEDNOTLIVÉM STAVU

        JLabel pocetZadano = new JLabel();
        JLabel pocetRozpracovano = new JLabel();
        JLabel pocetHotovo = new JLabel();
        JLabel pocetZaplaceno = new JLabel();
        pocetZakazekVJednotlivemStavuPanel.add(pocetZadano);
        pocetZakazekVJednotlivemStavuPanel.add(pocetRozpracovano);
        pocetZakazekVJednotlivemStavuPanel.add(pocetHotovo);
        pocetZakazekVJednotlivemStavuPanel.add(pocetZaplaceno);
        pocetZakazekVJednotliveStavu.addActionListener(e -> {
            stredKarty.show(stred, "pocetZakazekVJednotlivemStavuPanel");
            Map<Stav, Integer> pocetZakazekVJednotlivemStavuPole = evidence.pocetZakazekVJednotlivemStavu();
            pocetZadano.setText("Zakázek ve stavu zadáno: " + pocetZakazekVJednotlivemStavuPole.get(Stav.POPTAVKA));
            pocetRozpracovano.setText("Zakázek ve stavu rozpracováno: " + pocetZakazekVJednotlivemStavuPole.get(Stav.ROZPRACOVANO));
            pocetHotovo.setText("Zakázek ve stavu hotovo: " + pocetZakazekVJednotlivemStavuPole.get(Stav.HOTOVO));
            pocetZaplaceno.setText("Zakázek ve stavu zaplaceno: " + pocetZakazekVJednotlivemStavuPole.get(Stav.ZAPLACENO));
        });

        //POCET ZAKAZEK PO TERMINU ODEVZADANI

        JLabel pocetZakazekPoTerminuLabel = new JLabel();
        pocetZakazekPoTerminuPanel.add(pocetZakazekPoTerminuLabel);
        pocetZakazekPoTerminu.addActionListener(e -> {
            stredKarty.show(stred, "pocetZakazekPoTerminuPanel");
            pocetZakazekPoTerminuLabel.setText("Počet zakázek po termínu odevzdání: " + evidence.soucetZakazekPoTerminu());
        });

        //SOUCET CEN HOTOVYCH ZAKAZEK

        JLabel soucetCenHotovychZakazekLabel = new JLabel();
        soucetCenHotovychZakazekPanel.add(soucetCenHotovychZakazekLabel);
        soucetCenHotovychZakazek.addActionListener(e -> {
            stredKarty.show(stred, "soucetCenHotovychZakazekPanel");
            soucetCenHotovychZakazekLabel.setText("Součet cen hotových zakázek: " + evidence.soucetCenHotovychZakazek());
        });

        //SOUCET CEN ZAPLACENYCH ZAKAZEK

        JLabel soucetCenZaplacenychZakazekLabel = new JLabel();
        soucetCenZaplacenychZakazekPanel.add(soucetCenZaplacenychZakazekLabel);
        soucetCenZaplacenychZakazek.addActionListener(e -> {
            stredKarty.show(stred, "soucetCenZaplacenychZakazekPanel");
            soucetCenZaplacenychZakazekLabel.setText("Součet cen zaplacených zakázek: " + evidence.soucetCenZaplacenychZakazek());
        });

        //VYPIS VSECH ZAKAZEK

        JTextArea vypisVsechZakazekArea = new JTextArea(20, 15);
        JScrollPane vypisVsechZakazekAreaScroll = new JScrollPane(vypisVsechZakazekArea);
        vypisVsechZakazekArea.setEditable(false);
        vypisVsechZakazekPanel.add(vypisVsechZakazekAreaScroll, BorderLayout.CENTER);
        vypisVsechnZakazek.addActionListener(e -> {
            stredKarty.show(stred, "vypisVsechZakazekPanel");
            vypisVsechZakazekArea.setText("");
            for (Zakazka z : evidence.getZakazka().values()) {
                vypisVsechZakazekArea.append(z.getId() + "\n");
                vypisVsechZakazekArea.append(z.getJmeno() + "\n");
                vypisVsechZakazekArea.append(z.getPopis() + "\n");
                vypisVsechZakazekArea.append(z.getCena() + "\n");
                vypisVsechZakazekArea.append(z.getStav() + "\n");
                vypisVsechZakazekArea.append(z.getVytvoreni() + "\n");
                vypisVsechZakazekArea.append(z.getOdevzdani() + "\n");
            }
        });

        //VYPIS ZAKAZEK V JEDNOTLIVEM STAVU

        CardLayout vypisZakazekVJednotlivemStavuCard = new CardLayout();
        JPanel vypisZakazekVJednotlivemStavuPanelCard = new JPanel(vypisZakazekVJednotlivemStavuCard);
        JPanel vypisZakazekVJednotlivemStavuMenu = new JPanel();
        vypisZakazekVJednotlivemStavuZaklad.add(vypisZakazekVJednotlivemStavuMenu);
        vypisZakazekVJednotlivemStavuZaklad.add(vypisZakazekVJednotlivemStavuPanelCard);
        JPanel uvod2 = new JPanel();
        JPanel vypisZakazekVJednotlivemStavuPanelPoptavka = new JPanel();
        vypisZakazekVJednotlivemStavuPanelPoptavka.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel vypisZakazekVJednotlivemStavuPanelRozpracovano = new JPanel();
        vypisZakazekVJednotlivemStavuPanelRozpracovano.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel vypisZakazekVJednotlivemStavuPanelHotovo = new JPanel();
        vypisZakazekVJednotlivemStavuPanelHotovo.setLayout(new GridLayout(0, 1, 0, 5));
        JPanel vypisZakazekVJednotlivemStavuPanelZaplaceno = new JPanel();
        vypisZakazekVJednotlivemStavuPanelZaplaceno.setLayout(new GridLayout(0, 1, 0, 5));
        vypisZakazekVJednotlivemStavuPanelPoptavka.setLayout(new GridLayout(0, 1, 0, 5));
        vypisZakazekVJednotlivemStavuPanelCard.add(uvod2, "uvod2");
        vypisZakazekVJednotlivemStavuPanelCard.add(vypisZakazekVJednotlivemStavuPanelPoptavka, "vypisZakazekVJednotlivemStavuPanelPoptavka");
        vypisZakazekVJednotlivemStavuPanelCard.add(vypisZakazekVJednotlivemStavuPanelRozpracovano, "vypisZakazekVJednotlivemStavuPanelRozpracovano");
        vypisZakazekVJednotlivemStavuPanelCard.add(vypisZakazekVJednotlivemStavuPanelHotovo, "vypisZakazekVJednotlivemStavuPanelHotovo");
        vypisZakazekVJednotlivemStavuPanelCard.add(vypisZakazekVJednotlivemStavuPanelZaplaceno, "vypisZakazekVJednotlivemStavuPanelZaplaceno");
        JButton vypisPoptavka = new JButton("Poptávka");
        JButton vypisRozpracovano = new JButton("Rozpracováno");
        JButton vypisHotovo = new JButton("Hotovo");
        JButton vypisZaplaceno = new JButton("Zaplaceno");
        vypisZakazekVJednotlivemStavuMenu.add(vypisPoptavka);
        vypisZakazekVJednotlivemStavuMenu.add(vypisRozpracovano);
        vypisZakazekVJednotlivemStavuMenu.add(vypisHotovo);
        vypisZakazekVJednotlivemStavuMenu.add(vypisZaplaceno);

        vypisZakazekVJednotlivemStavu.addActionListener(e -> {
            stredKarty.show(stred, "vypisZakazekVJednotlivemStavuZaklad");
            vypisZakazekVJednotlivemStavuCard.show(vypisZakazekVJednotlivemStavuPanelCard, "vypisZakazekVJednotlivemStavuPanelPoptavka");
        });

        JTextArea vypisPoptavkaArea = new JTextArea(20, 15);
        JScrollPane vypisPoptavkaAreaScroll = new JScrollPane(vypisPoptavkaArea);
        vypisPoptavkaArea.setEditable(false);
        vypisZakazekVJednotlivemStavuPanelPoptavka.add(vypisPoptavkaAreaScroll, BorderLayout.CENTER);
        vypisPoptavka.addActionListener(e -> {
            vypisZakazekVJednotlivemStavuCard.show(vypisZakazekVJednotlivemStavuPanelCard, "vypisZakazekVJednotlivemStavuPanelPoptavka");
            vypisPoptavkaArea.setText("");
            for (Zakazka z : evidence.getZakazka().values()) {
                if (z.getStav() == Stav.POPTAVKA) {
                    vypisPoptavkaArea.append(z.getId() + "\n");
                    vypisPoptavkaArea.append(z.getJmeno() + "\n");
                    vypisPoptavkaArea.append(z.getPopis() + "\n");
                    vypisPoptavkaArea.append(z.getCena() + "\n");
                    vypisPoptavkaArea.append(z.getStav() + "\n");
                    vypisPoptavkaArea.append(z.getVytvoreni() + "\n");
                    vypisPoptavkaArea.append(z.getOdevzdani() + "\n");
                }
            }
        });

        JTextArea vypisRozpracovanoArea = new JTextArea(20, 15);
        JScrollPane vypisRozpracovanoAreaScroll = new JScrollPane(vypisRozpracovanoArea);
        vypisRozpracovanoArea.setEditable(false);
        vypisZakazekVJednotlivemStavuPanelRozpracovano.add(vypisRozpracovanoAreaScroll);
        vypisRozpracovano.addActionListener(e -> {
            vypisZakazekVJednotlivemStavuCard.show(vypisZakazekVJednotlivemStavuPanelCard, "vypisZakazekVJednotlivemStavuPanelRozpracovano");
            vypisRozpracovanoArea.setText("");
            for (Zakazka z : evidence.getZakazka().values()) {
                if (z.getStav() == Stav.ROZPRACOVANO) {
                    vypisRozpracovanoArea.append(z.getId() + "\n");
                    vypisRozpracovanoArea.append(z.getJmeno() + "\n");
                    vypisRozpracovanoArea.append(z.getPopis() + "\n");
                    vypisRozpracovanoArea.append(z.getCena() + "\n");
                    vypisRozpracovanoArea.append(z.getStav() + "\n");
                    vypisRozpracovanoArea.append(z.getVytvoreni() + "\n");
                    vypisRozpracovanoArea.append(z.getOdevzdani() + "\n");
                }
            }
        });

        JTextArea vypisHotovoArea = new JTextArea(20, 15);
        JScrollPane vypisHotovoAreaScroll = new JScrollPane(vypisHotovoArea);
        vypisHotovoArea.setEditable(false);
        vypisZakazekVJednotlivemStavuPanelHotovo.add(vypisHotovoAreaScroll);
        vypisHotovo.addActionListener(e -> {
            vypisZakazekVJednotlivemStavuCard.show(vypisZakazekVJednotlivemStavuPanelCard, "vypisZakazekVJednotlivemStavuPanelHotovo");
            vypisHotovoArea.setText("");
            for (Zakazka z : evidence.getZakazka().values()) {
                if (z.getStav() == Stav.HOTOVO) {
                    vypisHotovoArea.append(z.getId() + "\n");
                    vypisHotovoArea.append(z.getJmeno() + "\n");
                    vypisHotovoArea.append(z.getPopis() + "\n");
                    vypisHotovoArea.append(z.getCena() + "\n");
                    vypisHotovoArea.append(z.getStav() + "\n");
                    vypisHotovoArea.append(z.getVytvoreni() + "\n");
                    vypisHotovoArea.append(z.getOdevzdani() + "\n");
                }
            }
        });

        JTextArea vypisZaplacenoArea = new JTextArea(20, 15);
        JScrollPane vypisZaplacenoAreaScroll = new JScrollPane(vypisZaplacenoArea);
        vypisZaplacenoArea.setEditable(false);
        vypisZakazekVJednotlivemStavuPanelZaplaceno.add(vypisZaplacenoAreaScroll);
        vypisZaplaceno.addActionListener(e -> {
            vypisZakazekVJednotlivemStavuCard.show(vypisZakazekVJednotlivemStavuPanelCard, "vypisZakazekVJednotlivemStavuPanelZaplaceno");
            vypisZaplacenoArea.setText("");
            for (Zakazka z : evidence.getZakazka().values()) {
                if (z.getStav() == Stav.ZAPLACENO) {
                    vypisZaplacenoArea.append(z.getId() + "\n");
                    vypisZaplacenoArea.append(z.getJmeno() + "\n");
                    vypisZaplacenoArea.append(z.getPopis() + "\n");
                    vypisZaplacenoArea.append(z.getCena() + "\n");
                    vypisZaplacenoArea.append(z.getStav() + "\n");
                    vypisZaplacenoArea.append(z.getVytvoreni() + "\n");
                    vypisZaplacenoArea.append(z.getOdevzdani() + "\n");
                }
            }
        });


        //VYPIS ZAKAZEK PO TERMINU ODEVZDANI

        JTextArea vypisZakazekPoTerminuArea = new JTextArea(20, 15);
        JScrollPane vypisZakazekPoTerminuAreaScroll = new JScrollPane(vypisZakazekPoTerminuArea);
        vypisZakazekPoTerminuArea.setEditable(false);
        vypisZakazekPoTerminuKtereNejsouHotovePanel.add(vypisZakazekPoTerminuAreaScroll, BorderLayout.CENTER);
        vypisZakazekPoTerminuKtereNejsouHotove.addActionListener(e -> {
            stredKarty.show(stred, "vypisZakazekPoTerminuKtereNejsouHotovePanel");
            vypisZakazekPoTerminuArea.setText("");
            for (Zakazka z : evidence.zakazkyPoTerminu()) {
                vypisZakazekPoTerminuArea.append(z.getId() + "\n");
                vypisZakazekPoTerminuArea.append(z.getJmeno() + "\n");
                vypisZakazekPoTerminuArea.append(z.getPopis() + "\n");
                vypisZakazekPoTerminuArea.append(z.getCena() + "\n");
                vypisZakazekPoTerminuArea.append(z.getStav() + "\n");
                vypisZakazekPoTerminuArea.append(z.getVytvoreni() + "\n");
                vypisZakazekPoTerminuArea.append(z.getOdevzdani() + "\n");
            }
        });

        menu.add(upravaZakazek, "upravaZakazek");
        menu.add(vypisInformaci, "vypisInformaci");
        menu.add(vypisZakazek, "vypisZakazek");
        okno.add(menu, BorderLayout.WEST);


        dalsi1.addActionListener(e -> {
            menuKarty.show(menu, "vypisInformaci");
        });
        dalsi2.addActionListener(e -> {
            menuKarty.show(menu, "vypisZakazek");
        });
        dalsi3.addActionListener(e -> {
            menuKarty.show(menu, "upravaZakazek");
        });

        okno.setVisible(true);
    }
}
