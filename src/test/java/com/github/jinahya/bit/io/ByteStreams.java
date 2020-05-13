package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.LongAdder;

import static java.util.concurrent.ThreadLocalRandom.current;

final class ByteStreams {

    static InputStream white(final long bytes) {
        if (bytes < -1) {
            throw new IllegalArgumentException("bytes(" + bytes + ") < -1");
        }
        final LongAdder adder = new LongAdder();
        return new InputStream() {
            @Override
            public int read() throws IOException {
                if (bytes != -1L && adder.sum() >= bytes) {
                    return -1;
                }
                adder.add(1L);
                return current().nextInt(0, 256);
            }
        };
    }

    static OutputStream black(final long bytes) {
        if (bytes < -1L) {
            throw new IllegalArgumentException("bytes(" + bytes + ") < -1");
        }
        final LongAdder adder = new LongAdder();
        return new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                if (bytes != -1L && adder.sum() >= bytes) {
                    throw new IOException("can't write anymore");
                }
                adder.add(1L);
            }
        };
    }

    private ByteStreams() {
        throw new AssertionError("instantiation is not allowed");
    }
}
