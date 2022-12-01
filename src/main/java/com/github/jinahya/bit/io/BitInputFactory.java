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

import java.io.DataInput;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

/**
 * A factory class for creating instances from various byte sources.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutputFactory
 */
public final class BitInputFactory {

    /**
     * Creates a new instance on top of specified stream.
     *
     * @param stream the stream from which bytes are read.
     * @return a new instance.
     */
    public static BitInput from(final InputStream stream) {
        Objects.requireNonNull(stream, "stream is null");
        return new ByteInputAdapter(ByteInputFactory.from(stream));
    }

    /**
     * Creates a new instance on top of specified input.
     *
     * @param input the input from which bytes are read.
     * @return a new instance.
     */
    public static BitInput from(final DataInput input) {
        Objects.requireNonNull(input, "input is null");
        return new ByteInputAdapter(ByteInputFactory.from(input));
    }

    /**
     * Creates a new instance on top of specified file.
     *
     * @param file the file from which bytes are read.
     * @return a new instance.
     */
    public static BitInput from(final RandomAccessFile file) {
        Objects.requireNonNull(file, "file is null");
        return new ByteInputAdapter(ByteInputFactory.from(file));
    }

    /**
     * Creates a new instance on top of specified buffer.
     *
     * @param buffer the buffer from which bytes are read.
     * @return a new instance.
     */
    public static BitInput from(final ByteBuffer buffer) {
        Objects.requireNonNull(buffer, "buffer is null");
        return new ByteInputAdapter(ByteInputFactory.from(buffer));
    }

    /**
     * Creates a new instance on top of specified channel.
     *
     * @param channel the channel from which bytes are read.
     * @return a new instance.
     */
    public static BitInput from(final ReadableByteChannel channel) {
        Objects.requireNonNull(channel, "channel is null");
        return new ByteInputAdapter(ByteInputFactory.from(channel));
    }

    private BitInputFactory() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
