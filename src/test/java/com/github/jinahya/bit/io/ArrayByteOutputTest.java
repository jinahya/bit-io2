package com.github.jinahya.bit.io;

import java.io.IOException;

class ArrayByteOutputTest extends ByteOutputAdapterTest<ArrayByteOutput, byte[]> {

    static ArrayByteOutput black() {
        return new ArrayByteOutput(() -> null) {
            @Override
            public void write(byte[] target, int value) throws IOException {
                assert target == null;
                // empty
            }
        };
    }

    ArrayByteOutputTest() {
        super(ArrayByteOutput.class, byte[].class);
    }
}
