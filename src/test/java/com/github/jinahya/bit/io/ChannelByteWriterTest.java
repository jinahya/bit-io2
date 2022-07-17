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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.ByteBuffer.allocate;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ChannelByteWriterTest {

    @RepeatedTest(16)
    void from__Path(@TempDir final Path tempDir) throws IOException {
        final byte[] expected = new byte[current().nextInt(16)];
        current().nextBytes(expected);
        final Path path = Files.createTempFile(tempDir, null, null);
        try (ByteOutput output = ChannelByteOutput.from(path, allocate(current().nextInt(1, 8)))) {
            for (final byte b : expected) {
                output.write(b & 0XFF);
            }
            output.flush();
        }
        assertThat(Files.size(path)).isEqualTo(expected.length);
    }

    @Disabled
    @RepeatedTest(16)
    void from__Channel(@TempDir final Path tempDir) throws IOException {
        final byte[] expected = new byte[current().nextInt(16)];
        current().nextBytes(expected);
        final Path path = Files.createTempFile(tempDir, null, null);
        try (FileChannel channel = spy(FileChannel.open(path, StandardOpenOption.WRITE))) {
            try (ByteOutput output = ChannelByteOutput.from(channel, allocate(current().nextInt(1, 8)))) {
                for (final byte b : expected) {
                    output.write(b & 0XFF);
                }
                output.flush();
                verify(channel, times(1)).force(anyBoolean());
            }
            verify(channel, times(0)).close();
        }
        assertThat(Files.size(path)).isEqualTo(expected.length);
    }
}
