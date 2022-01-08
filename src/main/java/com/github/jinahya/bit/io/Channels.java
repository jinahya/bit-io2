package com.github.jinahya.bit.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

final class Channels {

    static ReadableByteChannel readableByteChannel(final AsynchronousByteChannel channel) {
        Objects.requireNonNull(channel, "channel is null");
        return new ReadableByteChannel() {
            @Override
            public int read(final ByteBuffer dst) throws IOException {
                try {
                    return channel.read(dst).get();
                } catch (InterruptedException | ExecutionException e) {
                    final Throwable cause = e.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean isOpen() {
                return channel.isOpen();
            }

            @Override
            public void close() throws IOException {
                channel.close();
            }
        };
    }

    private Channels() {
        throw new AssertionError("instantiation is not allowed");
    }
}
