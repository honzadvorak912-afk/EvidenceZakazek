package org.example;

public class Zakaznik {
    private String jmeno;
    private String telefon;
    private final int id;

    public int getId() {
        return id;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setJmeno(String jmeno) {
        if (jmeno.isBlank() || jmeno.contains(";")) {
            throw new IllegalArgumentException("Jméno klienta nesmí být prázdné a nesmí obsahovat ;.");
        } else {
            this.jmeno = jmeno;
        }
    }

    public void setTelefon(String telefon) {
        if (telefon.isBlank() || telefon.contains(";")) {
            throw new IllegalArgumentException("Telefon klienta nesmí být prázdný a nesmí obsahovat ;.");
        } else {
            this.telefon = telefon;
        }
    }

    public Zakaznik(String jmeno, String telefon, int id) {
        setJmeno(jmeno);
        setTelefon(telefon);
        this.id = id;
    }
}
