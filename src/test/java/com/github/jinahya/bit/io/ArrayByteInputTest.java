package com.github.jinahya.bit.io;

import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;

class ArrayByteInputTest extends ByteInputAdapterTest<ArrayByteInput, byte[]> {

    static ArrayByteInput white() {
        return new ArrayByteInput(() -> null) {
            @Override
            public int read(final byte[] source) throws IOException {
                assert source == null;
                return current().nextInt(255) + 1;
            }
        };
    }

    ArrayByteInputTest() {
        super(ArrayByteInput.class, byte[].class);
    }
}
