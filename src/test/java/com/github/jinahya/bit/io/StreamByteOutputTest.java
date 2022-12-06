package com.github.jinahya.bit.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class StreamByteOutputTest
        extends AbstractByteOutputTest<StreamByteOutput> {

    StreamByteOutputTest() {
        super(StreamByteOutput.class);
    }

    @Override
    protected StreamByteOutput newInstance(final int size) throws IOException {
        return new StreamByteOutput(new ByteArrayOutputStream(size));
    }
}
