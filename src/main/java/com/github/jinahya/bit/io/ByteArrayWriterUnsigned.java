package com.github.jinahya.bit.io;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForByte;

class ByteArrayWriterUnsigned
        extends ByteArrayWriter {

    ByteArrayWriterUnsigned(final int lengthSize, final int elementSize) {
        super(lengthSize, requireValidSizeForByte(true, elementSize));
    }

    @Override
    void writeElement(final BitOutput output, final byte value) throws IOException {
        output.writeUnsignedInt(elementSize, value);
    }
}
