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

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A byte input reads bytes from a readable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteOutput
 */
public class ChannelByteInput
        extends ByteInputAdapter<ReadableByteChannel> {

    /**
     * Creates a new instance which reads bytes from specified channel.
     *
     * @param channel the channel from which bytes are read.
     * @param buffer  a buffer to use; must have a non-zero capacity.
     * @apiNote Closing the result input does not close the {@code channel}.
     */
    public static ChannelByteInput from(final ReadableByteChannel channel, final ByteBuffer buffer) {
        Objects.requireNonNull(channel, "channel is null");
        final ChannelByteInput instance = new ChannelByteInput(BitIoUtils.emptySupplier(), buffer);
        instance.source(channel);
        return instance;
    }

    /**
     * Creates a new instance which reads bytes from specified path.
     *
     * @param path    the path from which bytes are read.
     * @param buffer  a buffer to use; must have a non-zero capacity.
     * @param options an array of open options.
     * @return a new instance.
     * @see #from(Path, ByteBuffer)
     */
    public static ChannelByteInput from(final Path path, final ByteBuffer buffer, final OpenOption... options) {
        Objects.requireNonNull(path, "path is null");
        Objects.requireNonNull(buffer, "buffer is null");
        Objects.requireNonNull(options, "options is null");
        return new ChannelByteInput(
                () -> {
                    try {
                        return FileChannel.open(path, options);
                    } catch (final IOException ioe) {
                        throw new RuntimeException("failed to open " + path, ioe);
                    }
                },
                buffer
        );
    }

    /**
     * Creates a new instance which reads bytes from specified path.
     *
     * @param path   the path from which bytes are read.
     * @param buffer a buffer to use; must have a non-zero capacity.
     * @see #from(Path, ByteBuffer, OpenOption...)
     */
    public static ChannelByteInput from(final Path path, final ByteBuffer buffer) {
        return from(path, buffer, StandardOpenOption.READ);
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param supplier a supplier for lazily opening a channel.
     * @param buffer   a non-zero capacity buffer to use.
     */
    public ChannelByteInput(final Supplier<? extends ReadableByteChannel> supplier, final ByteBuffer buffer) {
        super(supplier);
        if (Objects.requireNonNull(buffer, "buffer is null").capacity() == 0) {
            throw new IllegalArgumentException("buffer.capacity is zero");
        }
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        if (buffer.hasRemaining()) {
            return buffer.get() & 0xFF;
        }
        return super.read();
    }

    @Override
    protected int read(final ReadableByteChannel source) throws IOException {
        assert !buffer.hasRemaining();
        ((java.nio.Buffer) buffer).clear();
        while (true) {
            final int read = source(true).read(buffer);
            if (read == -1) {
                throw new EOFException("channel has reached to end-of-stream");
            }
            if (read > 0) {
                break;
            }
        }
        ((java.nio.Buffer) buffer).flip();
        assert buffer.hasRemaining();
        return read();
    }

    private final ByteBuffer buffer;
}
