package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

class BitInputStreamWrapperTest {

    @Test
    void test() throws IOException {
        final int bytes = 1024;
        try (ByteInput byteInput = new StreamByteInput(() -> new ByteArrayInputStream(new byte[bytes]));
             BitInput bitInput = new BitInputAdapter(() -> byteInput);
             InputStream inputStream = BitInputStreamWrapper.of(bitInput)) {
            for (int i = 0; i < bytes; i++) {
                final int b = inputStream.read();
            }
        }
    }
}