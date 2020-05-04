package com.github.jinahya.bit.io;

import java.io.DataInput;

/**
 * A class for testing {@link DataByteInput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DataByteOutputTest
 */
class DataByteInputTest extends ByteInputAdapterTest<DataByteInput, DataInput> {

    /**
     * Creates a new instance.
     */
    DataByteInputTest() {
        super(DataByteInput.class, DataInput.class);
    }
}
