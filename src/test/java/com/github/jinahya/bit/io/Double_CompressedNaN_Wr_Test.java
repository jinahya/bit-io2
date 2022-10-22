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
 * A class for testing {@link DoubleWriter.CompressedNaN} and {@link DoubleReader.CompressedNaN}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class Double_CompressedNaN_Wr_Test {

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

    static Stream<Double> valueStream() {
        return Stream.concat(
                        bitsStream().mapToObj(Double::longBitsToDouble),
                        Stream.of(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
                )
                .filter(v -> (Double.doubleToRawLongBits(v) & DoubleConstants.MASK_SIGNIFICAND_BITS) > 0);
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr__Full(final Double value) throws IOException {
        final int significandSize = DoubleConstants.SIZE_MAX_SIGNIFICAND;
        final var actual = wr1u(o -> {
            new DoubleWriter.CompressedNaN(significandSize).write(o, value);
            return i -> new DoubleReader.CompressedNaN(significandSize).read(i);
        });
        assertThat(actual).isNaN();
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr__FullNullable(final Double value) throws IOException {
        final int significandSize = DoubleConstants.SIZE_MAX_SIGNIFICAND;
        final var actual = wr1u(o -> {
            new DoubleWriter.CompressedNaN(significandSize).nullable().write(o, value);
            return i -> new DoubleReader.CompressedNaN(significandSize).nullable().read(i);
        });
        assertThat(actual).isNaN();
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr2__Compressed(final Double value) throws IOException {
        var bits = Double.doubleToRawLongBits(value) & DoubleConstants.MASK_SIGNIFICAND_BITS;
        bits >>= Long.numberOfTrailingZeros(bits);
        final var significandSize = Math.max(Long.SIZE - Long.numberOfLeadingZeros(bits),
                                             DoubleConstants.SIZE_MIN_SIGNIFICAND);
        final var actual = wr1u(o -> {
            new DoubleWriter.CompressedNaN(significandSize).write(o, value);
            return i -> new DoubleReader.CompressedNaN(significandSize).read(i);
        });
        assertThat(actual).isNaN();
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr2__CompressedNullable(final Double value) throws IOException {
        var bits = Double.doubleToRawLongBits(value) & DoubleConstants.MASK_SIGNIFICAND_BITS;
        bits >>= Long.numberOfTrailingZeros(bits);
        final var significandSize = Math.max(Long.SIZE - Long.numberOfLeadingZeros(bits),
                                             DoubleConstants.SIZE_MIN_SIGNIFICAND);
        final var actual = wr1u(o -> {
            new DoubleWriter.CompressedNaN(significandSize).nullable().write(o, value);
            return i -> new DoubleReader.CompressedNaN(significandSize).nullable().read(i);
        });
        assertThat(actual).isNaN();
    }

    @Nested
    class CashedInstanceTest {

        private static Stream<Double> valueStream_() {
            return valueStream();
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr__Full(final Double value) throws IOException {
            final int significand = DoubleConstants.SIZE_MAX_SIGNIFICAND;
            final var actual = wr1u(o -> {
                DoubleWriter.CompressedNaN.getCachedInstance(significand).write(o, value);
                return i -> DoubleReader.CompressedNaN.getCachedInstance(significand).read(i);
            });
            assertThat(actual).isNaN();
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr__FullNullable(final Double value) throws IOException {
            final int significand = DoubleConstants.SIZE_MAX_SIGNIFICAND;
            final var actual = wr1u(o -> {
                DoubleWriter.CompressedNaN.getCachedInstance(significand).nullable().write(o, value);
                return i -> DoubleReader.CompressedNaN.getCachedInstance(significand).nullable().read(i);
            });
            assertThat(actual).isNaN();
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr2__Compressed(final Double value) throws IOException {
            var bits = Double.doubleToRawLongBits(value) & DoubleConstants.MASK_SIGNIFICAND_BITS;
            bits >>= Long.numberOfTrailingZeros(bits);
            final int significandSize = Math.max(Long.SIZE - Long.numberOfLeadingZeros(bits),
                                                 DoubleConstants.SIZE_MIN_SIGNIFICAND);
            final var actual = wr1u(o -> {
                DoubleWriter.CompressedNaN.getCachedInstance(significandSize).write(o, value);
                return i -> DoubleReader.CompressedNaN.getCachedInstance(significandSize).read(i);
            });
            assertThat(actual).isNaN();
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr2__CompressedNullable(final Double value) throws IOException {
            var bits = Double.doubleToRawLongBits(value) & DoubleConstants.MASK_SIGNIFICAND_BITS;
            bits >>= Long.numberOfTrailingZeros(bits);
            final var significandSize = Math.max(Long.SIZE - Long.numberOfLeadingZeros(bits),
                                                 DoubleConstants.SIZE_MIN_SIGNIFICAND);
            final var actual = wr1u(o -> {
                DoubleWriter.CompressedNaN.getCachedInstance(significandSize).nullable().write(o, value);
                return i -> DoubleReader.CompressedNaN.getCachedInstance(significandSize).nullable().read(i);
            });
            assertThat(actual).isNaN();
        }

        @Test
        void wr_Null_Null() throws IOException {
            final int significand = DoubleConstants.SIZE_MAX_SIGNIFICAND;
            final var actual = wr1u(o -> {
                DoubleWriter.CompressedNaN.getCachedInstance(significand).nullable().write(o, null);
                return i -> DoubleReader.CompressedNaN.getCachedInstance(significand).nullable().read(i);
            });
            assertThat(actual).isNull();
        }
    }
}
