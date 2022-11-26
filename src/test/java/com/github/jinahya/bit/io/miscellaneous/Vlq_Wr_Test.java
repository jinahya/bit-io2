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

class Vlq_Wr_Test {

    private static int[] intArray() {
        return BitIoRandom.nextUnsignedIntArray();
    }

    private static long[] longArray() {
        return BitIoRandom.nextUnsignedLongArray();
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
    void vlq__106903() throws IOException {
        final int expected = 106903;
        final int actual = BitIoTestUtils.wr1au(o -> {
            new VlqWriter().writeInt(o, expected);
            return (a, i) -> {
                assertThat(a).contains(134, 195, 23);
                return new VlqReader().readInt(i);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void vlqLong__106903() throws IOException {
        final long expected = 106903L;
        final long actual = BitIoTestUtils.wr1au(o -> {
            new VlqWriter().writeLong(o, expected);
            return (a, i) -> {
                assertThat(a).contains(134, 195, 23);
                return new VlqReader().readLong(i);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"intArray"})
    @ParameterizedTest
    void wr__Int(final int expected) throws IOException {
        final int actual = BitIoTestUtils.wr1au(o -> {
            new VlqWriter().writeInt(o, expected);
            return (a, i) -> new VlqReader().readInt(i);
        });
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"longArray"})
    @ParameterizedTest
    void wr__Long(final long expected) throws IOException {
        final long actual = BitIoTestUtils.wr1au(o -> {
            new VlqWriter().writeLong(o, expected);
            return (a, i) -> new VlqReader().readLong(i);
        });
        assertThat(actual).isEqualTo(expected);
    }
}
