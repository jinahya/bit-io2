package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class MathTest {

    @Disabled
    @Test
    void log() {
        final int bits = (int) Math.ceil(Math.log10(Integer.MAX_VALUE) / Math.log10(2));
        log.debug("bits: {}", bits);
    }
}
