package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
abstract class ByteIoTest<O extends ByteOutput, I extends ByteInput> {

    protected abstract O newOutput(final int bytes);

    protected abstract I newInput(final byte[] bytes);

    @Test
    void writeAndRead() throws IOException {
        final int count = ThreadLocalRandom.current().nextInt(1024);
        final byte[] expected = new byte[count];
        for (int i = 0; i < count; i++) {
            expected[i] = (byte) (ThreadLocalRandom.current().nextInt() & 0xFF);
        }
        final O output = newOutput(expected.length);
        for (final byte b : expected) {
            output.write(b & 0xFF);
        }
        final I input = newInput(expected);
        final byte[] actual = new byte[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = (byte) input.read();
        }
        assertThat(actual).isEqualTo(expected);
    }
}
