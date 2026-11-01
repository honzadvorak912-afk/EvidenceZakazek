package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DatabazoveUloziste implements Uloz {
    private final String url;
    private final String uzivatel;
    private final String heslo;

    public DatabazoveUloziste(String url, String uzivatel, String heslo) {
        this.url = url;
        this.heslo = heslo;
        this.uzivatel = uzivatel;
    }

    @Override
    public void pridejZakaznika(Zakaznik zakaznik) {
        String sql = "INSERT INTO zakaznici (jmeno, telefon, id) VALUES (?, ?, ?)";
        try (Connection spojeni = DriverManager.getConnection(url, uzivatel, heslo);
             PreparedStatement prikaz = spojeni.prepareStatement(sql)) {
            prikaz.setString(1, zakaznik.getJmeno());
            prikaz.setString(2, zakaznik.getTelefon());
            prikaz.setInt(3, zakaznik.getId());
            prikaz.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nepodařilo se přidat zákazníka " + zakaznik.getId(), e);
        }
    }

    @Override
    public void ulozZakazku(Zakazka zakazka) {
        String sql = """
                INSERT INTO zakazky (id, zakaznik_id, popis, cena, stav, zadani, odevzdani)
                VALUES (?,?,?,?,?,?,?) AS novy
                ON DUPLICATE KEY UPDATE
                    zakaznik_id = novy.zakaznik_id,
                    popis = novy.popis,
                    cena = novy.cena,
                    stav = novy.stav,
                    zadani = novy.zadani,
                    odevzdani = novy.odevzdani
                """;
        try (Connection spojeni = DriverManager.getConnection(url, uzivatel, heslo);
             PreparedStatement prikaz = spojeni.prepareStatement(sql)) {
            prikaz.setInt(1, zakazka.getId());
            prikaz.setInt(2, zakazka.getZakaznikId());
            prikaz.setString(3, zakazka.getPopis());
            prikaz.setInt(4, zakazka.getCena());
            prikaz.setString(5, zakazka.getStav().name());
            prikaz.setObject(6, zakazka.getVytvoreni());
            prikaz.setObject(7, zakazka.getOdevzdani());
            prikaz.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nepodařilo se uložit zakázku " + zakazka.getId(), e);
        }
    }

    @Override
    public Map<Integer, Zakaznik> nactiZakazniky() {
        Map<Integer, Zakaznik> zakaznikMap = new HashMap<>();
        String sql = "SELECT jmeno, telefon, id FROM zakaznici";
        try (Connection spojeni = DriverManager.getConnection(url, uzivatel, heslo);
             PreparedStatement prikaz = spojeni.prepareStatement(sql);
             ResultSet vysledek = prikaz.executeQuery()) {
            while (vysledek.next()) {
                int id = vysledek.getInt("id");
                String jmeno = vysledek.getString("jmeno");
                String telefon = vysledek.getString("telefon");
                zakaznikMap.put(id, new Zakaznik(jmeno, telefon, id));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Nepodařilo se načíst zákazníky", e);
        }
        return zakaznikMap;
    }

    @Override
    public Map<Integer, Zakazka> nactiZakazky(Map<Integer, Zakaznik> zakaznikMap) {
        Map<Integer, Zakazka> zakazkaMap = new HashMap<>();
        String sql = "SELECT id, zakaznik_id, popis, cena, stav, zadani, odevzdani FROM zakazky";
        try (Connection spojeni = DriverManager.getConnection(url, uzivatel, heslo);
             PreparedStatement prikaz = spojeni.prepareStatement(sql);
             ResultSet vysledek = prikaz.executeQuery()) {
            while (vysledek.next()) {
                int id = vysledek.getInt("id");
                int zakaznik_id = vysledek.getInt("zakaznik_id");
                String popis = vysledek.getString("popis");
                int cena = vysledek.getInt("cena");
                Stav stav = Stav.valueOf(vysledek.getString("stav"));
                LocalDate zadani = vysledek.getObject("zadani", LocalDate.class);
                LocalDate odevzdani = vysledek.getObject("odevzdani", LocalDate.class);
                Zakaznik zakaznik = zakaznikMap.get(zakaznik_id);
                if (zakaznik == null) {
                    throw new IllegalStateException("Zakázka " + id + " odkazuje na zákazníka " + zakaznik_id + ", který neexistuje.");
                }
                zakazkaMap.put(id, new Zakazka(id, zakaznik, popis, cena, stav, zadani, odevzdani));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Nepodařilo se načíst zakázky", e);
        }
        return zakazkaMap;
    }

    @Override
    public void odeberZakaznika(int id) {
        String sql = "DELETE FROM zakaznici WHERE id = ?";
        try (Connection spojeni = DriverManager.getConnection(url, uzivatel, heslo);
             PreparedStatement prikaz = spojeni.prepareStatement(sql)) {
            prikaz.setInt(1, id);
            prikaz.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nepodařilo se smazat zákazníka " + id, e);
        }
    }

    @Override
    public void odeberZakazku(int id) {
        String sql = "DELETE FROM zakazky WHERE id = ?";
        try (Connection spojeni = DriverManager.getConnection(url, uzivatel, heslo);
             PreparedStatement prikaz = spojeni.prepareStatement(sql)) {
            prikaz.setInt(1, id);
            prikaz.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Nepodařilo se smazat zakázku " + id, e);
        }
    }
}