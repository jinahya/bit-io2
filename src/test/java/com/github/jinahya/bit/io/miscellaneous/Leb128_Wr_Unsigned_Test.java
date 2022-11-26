package com.github.jinahya.bit.io.miscellaneous;

import com.github.jinahya.bit.io.BitIoRandom;
import com.github.jinahya.bit.io.BitIoTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class Leb128_Wr_Unsigned_Test {

    private static int[] unsignedIntArray() {
        return BitIoRandom.nextUnsignedIntArray();
    }

    private static long[] unsignedLongArray() {
        return BitIoRandom.nextUnsignedLongArray();
    }

    @Test
    void wr__0() throws IOException {
        final int actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfUnsigned().writeInt(o, 0);
            return (a, i) -> {
                assertThat(a).hasSize(1).contains(0x00);
                return new Leb128Reader.OfUnsigned().readInt(i);
            };
        });
        assertThat(actual).isZero();
    }

    @Test
    void wr__0L() throws IOException {
        final long actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfUnsigned().writeLong(o, 0L);
            return (a, i) -> {
                assertThat(a).hasSize(1).contains(0x00);
                return new Leb128Reader.OfUnsigned().readLong(i);
            };
        });
        assertThat(actual).isZero();
    }

    @Test
    void wr__0b1001_10000111_01100101() throws IOException {
        final int expected = 0b1001_10000111_01100101;
        final int actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfUnsigned().writeInt(o, expected);
            return (a, i) -> {
                assertThat(a).hasSize(3).contains(0xE5, 0x8E, 0x26);
                return new Leb128Reader.OfUnsigned().readInt(i);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void wr__0b1001_10000111_01100101L() throws IOException {
        final long expected = 0b1001_10000111_01100101L;
        final long actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfUnsigned().writeLong(o, expected);
            return (a, i) -> {
                assertThat(a).hasSize(3).contains(0xE5, 0x8E, 0x26);
                return new Leb128Reader.OfUnsigned().readLong(i);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"unsignedIntArray"})
    @ParameterizedTest
    void wr__Int(final int expected) throws IOException {
        final int actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfUnsigned().writeInt(o, expected);
            return (a, i) -> new Leb128Reader.OfUnsigned().readInt(i);
        });
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"unsignedLongArray"})
    @ParameterizedTest
    void wr__Long(final long expected) throws IOException {
        final long actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfUnsigned().writeLong(o, expected);
            return (a, i) -> new Leb128Reader.OfUnsigned().readLong(i);
        });
        assertThat(actual).isEqualTo(expected);
    }
}
