package com.github.jinahya.bit.io;

import java.io.DataOutput;

/**
 * A class for testing {@link DataByteOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DataByteInputTest
 */
class DataByteOutputTest extends ByteOutputAdapterTest<DataByteOutput, DataOutput> {

    /**
     * Creates a new instance.
     */
    DataByteOutputTest() {
        super(DataByteOutput.class, DataOutput.class);
    }
}
