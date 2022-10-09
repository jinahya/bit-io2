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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link DoubleWriter.Infinity} and {@link DoubleReader.Infinity}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class BitIo_Double_Infinity_Test {

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

    private static Stream<Double> valueStream() {
        return Stream.concat(
                bitsStream().mapToObj(Double::longBitsToDouble),
                Stream.of(
                        Double.NEGATIVE_INFINITY,
                        Double.POSITIVE_INFINITY
                )
        );
    }

    static void validate(final Double written, final Double read) throws IOException {
        assertThat(read).isInfinite();
        final var valueBits = Double.doubleToRawLongBits(written);
        final var actualBits = Double.doubleToRawLongBits(read);
        if (valueBits >= 0) {
            assertThat(read).isEqualTo(Double.POSITIVE_INFINITY);
            assertThat(actualBits).isEqualTo(DoubleTestConstants.POSITIVE_INFINITY_BITS);
        } else {
            assertThat(read).isEqualTo(Double.NEGATIVE_INFINITY);
            assertThat(actualBits).isEqualTo(DoubleTestConstants.NEGATIVE_INFINITY_BITS);
        }
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void doubleOfInfinity__(final Double value) throws IOException {
        final var actual = wr1u(o -> {
            DoubleWriter.Infinity.getInstance().write(o, value);
            return i -> DoubleReader.Infinity.getInstance().read(i);
        });
        validate(value, actual);
    }

    @Nested
    class NullableTest {

        private static Stream<Double> valueStream_() {
            return valueStream();
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr__(final Double value) throws IOException {
            final var actual = wr1u(o -> {
                DoubleWriter.Infinity.getInstanceNullable().write(o, value);
                return i -> DoubleReader.Infinity.getInstanceNullable().read(i);
            });
            validate(value, actual);
        }

        @Test
        void wr_Null_Null() throws IOException {
            final var actual = wr1u(o -> {
                DoubleWriter.Infinity.getInstanceNullable().write(o, null);
                return i -> DoubleReader.Infinity.getInstanceNullable().read(i);
            });
            assertThat(actual).isNull();
        }
    }
}