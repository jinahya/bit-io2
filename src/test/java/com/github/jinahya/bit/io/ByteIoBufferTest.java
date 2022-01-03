package com.github.jinahya.bit.io;

import java.nio.ByteBuffer;

class ByteIoBufferTest
        extends ByteIoTest<BufferByteOutput, BufferByteInput> {

    @Override
    protected BufferByteOutput newOutput(final int bytes) {
        return BufferByteOutput.of(ByteBuffer.allocate(bytes));
    }

    @Override
    protected BufferByteInput newInput(final byte[] bytes) {
        return BufferByteInput.of(ByteBuffer.wrap(bytes));
    }
}
