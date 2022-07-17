package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 Jinahya, Inc.
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
import java.nio.channels.ReadableByteChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A byte input reads bytes from a {@link ByteBuffer}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput
        extends ByteInputAdapter<ByteBuffer> {

    /**
     * Creates a new instance which reads bytes from the channel supplied by specified supplier.
     *
     * @param channelSupplier the supplier of the channel.
     * @return a new instance.
     * @apiNote Closing the result input also closes the {@code channel}.
     */
    public static BufferByteInput adapting(
            final Supplier<? extends ReadableByteChannel> channelSupplier) {
        final Supplier<ByteBuffer> bufferSupplier
                = () -> (ByteBuffer) ((java.nio.Buffer) ByteBuffer.allocate(1)).position(1);
        return new BufferByteInputChannelAdapter(bufferSupplier, channelSupplier);
    }

    /**
     * Creates a new instance which reads bytes from specified channel.
     *
     * @param channel the channel from which bytes are read.
     * @return a new instance.
     * @apiNote Closing the result input does not close the {@code channel}.
     */
    public static BufferByteInput adapting(final ReadableByteChannel channel) {
        Objects.requireNonNull(channel, "channel is null");
        final Supplier<ByteBuffer> bufferSupplier
                = () -> (ByteBuffer) ((java.nio.Buffer) ByteBuffer.allocate(1)).position(1);
        final BufferByteInputChannelAdapter adapter
                = new BufferByteInputChannelAdapter(bufferSupplier, BitIoUtils.emptySupplier());
        adapter.channel(channel);
        return adapter;
    }

    /**
     * Returns a new instance which reads bytes from specified path.
     *
     * @param path    the path from which bytes are read.
     * @param options an array of open options.
     * @return a new instance.
     */
    static ByteInput from(final Path path, final OpenOption... options) {
        Objects.requireNonNull(path, "path is null");
        Objects.requireNonNull(options, "options is null");
        return adapting(() -> {
            try {
                return FileChannel.open(path, options);
            } catch (final IOException ioe) {
                throw new RuntimeException("failed to open " + path, ioe);
            }
        });
    }

    /**
     * Returns a new instance which reads bytes from specified path.
     *
     * @param path the path from which bytes are read.
     * @return a new instance.
     * @implNote The {@code from(Path)} method invokes {@link #from(Path, OpenOption...)} method with {@code path} and
     * {@link StandardOpenOption#READ}.
     */
    static ByteInput from(final Path path) {
        return from(path, StandardOpenOption.READ);
    }

    /**
     * Creates a new instance which reads bytes from specified buffer.
     *
     * @param source the byte buffer which bytes are read.
     * @return a new instance.
     */
    public static BufferByteInput from(final ByteBuffer source) {
        Objects.requireNonNull(source, "source is null");
        final BufferByteInput instance = new BufferByteInput(BitIoUtils.emptySupplier());
        instance.source(source);
        return instance;
    }

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param sourceSupplier the source supplier.
     */
    public BufferByteInput(final Supplier<? extends ByteBuffer> sourceSupplier) {
        super(sourceSupplier);
    }

    /**
     * {@inheritDoc}
     *
     * @param source {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implNote The {@code read(ByteBuffer)} method of {@code BufferByteInput} class invokes
     * {@link ByteBuffer#get() get()} method on {@code source} and returns the result as an unsigned
     * {@value java.lang.Byte#SIZE}-bit {@code int}.
     */
    @Override
    protected int read(final ByteBuffer source) throws IOException {
        return source.get() & 0xFF;
    }

    @Override
    void source(final ByteBuffer source) {
        if (Objects.requireNonNull(source, "source is null").capacity() == 0) {
            throw new IllegalArgumentException("source.capacity is zero");
        }
        super.source(source);
    }
}
