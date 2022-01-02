package com.github.jinahya.bit.io;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * An extended class for adapting readable byte channels.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class BufferByteInputChannelAdapter
        extends BufferByteInput {

    /**
     * Creates a new instance with specified arguments.
     *
     * @param sourceSupplier  a supplier for a byte buffer.
     * @param channelSupplier a supplier for a readable byte channel from which the {@code buffer} is filled.
     */
    BufferByteInputChannelAdapter(final Supplier<? extends ByteBuffer> sourceSupplier,
                                  final Supplier<? extends ReadableByteChannel> channelSupplier) {
        super(sourceSupplier);
        this.channelSupplier = Objects.requireNonNull(channelSupplier, "channelSupplier is null");
    }

    @Override
    public void close() throws IOException {
        super.close();
        final ReadableByteChannel channel = channel(false);
        if (channel != null) {
            channel.close();
        }
    }

    @Override
    protected int read(final ByteBuffer source) throws IOException {
        while (!source.hasRemaining()) {
            source.clear(); // position -> zero, limit -> capacity
            while (source.position() == 0) {
                if (channel(true).read(source) == -1) {
                    throw new EOFException("channel has reached end-of-stream");
                }
            }
            source.flip(); // limit -> position, position -> zero
        }
        return super.read(source);
    }

    private ReadableByteChannel channel(final boolean get) {
        if (get) {
            if (channel(false) == null) {
                channel(channelSupplier.get());
            }
            return channel(false);
        }
        return channel;
    }

    private void channel(final ReadableByteChannel channel) {
        if (channel(false) != null) {
            throw new IllegalStateException("channel already has been supplied");
        }
        this.channel = Objects.requireNonNull(channel, "channel is null");
    }

    private final Supplier<? extends ReadableByteChannel> channelSupplier;

    private ReadableByteChannel channel;
}
