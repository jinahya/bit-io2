package com.github.jinahya.bit.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

class ByteIoDataTest
        extends ByteIoTest<DataByteOutput, DataByteInput> {

    @Override
    protected DataByteOutput newOutput(final int bytes) {
        return DataByteOutput.of(new DataOutputStream(new ByteArrayOutputStream()));
    }

    @Override
    protected DataByteInput newInput(final byte[] bytes) {
        return DataByteInput.of(new DataInputStream(new ByteArrayInputStream(bytes)));
    }
}
