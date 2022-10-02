package com.github.jinahya.bit.io;

class ChannelByteInputTest
        extends AbstractByteInputTest<ChannelByteInput> {

    ChannelByteInputTest() {
        super(ChannelByteInput.class);
    }

    @Override
    protected ChannelByteInput newWhiteInstance() {
        return new ChannelByteInput(new _White._ReadableByteChannel());
    }
}
