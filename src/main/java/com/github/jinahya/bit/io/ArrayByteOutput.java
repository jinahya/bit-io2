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

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A byte output writes byte to an array of bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ArrayByteInput
 * @deprecated Use {@link BufferByteOutput} with a <a href="https://bit.ly/2WgUbS4">backing array</a>.
 */
@Deprecated // forRemoval = true
public class ArrayByteOutput extends ByteOutputAdapter<byte[]> {

    private static class StreamAdapter extends ArrayByteOutput {

        StreamAdapter(final Supplier<? extends OutputStream> streamSupplier) {
            super(() -> new byte[1]);
            this.streamSupplier = requireNonNull(streamSupplier, "streamSupplier is null");
        }

        @Override
        protected void write(final byte[] target, final int value) throws IOException {
            super.write(target, value);
            stream().write(target);
            index = 0;
        }

        OutputStream stream() {
            if (stream == null) {
                stream = streamSupplier.get();
            }
            return stream;
        }

        private final Supplier<? extends OutputStream> streamSupplier;

        private transient OutputStream stream;
    }

    public static ArrayByteOutput from(final Supplier<? extends OutputStream> streamSupplier) {
        return new StreamAdapter(streamSupplier);
    }

    public static ArrayByteOutput from(final OutputStream stream) {
        if (stream == null) {
            throw new NullPointerException("stream is null");
        }
        return new StreamAdapter(nullTargetSupplier()) {
            @Override
            OutputStream stream() {
                return stream;
            }
        };
    }

    /**
     * Creates a new instance which writes bytes directly to specified target.
     *
     * @param target the target to which bytes are written.
     * @return a new instance.
     */
    public static ArrayByteOutput from(final byte[] target) {
        return new ArrayByteOutput(() -> null) {
            @Override
            byte[] target() {
                return target;
            }
        };
    }

    /**
     * Creates a new instance with specified target supplier.
     *
     * @param targetSupplier the target supplier.
     */
    public ArrayByteOutput(final Supplier<byte[]> targetSupplier) {
        super(targetSupplier);
    }

    /**
     * {@inheritDoc} The {@code write(byte[], int)} method of {@code ArrayByteOutput} class sets the {@code value} on
     * {@code target} at {@link #index} and increments the value of {@link #index} by {@code 1}.
     *
     * @param target the array of byte on which the value is set.
     * @param value  {@inheritDoc}
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void write(final byte[] target, final int value) throws IOException {
        target[index++] = (byte) value;
    }

    /**
     * The next index in the {@code target} on which the byte is written.
     */
    protected int index;
}
