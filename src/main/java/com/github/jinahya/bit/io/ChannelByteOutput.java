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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A byte output writes bytes from a writable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteInput
 */
public class ChannelByteOutput
        extends ByteOutputAdapter<WritableByteChannel> {

    /**
     * Creates a new instance which writes bytes to specified channel.
     *
     * @param channel the channel to which bytes are written.
     * @param buffer  a buffer to use; must have a non-zero capacity.
     * @apiNote Closing the result output does not close the {@code channel}.
     */
    public static ChannelByteOutput from(final WritableByteChannel channel, final ByteBuffer buffer) {
        Objects.requireNonNull(channel, "channel is null");
        Objects.requireNonNull(buffer, "buffer is null");
        final ChannelByteOutput instance = new ChannelByteOutput(BitIoUtils.emptySupplier(), buffer);
        instance.target(channel);
        return instance;
    }

    /**
     * Creates a new instance which writes bytes to specified path.
     *
     * @param path    the path to which bytes are written.
     * @param buffer  a buffer to use; must have a non-zero capacity.
     * @param options an array of open options.
     * @see #from(Path, ByteBuffer)
     */
    public static ChannelByteOutput from(final Path path, final ByteBuffer buffer, final OpenOption... options) {
        Objects.requireNonNull(path, "path is null");
        Objects.requireNonNull(buffer, "buffer is null");
        Objects.requireNonNull(options, "options is null");
        return new ChannelByteOutput(
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
     * Creates a new instance which writes bytes to specified path.
     *
     * @param path   the path to which bytes are written.
     * @param buffer a buffer to use; must have a non-zero capacity.
     * @see #from(Path, ByteBuffer, OpenOption...)
     */
    public static ChannelByteOutput from(final Path path, final ByteBuffer buffer) {
        return from(path, buffer, StandardOpenOption.WRITE);
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param supplier a supplier for lazily opening a channel.
     * @param buffer   a buffer to use; must have a non-zero capacity.
     */
    public ChannelByteOutput(final Supplier<? extends WritableByteChannel> supplier, final ByteBuffer buffer) {
        super(supplier);
        if (Objects.requireNonNull(buffer, "buffer is null").capacity() == 0) {
            throw new IllegalArgumentException("buffer.capacity is zero");
        }
        this.buffer = buffer;
    }

    @Override
    public void flush() throws IOException {
        super.flush(); // does nothing, effectively.
        for (((java.nio.Buffer) buffer).flip(); buffer.hasRemaining(); ) {
            target(true).write(buffer);
        }
        ((java.nio.Buffer) buffer).clear();
        final WritableByteChannel channel = target(false);
        if (channel instanceof FileChannel) {
            ((FileChannel) channel).force(false);
        }
    }

    @Override
    public void write(final int value) throws IOException {
        if (buffer.hasRemaining()) {
            buffer.put((byte) value);
            return;
        }
        super.write(value);
    }

    @Override
    protected void write(final WritableByteChannel target, final int value) throws IOException {
        assert !buffer.hasRemaining();
        for (((java.nio.Buffer) buffer).flip(); target.write(buffer) == 0; ) {
        }
        buffer.compact();
        assert buffer.hasRemaining();
        write(value);
    }

    private final ByteBuffer buffer;
}
