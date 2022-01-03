package com.github.jinahya.bit.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

class ByteIoStreamTest
        extends ByteIoTest<StreamByteOutput, StreamByteInput> {

    @Override
    protected StreamByteOutput newOutput(final int bytes) {
        return StreamByteOutput.of(new ByteArrayOutputStream());
    }

    @Override
    protected StreamByteInput newInput(final byte[] bytes) {
        return StreamByteInput.of(new ByteArrayInputStream(bytes));
    }
}
