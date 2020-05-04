package com.github.jinahya.bit.io;

import java.io.DataInput;

class DataByteInputTest extends ByteInputAdapterTest<DataByteInput, DataInput> {

    DataByteInputTest() {
        super(DataByteInput.class, DataInput.class);
    }
}
