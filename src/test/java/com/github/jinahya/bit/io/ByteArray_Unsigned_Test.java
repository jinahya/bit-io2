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
class ByteArray_Unsigned_Test {

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

    static Stream<Arguments> randomBytesStream() {
        return IntStream.range(0, 16)
                .mapToObj(i -> {
                    final byte[] randomBytes = randomBytes();
                    return Arguments.of(new Object[]{randomBytes});
                });
    }

    private void run(final byte[] expected) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new ByteOutputAdapter(new StreamByteOutput(baos));
        final BitWriter<byte[]> writer = ByteArrayWriter.unsigned(7, 7);
        writer.write(output, expected);
        final long padded = output.align(1);
        if (expected.length > 0) {
            log.debug("given: {}, written: {}, rate: {}", expected.length, baos.size(),
                      (baos.size() / (double) expected.length) * 100.0d);
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = new ByteInputAdapter(new StreamByteInput(bais));
        final BitReader<byte[]> reader = ByteArrayReader.unsigned(7, 7);
        final byte[] actual = reader.read(input);
        final long discarded = input.align(1);
        assertThat(actual).isEqualTo(expected);
        assertThat(discarded).isEqualTo(padded);
    }

    @MethodSource({"randomBytesStream"})
    @ParameterizedTest
    void test(final byte[] randomBytes) throws IOException {
        run(randomBytes);
    }

    @Test
    void test__empty() throws IOException {
        run(new byte[0]);
    }

    @Test
    void test__one() throws IOException {
        run(randomize(new byte[1]));
    }

    @Test
    void test__two() throws IOException {
        run(randomize(new byte[2]));
    }

    @Test
    void test__three() throws IOException {
        run(randomize(new byte[3]));
    }

    @Test
    void test__four() throws IOException {
        run(randomize(new byte[4]));
    }

    @Test
    void test__five() throws IOException {
        run(randomize(new byte[5]));
    }
}
