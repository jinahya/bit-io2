package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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
import java.io.InputStream;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A byte input reads bytes from an array of bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ArrayByteOutput
 * @deprecated Use {@link BufferByteInput} with a <a href="https://bit.ly/2WgUbS4">backing array</a>.
 */
@Deprecated // forRemoval = true
public class ArrayByteInput extends ByteInputAdapter<byte[]> {

    private static class StreamAdapter extends ArrayByteInput {

        StreamAdapter(final Supplier<? extends InputStream> streamSupplier) {
            super(() -> new byte[1]);
            this.streamSupplier = requireNonNull(streamSupplier, "streamSupplier is null");
        }

        @Override
        protected int read(final byte[] source) throws IOException {
            if (stream().read(source) == -1) {
                throw new EOFException("stream has reached to end-of-stream");
            }
            index = 0;
            return super.read(source);
        }

        InputStream stream() {
            if (stream == null) {
                stream = streamSupplier.get();
            }
            return stream;
        }

        private final Supplier<? extends InputStream> streamSupplier;

        private transient InputStream stream;
    }

    /**
     * Creates a new instance with specified stream supplier.
     *
     * @param streamSupplier the stream supplier.
     * @return a new instance.
     */
    public static ArrayByteInput from(final Supplier<? extends InputStream> streamSupplier) {
        return new StreamAdapter(streamSupplier);
    }

    /**
     * Creates a new instance reads bytes directly from specified input stream.
     *
     * @param stream the input from which bytes are read.
     * @return a new instance.
     */
    public static ArrayByteInput from(final InputStream stream) {
        if (stream == null) {
            throw new NullPointerException("stream is null");
        }
        return new StreamAdapter(nullSourceSupplier()) {
            @Override
            InputStream stream() {
                return stream;
            }
        };
    }

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param sourceSupplier the source supplier.
     */
    public ArrayByteInput(final Supplier<byte[]> sourceSupplier) {
        super(sourceSupplier);
    }

    /**
     * {@inheritDoc} The {@code read(byte[])} method of {@code ArrayByteInput} class returns the byte on {@code source}
     * at {@link #index} as an unsigned {@code int} and increments the value of {@link #index} by {@code 1}.
     *
     * @param source the array of byte on which the byte is read.
     * @return {@inheritDoc}
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected int read(final byte[] source) throws IOException {
        return source[index++] & 0xFF;
    }

    /**
     * The next index in the {@code source} to read the octet.
     */
    protected int index;
}
