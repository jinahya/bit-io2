package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ThreadLocalRandom;

final class _Black {

    static class _OutputStream
            extends OutputStream {

        @Override
        public void write(final int b) throws IOException {
            // does nothing.
        }
    }

    static class _ReadableByteChannel
            implements ReadableByteChannel {

        @Override
        public int read(final ByteBuffer dst) throws IOException {
            final var written = ThreadLocalRandom.current().nextInt(dst.remaining() + 1);
            dst.position(dst.position() + written);
            return written;
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() throws IOException {
            // does nothing
        }
    }

    private _Black() {
        throw new AssertionError("instantiation is not allowed");
    }
}
