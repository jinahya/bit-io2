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

/**
 * A reader for reading arrays of bytes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class ByteArrayReader
        extends PrimitiveArrayReader<byte[]> {

    static class Unsigned
            extends ByteArrayReader {

        private static final boolean UNSIGNED_ = true;

        Unsigned(final int lengthSize, final int elementSize) {
            super(lengthSize, BitIoConstraints.requireValidSizeForByte(UNSIGNED_, elementSize));
        }

        @Override
        byte readElement(final BitInput input) throws IOException {
            return input.readByte(UNSIGNED_, elementSize);
        }
    }

    /**
     * Creates a new instance for reading an array of unsigned bytes.
     *
     * @param lengthSize  a number of bits for the length of the array.
     * @param elementSize a number of bits for each element in the array.
     * @return a new instance.
     */
    public static ByteArrayReader unsigned(final int lengthSize, final int elementSize) {
        return new Unsigned(lengthSize, elementSize);
    }

    private static class Ascii
            extends Unsigned {

        /**
         * An ASCII bytes reader for printable characters.
         *
         * @see <a href="https://en.wikipedia.org/wiki/ASCII#Printable_characters">Printable characters</a>
         */
        private static class Printable
                extends Ascii {

            Printable(final int lengthSize) {
                super(lengthSize);
            }

            @Override
            byte readElement(final BitInput input) throws IOException {
                final int e = input.readUnsignedInt(1);
                if (e == 0b0) {
                    return (byte) (input.readUnsignedInt(6) + 0x20);
                }
                return (byte) (input.readUnsignedInt(5) + 0x60);
            }
        }

        private Ascii(final int lengthSize) {
            super(lengthSize, 7);
        }
    }

    /**
     * Creates a new instance for reading an array of ASCII bytes.
     *
     * @param lengthSize    a number of bits for the length of an array.
     * @param printableOnly a flag for printable characters only; {@code true} for printable characters only;
     *                      {@code false} otherwise.
     * @return a new instance.
     */
    public static ByteArrayReader ascii(final int lengthSize, final boolean printableOnly) {
        if (printableOnly) {
            return new Ascii.Printable(lengthSize);
        }
        return new Ascii(lengthSize);
    }

    /**
     * Creates a new instance, for reading an array of ASCII bytes, which read an <em>unsigned</em> {@code 31}-bit int
     * value for the length of the array.
     *
     * @param printableOnly a flag for printable characters only; {@code true} for printable characters only;
     *                      {@code false} otherwise.
     * @return a new instance.
     */
    public static ByteArrayReader ascii31(final boolean printableOnly) {
        return ascii(Integer.SIZE - 1, printableOnly);
    }

    /**
     * A reader for reading an array of UTF-8 bytes as a compressed manner.
     */
    private static class Utf8
            extends ByteArrayReader {

        private Utf8(final int lengthSize) {
            super(lengthSize, Byte.SIZE);
        }

        @Override
        void readElements(final BitInput input, final byte[] value) throws IOException {
            for (int i = 0; i < value.length; i++) {
                switch (input.readUnsignedInt(2)) {
                    case 0b00:
                        value[i] = (byte) input.readUnsignedInt(7);
                        break;
                    case 0b01:
                        value[i] = (byte) (0b1100_0000 | input.readUnsignedInt(5));
                        value[++i] = (byte) (0b1000_0000 | input.readUnsignedInt(6));
                        break;
                    case 0b10:
                        value[i] = (byte) (0b1110_0000 | input.readUnsignedInt(4));
                        value[++i] = (byte) (0b1000_0000 | input.readUnsignedInt(6));
                        value[++i] = (byte) (0b1000_0000 | input.readUnsignedInt(6));
                        break;
                    default: // 0b11
                        value[i] = (byte) (0b1111_0000 | input.readUnsignedInt(3));
                        value[++i] = (byte) (0b1000_0000 | input.readUnsignedInt(6));
                        value[++i] = (byte) (0b1000_0000 | input.readUnsignedInt(6));
                        value[++i] = (byte) (0b1000_0000 | input.readUnsignedInt(6));
                        break;
                }
            }
        }
    }

    /**
     * Creates a new instance for reading an array of UTF-8 bytes in a compressed-manner.
     *
     * @param lengthSize a number of bits for the length of an array.
     * @return a new instance.
     */
    public static ByteArrayReader utf8(final int lengthSize) {
        return new Utf8(lengthSize);
    }

    /**
     * Creates a new instance, for reading an array of UTF-8 bytes in a compressed-manner, which reads an
     * <em>unsigned</em> {@code 31}-bit {@code int} value for length of the array.
     *
     * @return a new instance.
     */
    public static ByteArrayReader utf831() {
        return utf8(Integer.SIZE - 1);
    }

    /**
     * Returns a new instance which reads an <em>unsigned</em> {@code 31}-bit int value for the lenght of the array, and
     * reads specified number of bits for each element.
     *
     * @param elementSize the number of bits for each byte.
     * @return a new instance.
     */
    static ByteArrayReader of31(final int elementSize) {
        return new ByteArrayReader(Integer.SIZE - 1, elementSize);
    }

    /**
     * Returns a new instance which reads an <em>unsigned</em> {@code 31}-bit int value for the array length, and reads
     * {@value java.lang.Byte#SIZE} byte for each element.
     *
     * @return a new instance.
     */
    public static ByteArrayReader of318() {
        return of31(Byte.SIZE);
    }

    /**
     * Creates a new instance.
     *
     * @param lengthSize  a number of bits for the length of the array.
     * @param elementSize a number of bits for each element in the array.
     */
    ByteArrayReader(final int lengthSize, final int elementSize) {
        super(lengthSize);
        this.elementSize = BitIoConstraints.requireValidSizeForByte(false, elementSize);
    }

    @Override
    public byte[] read(final BitInput input) throws IOException {
        final int length = readLength(input);
        final byte[] value = new byte[length];
        readElements(input, value);
        return value;
    }

    void readElements(final BitInput input, final byte[] elements) throws IOException {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = readElement(input);
        }
    }

    byte readElement(final BitInput input) throws IOException {
        return input.readByte(false, elementSize); // signed elementSize-bit byte
    }

    final int elementSize;
}
