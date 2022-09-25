package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ThreadLocalRandom;

final class _White {

    static class _InputStream
            extends InputStream {

        @Override
        public int read() throws IOException {
            return ThreadLocalRandom.current().nextInt(256);
        }
    }

    static class _ReadableByteChannel
            implements ReadableByteChannel {

        @Override
        public int read(final ByteBuffer dst) throws IOException {
            final int read = ThreadLocalRandom.current().nextInt(dst.remaining() + 1);
            dst.position(dst.position() + read);
            return read;
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() throws IOException {
            // does nothing.
        }
    }

    private _White() {
        throw new AssertionError("instantiation is not allowed");
    }
}
