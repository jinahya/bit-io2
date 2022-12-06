package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.RandomAccessFile;

class RandomAccessByteOutputTest
        extends AbstractByteOutputTest<RandomAccessByteOutput> {

    RandomAccessByteOutputTest() {
        super(RandomAccessByteOutput.class);
    }

    @Override
    protected RandomAccessByteOutput newInstance(final int size) throws IOException {
        final var file = tempFile();
        return new RandomAccessByteOutput(new RandomAccessFile(file, "rw"));
    }
}
