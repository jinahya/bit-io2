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
class ByteArrayAsciiTest {

    static byte[] randomize(final byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ThreadLocalRandom.current().nextInt(0x00, 0x80);
        }
        return bytes;
    }

    static byte[] randomBytes() {
        final int length = ThreadLocalRandom.current().nextInt(128);
        return randomize(new byte[length]);
    }

    static Stream<Arguments> randomBytesAndLengthSizeStream() {
        return IntStream.range(0, 16)
                .mapToObj(i -> {
                    final byte[] randomBytes = randomBytes();
                    final int lengthSize = BitIoUtils.size(randomBytes.length);
                    return Arguments.of(randomBytes, lengthSize);
                });
    }

    private void run(final byte[] expected, final int lengthSize) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
        final BitWriter<byte[]> writer = ByteArrayWriter.ascii(lengthSize, false);
        writer.write(output, expected);
        final long padded = output.align();
        if (expected.length > 0) {
            log.debug("uncompressed: {}, compressed: {}, rate: {}", expected.length, baos.size(),
                      (baos.size() / (double) expected.length) * 100.0d);
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
        final BitReader<byte[]> reader = ByteArrayReader.ascii(lengthSize, false);
        final byte[] actual = reader.read(input);
        final long discarded = input.align();
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
}
