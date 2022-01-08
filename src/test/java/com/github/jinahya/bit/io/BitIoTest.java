package com.github.jinahya.bit.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

class BitIoTest {

    @DisplayName("skip")
    @Nested
    class SkipTest {

        @Test
        void skip__() throws IOException {
            BitIoTestUtils.wr2v(o -> {
                final int bits = current().nextInt(1, 128);
                o.skip(bits);
                return i -> {
                    i.skip(bits);
                };
            });
        }
    }

    @DisplayName("align")
    @Nested
    class AlignTest {

        @RepeatedTest(16)
        void align__single() throws IOException {
            BitIoTestUtils.wr2v(o -> {
                final int bits = current().nextInt(1, 128);
                final boolean skip = current().nextBoolean();
                if (skip) {
                    o.skip(bits);
                }
                final long padded = o.align();
                return i -> {
                    if (skip) {
                        i.skip(bits);
                    }
                    final long discarded = i.align();
                    assertThat(discarded).isEqualTo(padded);
                };
            });
        }

        @RepeatedTest(16)
        void align__() throws IOException {
            BitIoTestUtils.wr2v(o -> {
                final int bits = current().nextInt(1, 128);
                final boolean skip = current().nextBoolean();
                if (skip) {
                    o.skip(bits);
                }
                final int bytes = current().nextInt(1, 16);
                final long padded = o.align(bytes);
                return i -> {
                    if (skip) {
                        i.skip(bits);
                    }
                    final long discarded = i.align(bytes);
                    assertThat(discarded).isEqualTo(padded);
                };
            });
        }
    }
}
