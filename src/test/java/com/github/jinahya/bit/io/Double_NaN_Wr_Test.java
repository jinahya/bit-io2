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
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link DoubleWriter.Infinity} and {@link DoubleReader.Infinity}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class Double_NaN_Wr_Test {

    private static Stream<Double> valueStream() {
        return BitIo_Double_Infinity_Test.valueStream()
                .filter(v -> (Double.doubleToRawLongBits(v) & DoubleConstants.MASK_SIGNIFICAND) > 0);
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr__(final Double value) throws IOException {
        final int significandSize = DoubleConstants.SIZE_SIGNIFICAND;
        final var actual = wr1u(o -> {
            DoubleWriter.NaN.getInstance(significandSize).write(o, value);
            return i -> DoubleReader.NaN.getInstance(significandSize).read(i);
        });
        assertThat(actual).isNaN();
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr2__(final Double value) throws IOException {
        var bits = Double.doubleToRawLongBits(value) & DoubleConstants.MASK_SIGNIFICAND;
        bits >>= Long.numberOfTrailingZeros(bits);
        final int significandSize = Math.max(Long.SIZE - Long.numberOfLeadingZeros(bits),
                                             DoubleConstants.SIZE_MIN_SIGNIFICAND);
        final var actual = wr1u(o -> {
            DoubleWriter.NaN.getInstance(significandSize).write(o, value);
            return i -> DoubleReader.NaN.getInstance(significandSize).read(i);
        });
        assertThat(actual).isNaN();
    }

    @Nested
    class NullableTest {

        private static Stream<Double> valueStream_() {
            return valueStream();
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr__(final Double value) throws IOException {
            final int significand = DoubleConstants.SIZE_SIGNIFICAND;
            final var actual = wr1u(o -> {
                DoubleWriter.NaN.getInstance(significand).write(o, value);
                return i -> DoubleReader.NaN.getInstance(significand).read(i);
            });
            assertThat(actual).isNaN();
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr2__(final Double value) throws IOException {
            var bits = Double.doubleToRawLongBits(value) & DoubleConstants.MASK_SIGNIFICAND;
            bits >>= Long.numberOfTrailingZeros(bits);
            final int significandSize = Math.max(Long.SIZE - Long.numberOfLeadingZeros(bits),
                                                 DoubleConstants.SIZE_MIN_SIGNIFICAND);
            final var actual = wr1u(o -> {
                DoubleWriter.NaN.getInstance(significandSize).write(o, value);
                return i -> DoubleReader.NaN.getInstance(significandSize).read(i);
            });
            assertThat(actual).isNaN();
        }

        @Test
        void wr_Null_Null() throws IOException {
            final int significand = DoubleConstants.SIZE_SIGNIFICAND;
            final var actual = wr1u(o -> {
                DoubleWriter.NaN.getInstance(significand).nullable().write(o, null);
                return i -> DoubleReader.NaN.getInstance(significand).nullable().read(i);
            });
            assertThat(actual).isNull();
        }
    }
}
