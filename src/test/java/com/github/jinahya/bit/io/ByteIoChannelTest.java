package com.github.jinahya.bit.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;

class ByteIoChannelTest
        extends ByteIoTest<BufferByteOutput, BufferByteInput> {

    @Override
    protected BufferByteOutput newOutput(final int bytes) {
        return BufferByteOutput.adapting(Channels.newChannel(new ByteArrayOutputStream()));
    }

    @Override
    protected BufferByteInput newInput(final byte[] bytes) {
        return BufferByteInput.adapting(Channels.newChannel(new ByteArrayInputStream(bytes)));
    }
}
