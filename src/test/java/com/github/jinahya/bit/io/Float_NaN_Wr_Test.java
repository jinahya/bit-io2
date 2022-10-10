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
 * A class for testing {@link FloatWriter.Infinity} and {@link FloatReader.Infinity}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class Float_NaN_Wr_Test {

    private static Stream<Float> valueStream() {
        return BitIo_Float_Infinity_Test.valueStream()
                .filter(v -> (Float.floatToRawIntBits(v) & FloatConstants.MASK_SIGNIFICAND) > 0);
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr__(final Float value) throws IOException {
        final int significand = FloatConstants.SIZE_SIGNIFICAND;
        final var actual = wr1u(o -> {
            FloatWriter.NaN.getInstance(significand).write(o, value);
            return i -> FloatReader.NaN.getInstance(significand).read(i);
        });
        assertThat(actual).isNaN();
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr2__(final Float value) throws IOException {
        var bits = Float.floatToRawIntBits(value) & FloatConstants.MASK_SIGNIFICAND;
        bits >>= Integer.numberOfTrailingZeros(bits);
        final int significandSize = Math.max(Integer.SIZE - Integer.numberOfLeadingZeros(bits),
                                             FloatConstants.SIZE_MIN_SIGNIFICAND);
        final var actual = wr1u(o -> {
            FloatWriter.NaN.getInstance(significandSize).write(o, value);
            return i -> FloatReader.NaN.getInstance(significandSize).read(i);
        });
        assertThat(actual).isNaN();
    }

    @Nested
    class NullableTest {

        private static Stream<Float> valueStream_() {
            return valueStream();
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr__(final Float value) throws IOException {
            final int significand = FloatConstants.SIZE_SIGNIFICAND;
            final var actual = wr1u(o -> {
                FloatWriter.NaN.getInstance(significand).write(o, value);
                return i -> FloatReader.NaN.getInstance(significand).read(i);
            });
            assertThat(actual).isNaN();
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr2__(final Float value) throws IOException {
            var bits = Float.floatToRawIntBits(value) & FloatConstants.MASK_SIGNIFICAND;
            bits >>= Integer.numberOfTrailingZeros(bits);
            final int significandSize = Math.max(Integer.SIZE - Integer.numberOfLeadingZeros(bits),
                                                 FloatConstants.SIZE_MIN_SIGNIFICAND);
            final var actual = wr1u(o -> {
                FloatWriter.NaN.getInstance(significandSize).write(o, value);
                return i -> FloatReader.NaN.getInstance(significandSize).read(i);
            });
            assertThat(actual).isNaN();
        }

        @Test
        void wr_Null_Null() throws IOException {
            final int significand = FloatConstants.SIZE_SIGNIFICAND;
            final var actual = wr1u(o -> {
                FloatWriter.NaN.getInstance(significand).nullable().write(o, null);
                return i -> FloatReader.NaN.getInstance(significand).nullable().read(i);
            });
            assertThat(actual).isNull();
        }
    }
}
