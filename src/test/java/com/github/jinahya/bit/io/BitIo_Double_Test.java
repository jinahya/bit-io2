package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 Jinahya, Inc.
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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.applyRandomSizeAndValueForLongUnchecked;
import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class BitIo_Double_Test {

    private static LongStream signMaskStream() {
        return LongStream.of(
                Long.MIN_VALUE,
                -1L,
                0L,
                1L,
                Long.MAX_VALUE,
                ThreadLocalRandom.current().nextLong() >>> 1, // random positive
                ThreadLocalRandom.current().nextLong() | Long.MIN_VALUE // random negative
        );
    }

    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void test(final boolean unsigned) {
        applyRandomSizeAndValueForLongUnchecked(
                unsigned,
                s -> v -> wr1u(o -> {
                    o.writeLong(unsigned, s, v);
                    return i -> {
                        final var actual = i.readLong(unsigned, s);
                        assertThat(actual).isEqualTo(v);
                        return null;
                    };
                })
        );
    }

    private static Stream<Arguments> getExponentSizeAndSignificandSizeArgumentsStream() {
        return IntStream.range(0, 16)
                .mapToObj(i -> Arguments.of(BitIoRandom.nextExponentSizeForDouble(),
                                            BitIoRandom.nextSignificandSizeForDouble()))
                ;
    }

    @MethodSource({"getExponentSizeAndSignificandSizeArgumentsStream"})
    @ParameterizedTest
    void wr__(final int exponentSize, final int significandSize) throws IOException {
        final var expected = BitIoRandom.nextValueForDouble(exponentSize, significandSize);
        final var actual = wr1u(o -> {
            o.writeDouble(exponentSize, significandSize, expected);
            return i -> i.readDouble(exponentSize, significandSize);
        });
        if (Double.isNaN(expected)) {
            assertThat(actual).isNaN();
            return;
        }
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"signMaskStream"})
    @ParameterizedTest
    void doubleOfZero__(final long signMask) throws IOException {
        // https://github.com/assertj/assertj/issues/919
        // var 를 사용하면, assertThat(double) 이 아닌, assertThat(Double) 을 사용한다.
        final /*var*/ double actual = wr1u(o -> {
            o.writeDoubleOfZero(signMask);
            return BitInput::readDoubleOfZero;
        });
        assertThat(actual).isZero();
        final var bits = Double.doubleToRawLongBits(actual);
        if (signMask >= 0) {
            assertThat(actual).isEqualTo(+.0d);
            assertThat(bits).isEqualTo(DoubleTestConstants.POSITIVE_ZERO_BITS);
        } else {
            assertThat(actual).isEqualTo(-.0d);
            assertThat(bits).isEqualTo(DoubleTestConstants.NEGATIVE_ZERO_BITS);
        }
    }

    @MethodSource({"signMaskStream"})
    @ParameterizedTest
    void doubleOfInfinity__(final long signMask) throws IOException {
        final double actual = wr1u(o -> {
            o.writeDoubleOfInfinity(signMask);
            return BitInput::readDoubleOfInfinity;
        });
        assertThat(actual).isInfinite();
        final var bits = Double.doubleToRawLongBits(actual);
        if (signMask >= 0) {
            assertThat(actual).isEqualTo(Double.POSITIVE_INFINITY);
            assertThat(bits).isEqualTo(DoubleTestConstants.POSITIVE_INFINITY_BITS);
        } else {
            assertThat(actual).isEqualTo(Double.NEGATIVE_INFINITY);
            assertThat(bits).isEqualTo(DoubleTestConstants.NEGATIVE_INFINITY_BITS);
        }
    }
}