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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForInt;

/**
 * A value adapter for reading/writing an array of bytes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class BytesAdapter implements ValueAdapter<byte[]> {

    /**
     * An extended class for unsigned bytes.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    private static class UnsignedBytesAdapter extends BytesAdapter {

        /**
         * Creates a new instance.
         *
         * @param lengthSize  the number of bits for the length of the array; between {@code 1} (inclusive) and {@value
         *                    java.lang.Integer#SIZE} (exclusive).
         * @param elementSize the number of bits for each element in the array; between {@code 1} (inclusive) and
         *                    {@value java.lang.Byte#SIZE} (exclusive).
         */
        private UnsignedBytesAdapter(final int lengthSize, final int elementSize) {
            super(lengthSize, requireValidSizeForByte(true, elementSize));
        }

        @Override
        public void write(final BitOutput output, final byte[] value) throws IOException {
            final int length = writeLength(output, value);
            for (int i = 0; i < length; i++) {
                output.writeByte(true, elementSize, value[i]);
            }
        }

        @Override
        public byte[] read(final BitInput input) throws IOException {
            final int length = readLength(input);
            final byte[] value = new byte[length];
            for (int i = 0; i < value.length; i++) {
                value[i] = input.readByte(true, elementSize);
            }
            return value;
        }
    }

    /**
     * Creates a new instance for reading/writing an array of unsigned bytes.
     *
     * @param lengthSize  the number of bits for the length of the array; between {@code 1} (inclusive) and {@value
     *                    java.lang.Integer#SIZE} (exclusive).
     * @param elementSize the number of bits for each element in the array; between {@code 1} (inclusive) and {@value
     *                    java.lang.Byte#SIZE} (exclusive).
     * @return a new instance.
     */
    public static BytesAdapter unsigned(final int lengthSize, final int elementSize) {
        return new UnsignedBytesAdapter(lengthSize, elementSize);
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param lengthSize  the number of bits for the length of the array; between {@code 1} (inclusive) and {@value
     *                    java.lang.Integer#SIZE} (exclusive).
     * @param elementSize the number of bits for each element in the array; between {@code 1} and {@value
     *                    java.lang.Byte#SIZE}, both inclusive.
     */
    public BytesAdapter(final int lengthSize, final int elementSize) {
        super();
        this.lengthSize = requireValidSizeForInt(true, lengthSize);
        this.elementSize = requireValidSizeForByte(false, elementSize);
    }

    final int writeLength(final BitOutput output, final byte[] value) throws IOException {
        final int length = value.length & ((1 << lengthSize) - 1);
        output.writeUnsignedInt(lengthSize, length);
        return length;
    }

    @Override
    public void write(final BitOutput output, final byte[] value) throws IOException {
        final int length = writeLength(output, value);
        for (int i = 0; i < length; i++) {
            output.writeByte(elementSize, value[i]);
        }
    }

    final int readLength(final BitInput input) throws IOException {
        return input.readUnsignedInt(lengthSize);
    }

    @Override
    public byte[] read(final BitInput input) throws IOException {
        final int length = readLength(input);
        final byte[] value = new byte[length];
        for (int i = 0; i < value.length; i++) {
            value[i] = input.readByte(elementSize);
        }
        return value;
    }

    private final int lengthSize;

    final int elementSize;
}
