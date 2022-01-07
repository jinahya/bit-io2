package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

class BitIoByteWrTest {

    @Nested
    class Signed {

        @Test
        void wr_MinValue() throws IOException {
            final byte expected = Byte.MIN_VALUE;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(Byte.SIZE, expected);
                return i -> {
                    return i.readByte(Byte.SIZE);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_NegativeOne() throws IOException {
            final byte expected = -1;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(1, expected);
                return i -> {
                    return i.readByte(1);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_Zero() throws IOException {
            final byte expected = 0;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(1, expected);
                return i -> {
                    return i.readByte(1);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_PositiveOne() throws IOException {
            final byte expected = 1;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(2, expected);
                return i -> {
                    return i.readByte(2);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_MaxValue() throws IOException {
            final byte expected = Byte.MAX_VALUE;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(Byte.SIZE, expected);
                return i -> {
                    return i.readByte(Byte.SIZE);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @RepeatedTest(128)
        void wr() throws IOException {
            final int size = current().nextInt(1, Byte.SIZE + 1);
            final byte expected = (byte) (current().nextInt() >> (Integer.SIZE - size));
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(size, expected);
                return i -> {
                    return i.readByte(size);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class Unsigned {

        @RepeatedTest(128)
        void wr() throws IOException {
            final byte expected = (byte) current().nextInt(0x80);
            final int size = BitIoUtils.size(expected);
            final boolean unsigned = true;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(unsigned, size, expected);
                return i -> {
                    return i.readByte(unsigned, size);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class Byte8 {

        @RepeatedTest(128)
        void wr() throws IOException {
            final byte expected = (byte) current().nextInt(0x100);
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte8(expected);
                return i -> {
                    return i.readByte8();
                };
            });
            assertThat(actual).isEqualTo(expected);
        }
    }
}
