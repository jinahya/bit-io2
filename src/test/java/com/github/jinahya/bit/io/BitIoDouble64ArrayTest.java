package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BitIoDouble64ArrayTest {

    static Stream<double[]> values() {
        return IntStream.range(0, 16)
                .mapToObj(i -> BitIoDouble64Test.values())
                .map(DoubleStream::toArray);
    }

    @MethodSource({"values"})
    @ParameterizedTest
    void wr__(final double[] expected) throws IOException {
        BitIoTestUtils.wr2v(o -> {
            new Double64ArrayWriter(5).write(o, expected);
            return i -> {
                final double[] actual = new Double64ArrayReader(5).read(i);
                assertThat(actual).isEqualTo(expected);
            };
        });
    }
}
