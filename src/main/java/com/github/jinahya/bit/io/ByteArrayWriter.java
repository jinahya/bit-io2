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
import java.util.Objects;

/**
 * A value write for writing arrays of bytes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class ByteArrayWriter
        extends PrimitiveArrayWriter<byte[]> {

    static class Unsigned
            extends ByteArrayWriter {

        private static final boolean UNSIGNED_ = true;

        Unsigned(final int lengthSize, final int elementSize) {
            super(lengthSize, BitIoConstraints.requireValidSizeForByte(UNSIGNED_, elementSize));
        }

        @Override
        void writeElement(final BitOutput output, final byte value) throws IOException {
            output.writeInt(UNSIGNED_, elementSize, value);
        }
    }

    /**
     * Creates a new instance for writing an array of unsigned bytes.
     *
     * @param lengthSize  a number of bits for the length of the array.
     * @param elementSize a number of bits for each element in the array.
     * @return a new instance.
     */
    public static ByteArrayWriter unsigned(final int lengthSize, final int elementSize) {
        return new Unsigned(lengthSize, elementSize);
    }

    private static class Ascii
            extends Unsigned {

        /**
         * .
         *
         * @see <a href="https://en.wikipedia.org/wiki/ASCII#Printable_characters">Printable characters</a>
         */
        private static class Printable
                extends Ascii {

            Printable(final int lengthSize) {
                super(lengthSize);
            }

            @Override
            void writeElement(final BitOutput output, final byte value) throws IOException {
                if (value < 0x60) {
                    output.writeUnsignedInt(1, 0);
                    output.writeUnsignedInt(6, value - 0x20);
                    return;
                }
                output.writeUnsignedInt(1, 1);
                output.writeUnsignedInt(5, value - 0x60);
            }
        }

        private Ascii(final int lengthSize) {
            super(lengthSize, 7);
        }
    }

    /**
     * Creates a new instance for writing an array of ASCII bytes.
     *
     * @param lengthSize    a number of bits for the length of the array.
     * @param printableOnly a flag for printable characters only; {@code true} for printable ascii characters only;
     *                      {@code false} otherwise.
     * @return a new instance.
     */
    public static ByteArrayWriter ascii(final int lengthSize, final boolean printableOnly) {
        if (printableOnly) {
            return new Ascii.Printable(lengthSize);
        }
        return new Ascii(lengthSize);
    }

    /**
     * Creates a new instance, for writing an array of ASCII bytes, which writes an <em>unsigned</em> {@code 31}-bit int
     * value for the lenght of the array.
     *
     * @param printableOnly a flag for printable characters only; {@code true} for printable ascii characters only;
     *                      {@code false} otherwise.
     * @return a new instance.
     */
    public static ByteArrayWriter ascii31(final boolean printableOnly) {
        return ascii(Integer.SIZE - 1, printableOnly);
    }

    /**
     * A writer for writing an array of UTF-8 bytes as a compressed manner.
     */
    private static class Utf8
            extends ByteArrayWriter {

        private Utf8(final int lengthSize) {
            super(lengthSize, Byte.SIZE);
        }

        @Override
        void writeElements(final BitOutput output, final byte[] value) throws IOException {
            for (int i = 0; i < value.length; i++) {
                final int b = value[i] & 0xFF;
                if ((b & 0b0111_1111) == b) { // 0xxx_xxxx
                    output.writeUnsignedInt(2, 0b00);
                    output.writeUnsignedInt(7, b & 0xFF);
                    continue;
                }
                if ((b & 0b1101_1111) == b) { // 110x_xxxx
                    output.writeUnsignedInt(2, 0b01);
                    output.writeUnsignedInt(5, b);
                    output.writeUnsignedInt(6, value[++i] & 0xFF);
                    continue;
                }
                if ((b & 0b1110_1111) == b) { // 1110_xxxx
                    output.writeUnsignedInt(2, 0b10);
                    output.writeUnsignedInt(4, b);
                    output.writeUnsignedInt(6, value[++i] & 0xFF);
                    output.writeUnsignedInt(6, value[++i] & 0xFF);
                    continue;
                }
                assert (b & 0b1111_0111) == b;
                output.writeUnsignedInt(2, 0b11);
                output.writeUnsignedInt(3, b);
                output.writeUnsignedInt(6, value[++i] & 0xFF);
                output.writeUnsignedInt(6, value[++i] & 0xFF);
                output.writeUnsignedInt(6, value[++i] & 0xFF);
            }
        }
    }

    /**
     * Creates a new instance for writing an array of UTF-8 bytes.
     *
     * @param lengthSize a number of bits for the length of the array.
     * @return a new instance.
     */
    public static ByteArrayWriter utf8(final int lengthSize) {
        return new Utf8(lengthSize);
    }

    /**
     * Creates a new instance, for writing an array of UTF-8 bytes, which writes an <em>unsigned</em> {@code 31}-bit
     * {@code int} value for the length of the array.
     *
     * @return a new instance.
     */
    public static ByteArrayWriter utf831() {
        return utf8(Integer.SIZE - 1);
    }

    /**
     * Returns a new instance which writes an <em>unsigned</em> {@code 31}-bit int for the array length and writes
     * specified bits for each element.
     *
     * @param elementSize the number of bits for each byte.
     * @return a new instance.
     */
    static ByteArrayWriter of31(final int elementSize) {
        return new ByteArrayWriter(Integer.SIZE - 1, elementSize);
    }

    /**
     * Returns a new instance which writes an <em>unsigned</em> {@code 31}-bit int for the array length and writes a
     * <em>signed</em> {@value java.lang.Byte#SIZE} byte for each element.
     *
     * @return a new instance.
     */
    public static ByteArrayWriter of318() {
        return of31(Byte.SIZE);
    }

    /**
     * Creates a new instance.
     *
     * @param lengthSize  a number of bits for the length of the array.
     * @param elementSize a number of bits for each element in the array.
     */
    ByteArrayWriter(final int lengthSize, final int elementSize) {
        super(lengthSize);
        lengthMask = -1 >>> (Integer.SIZE - lengthSize);
        this.elementSize = BitIoConstraints.requireValidSizeForByte(false, elementSize);
    }

    @Override
    public void write(final BitOutput output, final byte[] value) throws IOException {
        Objects.requireNonNull(value, "value is null");
        final int maskedLength = lengthMask & value.length;
        if (maskedLength != value.length) {
            throw new IllegalArgumentException("invalid value.length(" + value.length + ") > " + lengthMask);
        }
        writeLength(output, value.length);
        writeElements(output, value);
    }

    void writeElements(final BitOutput output, final byte[] elements) throws IOException {
        for (final byte element : elements) {
            writeElement(output, element);
        }
    }

    void writeElement(final BitOutput output, final byte element) throws IOException {
        output.writeByte(false, elementSize, element); // signed elementSize-bit byte
    }

    private final int lengthMask;

    final int elementSize;
}
