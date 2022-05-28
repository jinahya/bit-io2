package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
class BitOutputStreamWrapperTest {

    @Test
    void test() throws IOException {
        final int bytes = 1024;
        try (ByteOutput byteOutput = new StreamByteOutput(ByteArrayOutputStream::new);
             BitOutput bitOutput = new BitOutputAdapter(() -> byteOutput);
             OutputStream outputStream = BitOutputStreamWrapper.of(bitOutput)) {
            for (int i = 0; i < bytes; i++) {
                outputStream.write(0);
            }
        }
    }
}