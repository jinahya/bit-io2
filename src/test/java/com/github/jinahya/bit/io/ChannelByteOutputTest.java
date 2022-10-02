package com.github.jinahya.bit.io;

class ChannelByteOutputTest extends AbstractByteOutputTest<ChannelByteOutput>{

    ChannelByteOutputTest() {
        super(ChannelByteOutput.class);
    }

    @Override
    protected ChannelByteOutput newBlackInstance() {
        return new ChannelByteOutput(new _Black._WritableByteChannel());
    }
}
