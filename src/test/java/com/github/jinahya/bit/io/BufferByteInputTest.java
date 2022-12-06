package com.github.jinahya.bit.io;

import java.io.IOException;
import java.nio.ByteBuffer;

class BufferByteInputTest
        extends AbstractByteInputTest<BufferByteInput> {

    BufferByteInputTest() {
        super(BufferByteInput.class);
    }

    @Override
    protected BufferByteInput newInstance(final int size) throws IOException {
        return new BufferByteInput(ByteBuffer.allocate(size));
    }
}
