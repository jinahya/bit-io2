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
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ByteArray_Unsigned_Wr_Test {

    static byte[] randomize(final byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) current().nextInt(0x00, 0x80);
        }
        return bytes;
    }

    static byte[] randomBytes() {
        final int length = current().nextInt(1024);
        return randomize(new byte[length]);
    }

    static Stream<Arguments> randomBytesAndLengthSizeArgumentsStream() {
        return IntStream.range(0, 16)
                .mapToObj(i -> randomBytes())
                .map(b -> Arguments.of(b, BitIoUtils.size(b.length)))
                ;
    }

    private void run(final byte[] expected, final int lengthSize) throws IOException {
        final var baos = new ByteArrayOutputStream();
        final var output = BitOutputFactory.from(baos);
        final var writer = ByteArrayWriter.unsigned(lengthSize, 7);
        writer.write(output, expected);
        final var padded = output.align(1);
        if (false && expected.length > 0) {
            final var given = expected.length + Integer.BYTES;
            log.debug("given: {}, written: {}, rate: {}", given, baos.size(), (baos.size() / (double) given) * 100.0d);
        }
        final var bais = new ByteArrayInputStream(baos.toByteArray());
        final var input = BitInputFactory.from(bais);
        final var reader = ByteArrayReader.unsigned(lengthSize, 7);
        final var actual = reader.read(input);
        final var discarded = input.align(1);
        assertThat(actual).isEqualTo(expected);
        assertThat(discarded).isEqualTo(padded);
    }

    @MethodSource({"randomBytesAndLengthSizeArgumentsStream"})
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
    void nullable__() throws IOException {
        final var lengthSize = 31;
        final var baos = new ByteArrayOutputStream();
        final var output = BitOutputFactory.from(baos);
        final var writer = ByteArrayWriter.unsigned(lengthSize, 7).nullable();
        writer.write(output, null);
        final var padded = output.align(1);
        final var bais = new ByteArrayInputStream(baos.toByteArray());
        final var input = BitInputFactory.from(bais);
        final var reader = ByteArrayReader.unsigned(lengthSize, 7).nullable();
        final var actual = reader.read(input);
        final var discarded = input.align(1);
        assertThat(actual).isNull();
        assertThat(discarded).isEqualTo(padded);
    }
}
