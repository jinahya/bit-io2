package com.github.jinahya.bit.io.recipe;

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

import com.github.jinahya.bit.io.BufferByteInput;
import com.github.jinahya.bit.io.BufferByteOutput;
import com.github.jinahya.bit.io.ByteInputAdapter;
import com.github.jinahya.bit.io.ByteOutputAdapter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;

@Slf4j
class ChannelWithNonSingleBufferTest {

    @Test
    void wr() throws IOException {
        final var count = 7;
        try (var channel = openChannelToWrite()) {
            final var buffer = ByteBuffer.allocate(2);
            final var byteOutput = new BufferByteOutput(buffer) {
                @Override
                public void write(final int value) throws IOException {
                    if (!buffer.hasRemaining()) {
                        buffer.flip();
                        while (buffer.position() == 0) {
                            channel.write(buffer);
                        }
                        buffer.compact();
                    }
                    super.write(value);
                }
            };
            final var bitOutput = new ByteOutputAdapter(byteOutput);
            for (var size = 1; size < count; size++) {
                bitOutput.writeLong(true, size, 0L);
            }
            final var padded = bitOutput.align(1);
            for (buffer.flip(); buffer.hasRemaining(); ) {
                channel.write(buffer);
            }
        }
        try (var channel = openChannelToRead()) {
            final var buffer = ByteBuffer.allocate(2);
            buffer.position(buffer.capacity());
            final var byteInput = new BufferByteInput(buffer) {
                @Override
                public int read() throws IOException {
                    if (!buffer.hasRemaining()) {
                        buffer.clear();
                        while (buffer.position() == 0) {
                            channel.read(buffer);
                        }
                        buffer.flip();
                    }
                    return super.read();
                }
            };
            final var bitInput = new ByteInputAdapter(byteInput);
            for (var size = 1; size < count; size++) {
                final var value = bitInput.readLong(false, size);
            }
            final var discarded = bitInput.align(1);
            assert !buffer.hasRemaining();
        }
    }

    private WritableByteChannel openChannelToWrite() {
        outputStream = new ByteArrayOutputStream();
        return Channels.newChannel(outputStream);
    }

    private ReadableByteChannel openChannelToRead() {
        Objects.requireNonNull(outputStream, "outputStream is null");
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return Channels.newChannel(inputStream);
    }

    private ByteArrayOutputStream outputStream;

    private ByteArrayInputStream inputStream;
}
