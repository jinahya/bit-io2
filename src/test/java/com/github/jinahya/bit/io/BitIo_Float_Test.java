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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.applyRandomSizeAndValueForLongUnchecked;
import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class BitIo_Float_Test {

    private static IntStream signMaskStream() {
        return IntStream.of(
                Integer.MIN_VALUE,
                -1,
                0,
                +1,
                Integer.MAX_VALUE,
                ThreadLocalRandom.current().nextInt() >>> 1, // random positive
                ThreadLocalRandom.current().nextInt() | Integer.MIN_VALUE // random negative
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
                .mapToObj(i -> Arguments.of(BitIoRandom.nextExponentSizeForFloat(),
                                            BitIoRandom.nextSignificandSizeForFloat()))
                ;
    }

    @MethodSource({"getExponentSizeAndSignificandSizeArgumentsStream"})
    @ParameterizedTest
    void wr__(final int exponentSize, final int significandSize) throws IOException {
        final var expected = BitIoRandom.nextValueForFloat(exponentSize, significandSize);
        final var actual = wr1u(o -> {
            o.writeFloat(exponentSize, significandSize, expected);
            return i -> i.readFloat(exponentSize, significandSize);
        });
        if (Float.isNaN(expected)) {
            assertThat(actual).isNaN();
            return;
        }
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"signMaskStream"})
    @ParameterizedTest
    void ofZero__(final int signMask) throws IOException {
        // https://github.com/assertj/assertj/issues/919
        // var 를 사용하면, assertThat(float) 이 아닌, assertThat(Float) 을 사용한다.
        final /*var*/ float actual = wr1u(o -> {
            o.writeFloatOfZero(signMask);
            return BitInput::readFloatOfZero;
        });
        assertThat(actual).isZero();
        final var bits = Float.floatToRawIntBits(actual);
        if (signMask >= 0) {
            assertThat(actual).isEqualTo(+.0f);
            assertThat(bits).isEqualTo(FloatTestConstants.POSITIVE_ZERO_BITS);
        } else {
            assertThat(actual).isEqualTo(-.0f);
            assertThat(bits).isEqualTo(FloatTestConstants.NEGATIVE_ZERO_BITS);
        }
    }

    @MethodSource({"signMaskStream"})
    @ParameterizedTest
    void ofInfinity__(final int signMask) throws IOException {
        final var actual = wr1u(o -> {
            o.writeFloatOfInfinity(signMask);
            return BitInput::readFloatOfInfinity;
        });
        assertThat(actual).isInfinite();
        final var bits = Float.floatToRawIntBits(actual);
        if (signMask >= 0) {
            assertThat(actual).isEqualTo(Float.POSITIVE_INFINITY);
            assertThat(bits).isEqualTo(FloatTestConstants.POSITIVE_INFINITY_BITS);
        } else {
            assertThat(actual).isEqualTo(Float.NEGATIVE_INFINITY);
            assertThat(bits).isEqualTo(FloatTestConstants.NEGATIVE_INFINITY_BITS);
        }
    }

    @Nested
    class OfNaNTest {

        private static Stream<Arguments> getSignificandSizeAndValueArgumentsStream() {
            return IntStream.range(0, 8)
                    .mapToObj(i -> {
                        final int size = BitIoRandom.nextSignificandSizeForFloat();
                        final int bits = BitIoRandom.nextSignificandBitsForFloatNaN(size);
                        return Arguments.of(size, bits);
                    });
        }

        @MethodSource({"getSignificandSizeAndValueArgumentsStream"})
        @ParameterizedTest(name = "[{index}] size: {0}, bits: {1}")
        void ofNaN__(final int size, final int bits) throws IOException {
            final var actual = wr1u(o -> {
                o.writeFloatOfNaN(size, bits);
                return i -> i.readFloatOfNaN(size);
            });
            assertThat(actual).isNaN();
        }
    }
}
