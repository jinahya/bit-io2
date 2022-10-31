package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

class BitIoConstantsTest {

    @Test
    void wr__Count() throws IOException {
        final int expected = ThreadLocalRandom.current().nextInt() & Integer.MAX_VALUE;
        final int actual = BitIoTestUtils.wr1u(o -> {
            BitIoConstants.COUNT_WRITER.accept(o, expected);
            return BitIoConstants.COUNT_READER::applyAsInt;
        });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void wr__CountCompressed() throws IOException {
        final int expected = ThreadLocalRandom.current().nextInt() & Integer.MAX_VALUE;
        final int actual = BitIoTestUtils.wr1u(o -> {
            BitIoConstants.COUNT_WRITER_COMPRESSED.accept(o, expected);
            return BitIoConstants.COUNT_READER_COMPRESSED::applyAsInt;
        });
        assertThat(actual).isEqualTo(expected);
    }
}
