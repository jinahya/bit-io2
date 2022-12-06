package com.github.jinahya.bit.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;

class ChannelByteOutputTest
        extends AbstractByteOutputTest<ChannelByteOutput> {

    ChannelByteOutputTest() {
        super(ChannelByteOutput.class);
    }

    @Override
    protected ChannelByteOutput newInstance(final int size) throws IOException {
        return new ChannelByteOutput(Channels.newChannel(new ByteArrayOutputStream()));
    }
}
