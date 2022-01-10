package com.github.jinahya.bit.io;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

final class ByteIoUtils {

    static void clearFillAndFlip(final ByteBuffer buffer, final ReadableByteChannel channel) throws IOException {
        assert !buffer.hasRemaining();
        buffer.clear();
        while (true) {
            final int read = channel.read(buffer);
            if (read > 0) {
                break;
            }
            if (read == -1) {
                throw new EOFException("the channel reached to end-of-stream");
            }
        }
        buffer.flip();
    }

    static void flipFlushAndClear(final ByteBuffer buffer, final WritableByteChannel channel) throws IOException {
        for (buffer.flip(); buffer.hasRemaining(); ) {
            channel.write(buffer);
        }
        buffer.clear();
    }

    private ByteIoUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
