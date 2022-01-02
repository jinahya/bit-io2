package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class BitIoUtils_Size_Test {

    //    @Disabled
    @Test
    void size_NotNegative_Zero() {
        final int value = 0;
        final int size = BitIoUtils.size(value);
        log.debug("size for {}: {}", value, size);
        assertThat(size).isEqualTo(1);
    }

    @Test
    void size_NotNegative_One() {
        final int value = 1;
        final int size = BitIoUtils.size(value);
        log.debug("size for {}: {}", value, size);
        assertThat(size).isEqualTo(1);
    }

    @Test
    void size_NotNegative_MaxValue() {
        final int value = Integer.MAX_VALUE;
        final int size = BitIoUtils.size(value);
        log.debug("size for {}: {}", value, size);
        assertThat(size).isEqualTo(Integer.SIZE - 1);
    }

    @Test
    void size_NotNegative_Random() {
        final int value = ThreadLocalRandom.current().nextInt() & Integer.MAX_VALUE;
        final int size = BitIoUtils.size(value);
        log.debug("size for {}: {}", value, size);
        assertThat(size).isPositive().isLessThan(Integer.SIZE);
    }
}
