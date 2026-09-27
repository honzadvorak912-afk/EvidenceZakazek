package org.example;

import java.util.ArrayList;
import java.util.List;

public class SberacVystup implements Vystup {
    private final List<String> zpravy = new ArrayList<>();

    @Override
    public void zobraz(String zprava) {
        zpravy.add(zprava);
    }

    public List<String> getZpravy() {
        return List.copyOf(zpravy);
    }
}
