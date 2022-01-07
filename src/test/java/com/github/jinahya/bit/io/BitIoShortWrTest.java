package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

class BitIoShortWrTest {

    @Nested
    class Signed {

        @Test
        void wr_MinValue() throws IOException {
            final short expected = Short.MIN_VALUE;
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(Short.SIZE, expected);
                return i -> {
                    return i.readShort(Short.SIZE);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_NegativeOne() throws IOException {
            final short expected = -1;
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(1, expected);
                return i -> {
                    return i.readShort(1);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_Zero() throws IOException {
            final short expected = 0;
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(1, expected);
                return i -> {
                    return i.readShort(1);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_PositiveOne() throws IOException {
            final short expected = 1;
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(2, expected);
                return i -> {
                    return i.readShort(2);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @RepeatedTest(128)
        void wr() throws IOException {
            final int size = current().nextInt(1, Short.SIZE + 1);
            final short expected = (short) (current().nextInt() >> (Integer.SIZE - size));
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(size, expected);
                return i -> {
                    return i.readShort(size);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class Unsigned {

        @RepeatedTest(128)
        void wr() throws IOException {
            final short expected = (short) current().nextInt(0x8000);
            final int size = BitIoUtils.size(expected);
            final boolean unsigned = true;
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(unsigned, size, expected);
                return i -> {
                    return i.readShort(unsigned, size);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class Short16 {

        @Test
        void wr() throws IOException {
            final short expected = (short) current().nextInt(0x10000);
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort16(expected);
                return i -> {
                    return i.readShort16();
                };
            });
            assertThat(actual).isEqualTo(expected);
        }
    }
}
