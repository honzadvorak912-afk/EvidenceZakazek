package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Okenní rozhraní k evidenci zakázek.
 * <p>
 * S daty nepracuje přímo – všechno dělá přes {@link Evidence} a {@link Uloz},
 * stejně jako konzolová {@link Main}. Tahle třída jen zobrazuje a sbírá vstup.
 */
public class OknoEvidence {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final String[] FILTRY = {
            "Všechny zakázky", "Poptávka", "Rozpracováno", "Hotovo", "Zaplaceno", "Po termínu"
    };

    private final Evidence evidence;
    private final Uloz uloz;
    private final Vystup vystup = new OknovyVystup();

    private final JFrame okno = new JFrame("Evidence zakázek");

    private final DefaultTableModel modelZakazek = new NeupravitelnyModel(
            new String[]{"Číslo", "Zákazník", "Popis", "Cena", "Stav", "Zadáno", "Odevzdat do"});
    private final JTable tabulkaZakazek = new JTable(modelZakazek);
    private final JComboBox<String> filtr = new JComboBox<>(FILTRY);

    private final DefaultTableModel modelZakazniku = new NeupravitelnyModel(
            new String[]{"Číslo", "Jméno", "Telefon", "Zakázek"});
    private final JTable tabulkaZakazniku = new JTable(modelZakazniku);

    private final JLabel prehledStavy = new JLabel();
    private final JLabel prehledHotove = new JLabel();
    private final JLabel prehledZaplacene = new JLabel();
    private final JLabel prehledPoTerminu = new JLabel();

    public OknoEvidence(Evidence evidence, Uloz uloz) {
        this.evidence = evidence;
        this.uloz = uloz;
        postavOkno();
        obnovVse();
    }

    // ------------------------------------------------------------------ start

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            nastavVzhled();

            Uloz uloz = new SouboroveUloziste(Path.of("zakazky.csv"), Path.of("zakaznici.csv"));

            Evidence evidence;
            try {
                evidence = new Evidence(uloz, Clock.systemDefaultZone());
            } catch (IllegalStateException | IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),
                        "Data se nepodařilo načíst", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new OknoEvidence(evidence, uloz).zobraz();
        });
    }

    private static void nastavVzhled() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // vzhled není podstatný – když se nepovede, poběží výchozí
        }
    }

    public void zobraz() {
        okno.setVisible(true);
    }

    // ----------------------------------------------------------------- skládání

    private void postavOkno() {
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setMinimumSize(new Dimension(900, 520));
        okno.setLocationByPlatform(true);

        JTabbedPane zalozky = new JTabbedPane();
        zalozky.addTab("Zakázky", panelZakazek());
        zalozky.addTab("Zákazníci", panelZakazniku());
        zalozky.addTab("Přehled", panelPrehledu());
        zalozky.addChangeListener(e -> obnovVse());

        okno.setContentPane(zalozky);
        okno.pack();
    }

    private JPanel panelZakazek() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel horni = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        horni.add(new JLabel("Zobrazit:"));
        horni.add(filtr);
        filtr.addActionListener(e -> obnovZakazky());
        panel.add(horni, BorderLayout.NORTH);

        pripravTabulku(tabulkaZakazek, new int[]{60, 160, 260, 80, 120, 100, 110});
        panel.add(new JScrollPane(tabulkaZakazek), BorderLayout.CENTER);

        JButton nova = new JButton("Nová zakázka");
        JButton posun = new JButton("Posunout stav");
        JButton zrus = new JButton("Zrušit zakázku");
        nova.addActionListener(e -> novaZakazka());
        posun.addActionListener(e -> posunStav());
        zrus.addActionListener(e -> zrusZakazku());
        panel.add(radekTlacitek(nova, posun, zrus), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel panelZakazniku() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        pripravTabulku(tabulkaZakazniku, new int[]{60, 260, 200, 90});
        panel.add(new JScrollPane(tabulkaZakazniku), BorderLayout.CENTER);

        JButton novy = new JButton("Nový zákazník");
        JButton zrus = new JButton("Zrušit zákazníka");
        novy.addActionListener(e -> novyZakaznik());
        zrus.addActionListener(e -> zrusZakaznika());
        panel.add(radekTlacitek(novy, zrus), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel panelPrehledu() {
        JPanel obsah = new JPanel();
        obsah.setLayout(new BoxLayout(obsah, BoxLayout.Y_AXIS));
        obsah.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel titulek = new JLabel("Souhrn");
        titulek.setFont(titulek.getFont().deriveFont(Font.BOLD, 15f));

        obsah.add(zarovnane(titulek));
        obsah.add(Box.createVerticalStrut(16));
        obsah.add(zarovnane(prehledStavy));
        obsah.add(Box.createVerticalStrut(16));
        obsah.add(zarovnane(prehledHotove));
        obsah.add(Box.createVerticalStrut(6));
        obsah.add(zarovnane(prehledZaplacene));
        obsah.add(Box.createVerticalStrut(6));
        obsah.add(zarovnane(prehledPoTerminu));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(obsah, BorderLayout.NORTH);
        return panel;
    }

    private JComponent zarovnane(JComponent komponenta) {
        komponenta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return komponenta;
    }

    private JPanel radekTlacitek(JButton... tlacitka) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        for (JButton t : tlacitka) {
            panel.add(t);
        }
        return panel;
    }

    private void pripravTabulku(JTable tabulka, int[] sirky) {
        tabulka.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabulka.setRowHeight(24);
        tabulka.setAutoCreateRowSorter(true);
        tabulka.getTableHeader().setReorderingAllowed(false);
        for (int i = 0; i < sirky.length; i++) {
            tabulka.getColumnModel().getColumn(i).setPreferredWidth(sirky[i]);
        }
    }

    // --------------------------------------------------------------- obnovování

    private void obnovVse() {
        obnovZakazky();
        obnovZakazniky();
        obnovPrehled();
    }

    private void obnovZakazky() {
        modelZakazek.setRowCount(0);
        for (Zakazka z : vybraneZakazky()) {
            modelZakazek.addRow(new Object[]{
                    z.getId(), z.getJmeno(), z.getPopis(), z.getCena() + " Kč",
                    z.getStav(), z.getVytvoreni().format(FORMAT), z.getOdevzdani().format(FORMAT)
            });
        }
    }

    private List<Zakazka> vybraneZakazky() {
        String volba = (String) filtr.getSelectedItem();

        List<Zakazka> vysledek = new ArrayList<>();
        if ("Po termínu".equals(volba)) {
            vysledek.addAll(evidence.zakazkyPoTerminu());
        } else {
            Stav hledany = stavPodleVolby(volba);
            for (Zakazka z : evidence.getZakazka().values()) {
                if (hledany == null || z.getStav() == hledany) {
                    vysledek.add(z);
                }
            }
        }
        vysledek.sort(Comparator.comparingInt(Zakazka::getId));
        return vysledek;
    }

    private Stav stavPodleVolby(String volba) {
        if ("Poptávka".equals(volba)) return Stav.POPTAVKA;
        if ("Rozpracováno".equals(volba)) return Stav.ROZPRACOVANO;
        if ("Hotovo".equals(volba)) return Stav.HOTOVO;
        if ("Zaplaceno".equals(volba)) return Stav.ZAPLACENO;
        return null;
    }

    private void obnovZakazniky() {
        modelZakazniku.setRowCount(0);
        for (Zakaznik z : serazeniZakaznici()) {
            modelZakazniku.addRow(new Object[]{
                    z.getId(), z.getJmeno(), z.getTelefon(), pocetZakazek(z.getId())
            });
        }
    }

    private List<Zakaznik> serazeniZakaznici() {
        List<Zakaznik> seznam = new ArrayList<>(evidence.getZakaznik().values());
        seznam.sort(Comparator.comparingInt(Zakaznik::getId));
        return seznam;
    }

    private int pocetZakazek(int idZakaznika) {
        int pocet = 0;
        for (Zakazka z : evidence.getZakazka().values()) {
            if (z.getZakaznikId() == idZakaznika) {
                pocet++;
            }
        }
        return pocet;
    }

    private void obnovPrehled() {
        Map<Stav, Integer> pocty = evidence.pocetZakazekVJednotlivemStavu();

        StringBuilder sb = new StringBuilder("<html>");
        for (Stav s : Stav.values()) {
            sb.append(s).append(": ").append(pocty.getOrDefault(s, 0)).append("<br>");
        }
        sb.append("</html>");

        prehledStavy.setText(sb.toString());
        prehledHotove.setText("Součet cen hotových, ale nezaplacených zakázek: "
                + evidence.soucetCenHotovychZakazek() + " Kč");
        prehledZaplacene.setText("Součet cen zaplacených zakázek: "
                + evidence.soucetCenZaplacenychZakazek() + " Kč");
        prehledPoTerminu.setText("Zakázek po termínu odevzdání: "
                + evidence.soucetZakazekPoTerminu());
    }

    // ------------------------------------------------------------------- akce

    private void novaZakazka() {
        List<Zakaznik> zakaznici = serazeniZakaznici();
        if (zakaznici.isEmpty()) {
            vystup.zobraz("Nejdřív přidej aspoň jednoho zákazníka.");
            return;
        }

        JComboBox<Zakaznik> vyberZakaznika = new JComboBox<>(zakaznici.toArray(new Zakaznik[0]));
        vyberZakaznika.setRenderer(new ZakaznikRenderer());
        JTextField popis = new JTextField(18);
        JTextField cena = new JTextField(18);
        JTextField datum = new JTextField(18);

        JPanel formular = formular(
                "Zákazník", vyberZakaznika,
                "Popis", popis,
                "Cena (Kč)", cena,
                "Odevzdat do (dd.MM.yyyy)", datum);

        if (!potvrzeno(formular, "Nová zakázka")) {
            return;
        }

        try {
            Zakaznik zakaznik = (Zakaznik) vyberZakaznika.getSelectedItem();
            int castka = Integer.parseInt(cena.getText().trim());
            LocalDate odevzdani = LocalDate.parse(datum.getText().trim(), FORMAT);

            int cislo = evidence.pridatZakazku(zakaznik.getId(), popis.getText().trim(), castka, odevzdani);
            uloz.ulozZakazku(evidence.getZakazka().get(cislo));

            obnovVse();
            vystup.zobraz("Zakázka byla přidána pod číslem " + cislo + ".");
        } catch (NumberFormatException e) {
            vystup.zobraz("Cena musí být číslo.");
        } catch (DateTimeParseException e) {
            vystup.zobraz("Datum musí být ve tvaru dd.MM.yyyy.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            vystup.zobraz(e.getMessage());
        }
    }

    private void posunStav() {
        Integer cislo = vybraneCislo(tabulkaZakazek, "Vyber v tabulce zakázku, které chceš posunout stav.");
        if (cislo == null) {
            return;
        }
        try {
            ZakazkaService.posunStav(vystup, evidence, cislo, uloz);
            obnovVse();
        } catch (IllegalArgumentException | IllegalStateException e) {
            vystup.zobraz(e.getMessage());
        }
    }

    private void zrusZakazku() {
        Integer cislo = vybraneCislo(tabulkaZakazek, "Vyber v tabulce zakázku, kterou chceš zrušit.");
        if (cislo == null) {
            return;
        }
        if (!potvrzeniAno("Opravdu zrušit zakázku číslo " + cislo + "?")) {
            return;
        }
        try {
            ZakazkaService.zrusZakazku(vystup, evidence, cislo, uloz);
            obnovVse();
        } catch (IllegalArgumentException | IllegalStateException e) {
            vystup.zobraz(e.getMessage());
        }
    }

    private void novyZakaznik() {
        JTextField jmeno = new JTextField(18);
        JTextField telefon = new JTextField(18);

        JPanel formular = formular("Jméno", jmeno, "Telefon", telefon);
        if (!potvrzeno(formular, "Nový zákazník")) {
            return;
        }

        try {
            int cislo = evidence.pridatZakaznika(jmeno.getText().trim(), telefon.getText().trim());
            uloz.pridejZakaznika(evidence.getZakaznik().get(cislo));

            obnovVse();
            vystup.zobraz("Zákazník byl přidán pod číslem " + cislo + ".");
        } catch (IllegalArgumentException | IllegalStateException e) {
            vystup.zobraz(e.getMessage());
        }
    }

    private void zrusZakaznika() {
        Integer cislo = vybraneCislo(tabulkaZakazniku, "Vyber v tabulce zákazníka, kterého chceš zrušit.");
        if (cislo == null) {
            return;
        }
        if (!potvrzeniAno("Opravdu zrušit zákazníka číslo " + cislo + "?")) {
            return;
        }
        try {
            if (evidence.zrusZakaznika(cislo)) {
                uloz.odeberZakaznika(cislo);
                obnovVse();
                vystup.zobraz("Zákazník byl zrušen.");
            } else {
                vystup.zobraz("Zákazníka nelze zrušit – buď neexistuje, nebo má ještě zakázku.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            vystup.zobraz(e.getMessage());
        }
    }

    // ----------------------------------------------------------------- pomocné

    /** Vrátí číslo z prvního sloupce vybraného řádku, nebo null (a upozorní), když nic vybrané není. */
    private Integer vybraneCislo(JTable tabulka, String kdyzNicNeniVybrano) {
        int radek = tabulka.getSelectedRow();
        if (radek < 0) {
            vystup.zobraz(kdyzNicNeniVybrano);
            return null;
        }
        return (Integer) tabulka.getValueAt(radek, 0);
    }

    /** Postaví formulář ze střídajících se dvojic popisek – pole. */
    private JPanel formular(Object... popiskyAPole) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < popiskyAPole.length; i += 2) {
            c.gridx = 0;
            c.gridy = i / 2;
            panel.add(new JLabel((String) popiskyAPole[i] + ":"), c);
            c.gridx = 1;
            panel.add((Component) popiskyAPole[i + 1], c);
        }
        return panel;
    }

    private boolean potvrzeno(JPanel formular, String titulek) {
        int volba = JOptionPane.showConfirmDialog(okno, formular, titulek,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        return volba == JOptionPane.OK_OPTION;
    }

    private boolean potvrzeniAno(String otazka) {
        int volba = JOptionPane.showConfirmDialog(okno, otazka, "Potvrzení",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return volba == JOptionPane.YES_OPTION;
    }

    /** Tabulka, do které uživatel nemůže psát – data se mění jen přes evidenci. */
    private static class NeupravitelnyModel extends DefaultTableModel {
        NeupravitelnyModel(String[] sloupce) {
            super(sloupce, 0);
        }

        @Override
        public boolean isCellEditable(int radek, int sloupec) {
            return false;
        }

        /** Bez tohohle by se sloupec s čísly řadil jako text: 1, 10, 2, 3. */
        @Override
        public Class<?> getColumnClass(int sloupec) {
            Object hodnota = getRowCount() > 0 ? getValueAt(0, sloupec) : null;
            return hodnota == null ? Object.class : hodnota.getClass();
        }
    }

    /** Aby se v rozbalovacím seznamu ukazovalo jméno zákazníka, a ne org.example.Zakaznik@1a2b3c. */
    private static class ZakaznikRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> seznam, Object hodnota,
                                                      int index, boolean vybrano, boolean fokus) {
            super.getListCellRendererComponent(seznam, hodnota, index, vybrano, fokus);
            if (hodnota instanceof Zakaznik z) {
                setText(z.getId() + " – " + z.getJmeno());
            }
            return this;
        }
    }
}
