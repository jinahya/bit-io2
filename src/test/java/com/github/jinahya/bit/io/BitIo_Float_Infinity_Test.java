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
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link FloatWriter.Infinity} and {@link FloatReader.Infinity}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class BitIo_Float_Infinity_Test {

    private static IntStream bitsStream() {
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

    static Stream<Float> valueStream() {
        return Stream.concat(
                bitsStream().mapToObj(Float::intBitsToFloat),
                Stream.of(
                        Float.NEGATIVE_INFINITY,
                        Float.POSITIVE_INFINITY
                )
        );
    }

    static void validate(final Float written, final Float read) throws IOException {
        assertThat(read).isInfinite();
        final var valueBits = Float.floatToRawIntBits(written);
        final var actualBits = Float.floatToRawIntBits(read);
        if (valueBits >= 0) { // read.floatValue() >= .0f 는 -.0f 와 +.0f 를 구분하지 못한다.
            assertThat(read).isEqualTo(Float.POSITIVE_INFINITY);
            assertThat(actualBits).isEqualTo(FloatTestConstants.POSITIVE_INFINITY_BITS);
        } else {
            assertThat(read).isEqualTo(Float.NEGATIVE_INFINITY);
            assertThat(actualBits).isEqualTo(FloatTestConstants.NEGATIVE_INFINITY_BITS);
        }
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr__(final Float value) throws IOException {
        final var actual = wr1u(o -> {
            o.writeFloatOfInfinity(value);
            return BitInput::readFloatOfInfinity;
        });
        validate(value, actual);
    }
}
