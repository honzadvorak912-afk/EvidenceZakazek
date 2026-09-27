package org.example;

public class KonzolovyVystup implements Vystup {
    @Override
    public void zobraz(String zprava) {
        IO.println(zprava);
    }
}
