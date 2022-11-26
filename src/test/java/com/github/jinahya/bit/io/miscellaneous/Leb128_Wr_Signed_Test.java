package com.github.jinahya.bit.io.miscellaneous;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.jinahya.bit.io.BitIoRandom;
import com.github.jinahya.bit.io.BitIoTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class Leb128_Wr_Signed_Test {

    private static int[] intArray() {
        return BitIoRandom.nextSignedIntArray();
    }

    private static long[] longArray() {
        return BitIoRandom.nextSignedLongArray();
    }

    @Test
    void wr__0() throws IOException {
        final int actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfSigned().writeInt(o, 0);
            return (a, i) -> {
                assertThat(a).hasSize(1).contains(0x00);
                return new Leb128Reader.OfSigned().readInt(i);
            };
        });
        assertThat(actual).isZero();
    }

    @Test
    void wr__0L() throws IOException {
        final long actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfSigned().writeLong(o, 0L);
            return (a, i) -> {
                assertThat(a).hasSize(1).contains(0x00);
                return new Leb128Reader.OfSigned().readLong(i);
            };
        });
        assertThat(actual).isZero();
    }

    @Test
    void wr__M1() throws IOException {
        final int actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfSigned().writeInt(o, -1);
            return (a, i) -> {
//                assertThat(a).hasSize(1).contains(0x00);
                return new Leb128Reader.OfSigned().readInt(i);
            };
        });
        assertThat(actual).isEqualTo(-1);
    }

    @Test
    void wr__M1L() throws IOException {
        final long actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfSigned().writeLong(o, -1L);
            return (a, i) -> {
//                assertThat(a).hasSize(1).contains(0x00);
                return new Leb128Reader.OfSigned().readLong(i);
            };
        });
        assertThat(actual).isEqualTo(-1L);
    }

    @Test
    void wr__1_11100010_01000000() throws IOException {
        final int expected = 0b1_11100010_01000000;
        final int actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfSigned().writeInt(o, expected);
            return (a, i) -> {
//                assertThat(a).hasSize(3).contains(0xE5, 0x8E, 0x26);
                return new Leb128Reader.OfSigned().readInt(i);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void wr__1_11100010_01000000L() throws IOException {
        final long expected = 0b1_11100010_01000000L;
        final long actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfSigned().writeLong(o, expected);
            return (a, i) -> {
//                assertThat(a).hasSize(3).contains(0xE5, 0x8E, 0x26);
                return new Leb128Reader.OfSigned().readLong(i);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"intArray"})
    @ParameterizedTest
    void wr__Int(final int expected) throws IOException {
        final int actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfSigned().writeInt(o, expected);
            return (a, i) -> new Leb128Reader.OfSigned().readInt(i);
        });
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"longArray"})
    @ParameterizedTest
    void wr__Long(final long expected) throws IOException {
        final long actual = BitIoTestUtils.wr1au(o -> {
            new Leb128Writer.OfSigned().writeLong(o, expected);
            return (a, i) -> new Leb128Reader.OfSigned().readLong(i);
        });
        assertThat(actual).isEqualTo(expected);
    }
}
