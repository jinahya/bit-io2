package com.github.jinahya.bit.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.channels.Channels;

class ChannelByteInputTest
        extends AbstractByteInputTest<ChannelByteInput> {

    ChannelByteInputTest() {
        super(ChannelByteInput.class);
    }

    @Override
    protected ChannelByteInput newInstance(final int size) throws IOException {
        return new ChannelByteInput(Channels.newChannel(new ByteArrayInputStream(new byte[size])));
    }
}
