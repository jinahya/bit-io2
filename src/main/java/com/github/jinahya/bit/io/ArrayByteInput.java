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
 * @deprecated Use {@link BufferByteInput} with a <a href="https://docs.oracle.com/javase/7/docs/api/java/nio/ByteBuffer.html#wrap(byte[])">backing
 * array</a>.
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
            while (true) {
                final int read = stream().read(source);
                if (read == -1) {
                    throw new EOFException("stream has reached to end-of-stream");
                }
                if (read == 1) {
                    index = 0;
                    break;
                }
            }
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

    public static ByteInput of(final Supplier<? extends InputStream> streamSupplier) {
        return new StreamAdapter(streamSupplier);
    }

    public static ByteInput of(final InputStream stream) {
        if (stream == null) {
            throw new NullPointerException("stream is null");
        }
        return new StreamAdapter(() -> null) {
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
