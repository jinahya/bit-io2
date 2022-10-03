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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ByteArray_CompressedAscii_Printable_Wr_Test {

    private static byte[] randomize(final byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ThreadLocalRandom.current().nextInt(0x20, 0x7F);
        }
        return bytes;
    }

    private static byte[] randomBytes() {
        final int length = ThreadLocalRandom.current().nextInt(1024);
        return randomize(new byte[length]);
    }

    static Stream<byte[]> randomBytesStream() {
        return IntStream.range(0, 16)
                .mapToObj(i -> randomBytes())
                ;
    }

    private static Stream<Arguments> randomBytesAndLengthSizeStream() {
        return randomBytesStream()
                .map(b -> Arguments.of(b, BitIoUtils.size(b.length)))
                ;
    }

    private void run(final byte[] expected, final int lengthSize) throws IOException {
        final var baos = new ByteArrayOutputStream();
        final var output = new ByteOutputAdapter(new StreamByteOutput(baos));
        final var writer = ByteArrayWriter.compressedAscii(lengthSize, true);
        writer.write(output, expected);
        final var padded = output.align(1);
        {
            final var given = expected.length + Integer.BYTES;
            log.debug("given: {}, written: {}, ratio: {}", given, baos.size(), (baos.size() / (double) given) * 100.0d);
        }
        final var bais = new ByteArrayInputStream(baos.toByteArray());
        final var input = new ByteInputAdapter(new StreamByteInput(bais));
        final var reader = ByteArrayReader.compressedAscii(lengthSize, true);
        final var actual = reader.read(input);
        final var discarded = input.align(1);
        assertThat(actual).isEqualTo(expected);
        assertThat(discarded).isEqualTo(padded);
    }

    @MethodSource({"randomBytesAndLengthSizeStream"})
    @ParameterizedTest
    void test(final byte[] randomBytes, final int lengthSize) throws IOException {
        run(randomBytes, lengthSize);
    }

    @Test
    void test__empty() throws IOException {
        run(new byte[0], 1);
    }

    @Test
    void test__one() throws IOException {
        run(randomize(new byte[1]), 1);
    }

    @Test
    void test__two() throws IOException {
        run(randomize(new byte[2]), 2);
    }

    @Test
    void test__three() throws IOException {
        run(randomize(new byte[3]), 2);
    }

    @Test
    void test__four() throws IOException {
        run(randomize(new byte[4]), 3);
    }

    @Test
    void test__five() throws IOException {
        run(randomize(new byte[5]), 3);
    }

    @Test
    void asNullable_() throws IOException {
        final var lengthSize = 31;
        final var printableOnly = true;
        final var baos = new ByteArrayOutputStream();
        final var output = new ByteOutputAdapter(new StreamByteOutput(baos));
        final var writer = ByteArrayWriter.compressedAscii(lengthSize, printableOnly).nullable();
        writer.write(output, null);
        final var padded = output.align(1);
        final var bais = new ByteArrayInputStream(baos.toByteArray());
        final var input = new ByteInputAdapter(new StreamByteInput(bais));
        final var reader = ByteArrayReader.compressedAscii(lengthSize, printableOnly).nullable();
        final var actual = reader.read(input);
        final var discarded = input.align(1);
        assertThat(actual).isNull();
        assertThat(discarded).isEqualTo(padded);
    }
}
