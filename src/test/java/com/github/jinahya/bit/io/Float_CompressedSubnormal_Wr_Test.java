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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link FloatWriter.CompressedInfinity} and {@link FloatReader.CompressedInfinity}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class Float_CompressedSubnormal_Wr_Test {

    private static Stream<Float> valueStream() {
        return FloatTestParameters.valueStream()
                .filter(v -> (Float.floatToRawIntBits(v) & FloatConstants.MASK_SIGNIFICAND) > 0);
    }

    static void verify(final Float written, final Float read) {
        FloatTestConstraints.assertSignBitsAreSame(written, read);
        FloatTestConstraints.requireSubnormal(read);
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr__(final Float value) throws IOException {
        {
            final var actual = wr1u(o -> {
                new FloatWriter.CompressedSubnormal(FloatConstants.SIZE_SIGNIFICAND).write(o, value);
                return i -> new FloatReader.CompressedSubnormal(FloatConstants.SIZE_SIGNIFICAND).read(i);
            });
            verify(value, actual);
        }
        {
            final var actual = wr1u(o -> {
                FloatWriter.CompressedSubnormal.getCachedInstance(FloatConstants.SIZE_SIGNIFICAND).write(o, value);
                return i -> FloatReader.CompressedSubnormal.getCachedInstance(FloatConstants.SIZE_SIGNIFICAND).read(i);
            });
            verify(value, actual);
        }
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr__Compressed(final Float value) throws IOException {
        final var significandBits = Float.floatToRawIntBits(value) & FloatConstants.MASK_SIGNIFICAND;
        final int significandSize = Math.max(
                FloatConstants.SIZE_SIGNIFICAND - Integer.numberOfTrailingZeros(significandBits),
                FloatConstants.SIZE_MIN_SIGNIFICAND
        );
        {
            final var actual = wr1u(o -> {
                new FloatWriter.CompressedSubnormal(significandSize).write(o, value);
                return i -> new FloatReader.CompressedSubnormal(significandSize).read(i);
            });
            verify(value, actual);
        }
        {
            final var actual = wr1u(o -> {
                FloatWriter.CompressedSubnormal.getCachedInstance(significandSize).write(o, value);
                return i -> FloatReader.CompressedSubnormal.getCachedInstance(significandSize).read(i);
            });
            verify(value, actual);
        }
    }

    @Nested
    class NullableTest {

        private static Stream<Float> valueStream_() {
            return valueStream();
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr__(final Float value) throws IOException {
            {
                final var actual = wr1u(o -> {
                    new FloatWriter.CompressedSubnormal(FloatConstants.SIZE_SIGNIFICAND).nullable().write(o, value);
                    return i -> new FloatReader.CompressedSubnormal(FloatConstants.SIZE_SIGNIFICAND).nullable().read(i);
                });
                verify(value, actual);
            }
            {
                final var actual = wr1u(o -> {
                    FloatWriter.CompressedSubnormal.getCachedInstance(FloatConstants.SIZE_SIGNIFICAND)
                            .nullable().write(o, value);
                    return i -> FloatReader.CompressedSubnormal.getCachedInstance(FloatConstants.SIZE_SIGNIFICAND)
                            .nullable().read(i);
                });
                verify(value, actual);
            }
        }

        @MethodSource({"valueStream_"})
        @ParameterizedTest
        void wr__Compressed(final Float value) throws IOException {
            final var significandBits = Float.floatToRawIntBits(value) & FloatConstants.MASK_SIGNIFICAND;
            final int significandSize = Math.max(
                    FloatConstants.SIZE_SIGNIFICAND - Integer.numberOfTrailingZeros(significandBits),
                    FloatConstants.SIZE_MIN_SIGNIFICAND
            );
            {
                final var actual = wr1u(o -> {
                    new FloatWriter.CompressedSubnormal(significandSize).nullable().write(o, value);
                    return i -> new FloatReader.CompressedSubnormal(significandSize).nullable().read(i);
                });
                verify(value, actual);
            }
            {
                final var actual = wr1u(o -> {
                    FloatWriter.CompressedSubnormal.getCachedInstance(significandSize).nullable().write(o, value);
                    return i -> FloatReader.CompressedSubnormal.getCachedInstance(significandSize).nullable().read(i);
                });
                verify(value, actual);
            }
        }

        @DisplayName("nullable().write(null) -> nullable().read()null")
        @Test
        void wr_Null_Nullable() throws IOException {
            final int significand = FloatConstants.SIZE_SIGNIFICAND;
            {
                final var actual = wr1u(o -> {
                    new FloatWriter.CompressedSubnormal(significand).nullable().write(o, null);
                    return i -> new FloatReader.CompressedSubnormal(significand).nullable().read(i);
                });
                assertThat(actual).isNull();
            }
            {
                final var actual = wr1u(o -> {
                    FloatWriter.CompressedSubnormal.getCachedInstance(significand).nullable().write(o, null);
                    return i -> FloatReader.CompressedSubnormal.getCachedInstance(significand).nullable().read(i);
                });
                assertThat(actual).isNull();
            }
        }
    }
}
