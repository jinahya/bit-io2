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
import static com.github.jinahya.bit.io.ValueReader.readLength;
import static com.github.jinahya.bit.io.ValueWriter.writeLength;
import static java.util.Objects.requireNonNull;

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
    public static class Unsigned extends BytesAdapter {

        /**
         * Creates a new instance.
         *
         * @param lengthSize  the number of bits for the length of the array; between {@code 1} (inclusive) and {@value
         *                    java.lang.Integer#SIZE} (exclusive).
         * @param elementSize the number of bits for each element in the array; between {@code 1} (inclusive) and
         *                    {@value java.lang.Byte#SIZE} (exclusive).
         */
        public Unsigned(final int lengthSize, final int elementSize) {
            super(lengthSize, requireValidSizeForByte(true, elementSize));
        }

        @Override
        byte readByte(final BitInput input) throws IOException {
            return input.readByte(true, elementSize);
        }

        @Override
        void writeByte(final BitOutput output, byte value) throws IOException {
            output.writeByte(true, elementSize, value);
        }
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

    /**
     * Reads an array of bytes from specified input.
     *
     * @param input the input from which the value is read.
     * @return an array of bytes.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public byte[] read(final BitInput input) throws IOException {
        final int length = readLength(input, lengthSize);
        final byte[] value = new byte[length];
        for (int i = 0; i < value.length; i++) {
            value[i] = readByte(input);
        }
        return value;
    }

    byte readByte(final BitInput input) throws IOException {
        return input.readByte(elementSize);
    }

    /**
     * Writes specified array to specified output.
     *
     * @param output the output to which the array is written.
     * @param value  the array to write.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(final BitOutput output, final byte[] value) throws IOException {
        requireNonNull(value, "value is null");
        final int length = writeLength(output, lengthSize, value.length);
        for (int i = 0; i < length; i++) {
            writeByte(output, value[i]);
        }
    }

    void writeByte(final BitOutput output, final byte value) throws IOException {
        output.writeByte(elementSize, value);
    }

    private final int lengthSize;

    final int elementSize;
}
