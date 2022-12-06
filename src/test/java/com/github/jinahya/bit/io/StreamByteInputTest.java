package com.github.jinahya.bit.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class StreamByteInputTest
        extends AbstractByteInputTest<StreamByteInput> {

    StreamByteInputTest() {
        super(StreamByteInput.class);
    }

    @Override
    protected StreamByteInput newInstance(final int size) throws IOException {
        return new StreamByteInput(new ByteArrayInputStream(new byte[size]));
    }
}
