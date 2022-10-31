package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

class BitIoConstantsTest {

    @Test
    void wr__Count() throws IOException {
        final int expected = current().nextInt() & Integer.MAX_VALUE;
        final int actual = wr1u(o -> {
            BitIoConstants.COUNT_WRITER.accept(o, expected);
            return BitIoConstants.COUNT_READER::applyAsInt;
        });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void wr__CountCompressed() throws IOException {
        final int expected = current().nextInt() & Integer.MAX_VALUE;
        final int actual = wr1u(o -> {
            BitIoConstants.COUNT_WRITER_COMPRESSED.accept(o, expected);
            return BitIoConstants.COUNT_READER_COMPRESSED::applyAsInt;
        });
        assertThat(actual).isEqualTo(expected);
    }
}
