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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

class Double64ArrayRwTest {

    @Nested
    class WriteNullableTest {

        @Test
        void nullable_null() throws IOException {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
            Double64ArrayWriter.writeNullableTo(output, null);
            final long padded = output.align();
            final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
            final double[] actual = Double64ArrayReader.readNullableFrom(input);
            final long discarded = input.align();
            assertThat(actual).isNull();
            assertThat(discarded).isEqualTo(padded);
        }

        @Test
        void nullable_nonnull() throws IOException {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
            final double[] expected = new double[current().nextInt(128)];
            for (int i = 0; i < expected.length; i++) {
                expected[i] = current().nextDouble();
            }
            Double64ArrayWriter.writeNullableTo(output, expected);
            final long padded = output.align();
            final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
            final double[] actual = Double64ArrayReader.readNullableFrom(input);
            final long discarded = input.align();
            assertThat(actual).isEqualTo(expected);
            assertThat(discarded).isEqualTo(padded);
        }
    }

    @Test
    void _nonnull() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
        final double[] expected = new double[current().nextInt(128)];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = current().nextDouble();
        }
        Double64ArrayWriter.writeTo(output, expected);
        final long padded = output.align();
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
        final double[] actual = Double64ArrayReader.readFrom(input);
        final long discarded = input.align();
        assertThat(actual).isEqualTo(expected);
        assertThat(discarded).isEqualTo(padded);
    }
}
