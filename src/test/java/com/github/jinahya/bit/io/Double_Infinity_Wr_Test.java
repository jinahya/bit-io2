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
class Double_Infinity_Wr_Test {

    private static Stream<Double> valueStream() {
        return BitIo_Double_Infinity_Test.valueStream();
    }

    @MethodSource({"valueStream"})
    @ParameterizedTest
    void wr__(final Double value) throws IOException {
        final var actual = wr1u(o -> {
            DoubleWriter.Infinity.getInstance().write(o, value);
            return i -> DoubleReader.Infinity.getInstance().read(i);
        });
        BitIo_Double_Infinity_Test.validate(value, actual);
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
            BitIo_Double_Infinity_Test.validate(value, actual);
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
