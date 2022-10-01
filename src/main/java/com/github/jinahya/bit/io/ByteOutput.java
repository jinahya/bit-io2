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
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * An interface for writing bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ByteInput
 */
@FunctionalInterface
public interface ByteOutput {

    static ByteOutput of(final OutputStream stream) {
        return new StreamByteOutput(stream);
    }

    static ByteOutput of(final DataOutput output) {
        return new DataByteOutput(output);
    }

    static ByteOutput of(final RandomAccessFile file) {
        return new RandomAccessByteOutput(file);
    }

    static ByteOutput of(final ByteBuffer buffer) {
        return new BufferByteOutput(buffer);
    }

    static ByteOutput of(final WritableByteChannel channel) {
        return new ChannelByteOutput(channel);
    }

    /**
     * Writes specified <em>unsigned</em> {@value java.lang.Byte#SIZE}-bit byte value.
     *
     * @param value the <em>unsigned</em> {@value java.lang.Byte#SIZE}-bit byte value to write; between {@code 0} and
     *              {@code 255}, both inclusive.
     * @throws IOException if an I/O error occurs.
     */
    void write(int value) throws IOException;
}
