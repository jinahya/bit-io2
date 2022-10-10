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
import org.junit.jupiter.params.aggregator.DefaultArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link BitOutput#writeFloatOfZero(float)} method and {@link BitInput#readFloatOfZero()} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class Float_Wr_Test {

    static Stream<Arguments> sizes() {
        return IntStream.range(0, 16)
                .mapToObj(i -> Arguments.of(BitIoRandom.nextExponentSizeForFloat(),
                                            BitIoRandom.nextSignificandSizeForFloat()))
                ;
    }

    static Stream<Arguments> sizesAndValues() {
        return sizes()
                .map(a -> {
                    final var daa = new DefaultArgumentsAccessor(a.get());
                    final var exponentSize = daa.getInteger(0);
                    final var significandSize = daa.getInteger(1);
                    final var value = BitIoRandom.nextValueForFloat(exponentSize, significandSize);
                    return Arguments.of(exponentSize, significandSize, value);
                });
    }

    @MethodSource({"sizesAndValues"})
    @ParameterizedTest
    void rw__(final int exponentSize, final int significandSize, final Float value) throws IOException {
        final var actual = wr1u(o -> {
            FloatWriter.getInstance(exponentSize, significandSize).write(o, value);
            return i -> FloatReader.getInstance(exponentSize, significandSize).read(i);
        });
        if (value.isNaN()) {
            assertThat(actual).isNaN();
            return;
        }
        assertThat(actual).isEqualTo(value);
    }

    @Nested
    class NullableTest {

        private static Stream<Arguments> sizes_() {
            return sizes();
        }

        private static Stream<Arguments> sizesAndValues_() {
            return sizesAndValues();
        }

        @MethodSource({"sizesAndValues_"})
        @ParameterizedTest
        void wr__(final int exponentSize, final int significandSize, final Float value) throws IOException {
            final var actual = wr1u(o -> {
                FloatWriter.getInstance(exponentSize, significandSize).nullable().write(o, value);
                return i -> FloatReader.getInstance(exponentSize, significandSize).nullable().read(i);
            });
            if (value.isNaN()) {
                assertThat(actual).isNaN();
                return;
            }
            assertThat(actual).isEqualTo(value);
        }

        @MethodSource({"sizes_"})
        @ParameterizedTest
        void wr_Null_Null(final int exponentSize, final int significandSize) throws IOException {
            final var actual = wr1u(o -> {
                FloatWriter.getInstance(exponentSize, significandSize).nullable().write(o, null);
                return i -> FloatReader.getInstance(exponentSize, significandSize).nullable().read(i);
            });
            assertThat(actual).isNull();
        }
    }
}
