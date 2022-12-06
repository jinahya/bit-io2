package com.github.jinahya.bit.io;

import java.io.IOException;
import java.nio.ByteBuffer;

class BufferByteOutputTest
        extends AbstractByteOutputTest<BufferByteOutput> {

    BufferByteOutputTest() {
        super(BufferByteOutput.class);
    }

    @Override
    protected BufferByteOutput newInstance(final int size) throws IOException {
        return new BufferByteOutput(ByteBuffer.allocate(size));
    }
}
