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
import org.junit.jupiter.api.RepeatedTest;
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
    void fixed__(final int exponentSize, final int significandSize) throws IOException {
        log.debug("exponentSize: {}, significandSize: {}", exponentSize, significandSize);
        final var expected = BitIoRandom.nextValueForFloat(exponentSize, significandSize);
        log.debug("expected: {}", BitIoTestUtils.format(expected));
        final var actual = wr1u(o -> {
            o.writeFloat(exponentSize, significandSize, expected);
            return i -> i.readFloat(exponentSize, significandSize);
        });
        log.debug("actual  : {}", BitIoTestUtils.format(actual));
        if (Float.isNaN(expected)) {
            assertThat(actual).isNaN();
            return;
        }
        assertThat(actual).isEqualTo(expected);
    }

    @RepeatedTest(128)
    void floatOfZero__() throws IOException {
        final var positive = ThreadLocalRandom.current().nextBoolean();
        final var actual = wr1u(o -> {
            o.writeFloatOfZero(positive);
            return BitInput::readFloatOfZero;
        });
        // https://github.com/assertj/assertj/issues/919
//        assertThat(actual).isZero();
        if (positive) {
            assertThat(Float.floatToRawIntBits(actual))
                    .isEqualTo(0b0__00000000__00000000_00000000_0000000);
        } else {
            assertThat(Float.floatToRawIntBits(actual))
                    .isEqualTo(0b1__00000000__00000000_00000000_0000000);
        }
    }

    private static IntStream signMaskStream() {
        return IntStream.of(
                0,
                Integer.MIN_VALUE
        );
    }

    @MethodSource({"signMaskStream"})
    @ParameterizedTest
    void floatOfInfinity__(final int signMask) throws IOException {
        final var actual = wr1u(o -> {
            o.writeFloatOfInfinity(signMask);
            return BitInput::readFloatOfInfinity;
        });
        assertThat(actual).isInfinite(); // 얘는 또 되네???
        if (signMask >= 0) {
            assertThat(Float.floatToRawIntBits(actual))
                    .isEqualTo(0b0__11111111__00000000_00000000_0000000);
        } else {
            assertThat(Float.floatToRawIntBits(actual))
                    .isEqualTo(0b1__11111111__00000000_00000000_0000000);
        }
    }
}
