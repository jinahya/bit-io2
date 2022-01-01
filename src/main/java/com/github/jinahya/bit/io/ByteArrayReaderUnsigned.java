package com.github.jinahya.bit.io;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForByte;

class ByteArrayReaderUnsigned
        extends ByteArrayReader {

    ByteArrayReaderUnsigned(final int lengthSize, final int elementSize) {
        super(lengthSize, requireValidSizeForByte(true, elementSize));
    }

    @Override
    byte readElement(final BitInput input) throws IOException {
        return input.readUnsignedByte(elementSize);
    }
}
