package org.example;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

class PosuvneHodiny extends Clock {
    private Instant ted;

    PosuvneHodiny(Instant zacatek) {
        this.ted = zacatek;
    }

    @Override
    public Instant instant() {
        return ted;
    }

    @Override
    public ZoneId getZone() {
        return ZoneId.of("Europe/Prague");
    }

    @Override
    public Clock withZone(ZoneId zona) {
        return this;
    }

    void posunO(Duration o) {
        ted = ted.plus(o);
    }
}