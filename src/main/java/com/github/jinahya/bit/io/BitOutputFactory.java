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

import java.io.DataOutput;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * A factory class for creating instances from various byte sources.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInputFactory
 */
public final class BitOutputFactory {

    /**
     * Creates a new instance on top of specified stream.
     *
     * @param stream the stream to which bytes are written.
     * @return a new instance.
     */
    public static BitOutput from(final OutputStream stream) {
        return new ByteOutputAdapter(ByteOutputFactory.from(stream));
    }

    /**
     * Creates a new instance on top of specified output.
     *
     * @param output the output to which bytes are written.
     * @return a new instance.
     */
    public static BitOutput from(final DataOutput output) {
        return new ByteOutputAdapter(ByteOutputFactory.from(output));
    }

    /**
     * Creates a new instance on top of specified file.
     *
     * @param file the file to which bytes are written.
     * @return a new instance.
     */
    public static BitOutput from(final RandomAccessFile file) {
        return new ByteOutputAdapter(ByteOutputFactory.from(file));
    }

    /**
     * Creates a new instance on top of specified buffer.
     *
     * @param buffer the buffer to which bytes are written.
     * @return a new instance.
     */
    public static BitOutput from(final ByteBuffer buffer) {
        return new ByteOutputAdapter(ByteOutputFactory.from(buffer));
    }

    /**
     * Creates a new instance on top of specified channel.
     *
     * @param channel the channel to which bytes are written.
     * @return a new instance.
     */
    public static BitOutput from(final WritableByteChannel channel) {
        return new ByteOutputAdapter(ByteOutputFactory.from(channel));
    }

    private BitOutputFactory() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
