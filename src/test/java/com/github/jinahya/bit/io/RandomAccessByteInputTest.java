package com.github.jinahya.bit.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

class RandomAccessByteInputTest
        extends AbstractByteInputTest<RandomAccessByteInput> {

    RandomAccessByteInputTest() {
        super(RandomAccessByteInput.class);
    }

    @Override
    protected RandomAccessByteInput newInstance(final int size) throws IOException {
        final var file = tempFile();
        try (var stream = new FileOutputStream(file)) {
            stream.write(new byte[size]);
            stream.flush();
        }
        return new RandomAccessByteInput(new RandomAccessFile(file, "r"));
    }
}
