package com.github.jinahya.bit.io;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForByte;

/**
 * A class for reading an array of unsigned bytes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class BytesAdapterUnsigned
        extends BytesAdapter {

    /**
     * Creates a new instance.
     *
     * @param lengthSize  the number of bits for the length of the array; between {@code 1} (inclusive) and {@value
     *                    Integer#SIZE} (exclusive).
     * @param elementSize the number of bits for each element in the array; between {@code 1} (inclusive) and {@value
     *                    Byte#SIZE} (exclusive).
     */
    BytesAdapterUnsigned(final int lengthSize, final int elementSize) {
        super(lengthSize, requireValidSizeForByte(true, elementSize));
    }

    @Override
    byte readByte(final BitInput input) throws IOException {
        return input.readUnsignedByte(elementSize);
    }

    @Override
    void writeByte(final BitOutput output, byte value) throws IOException {
        output.writeUnsignedByte(elementSize, value);
    }
}
