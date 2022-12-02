package com.github.jinahya.bit.io.miscellaneous;

import com.github.jinahya.bit.io.BitOutput;

import java.io.IOException;
import java.util.Objects;

class MiscellaneousBitOutput {

    public static void writeNibble(final BitOutput output, final boolean unsigned, final int value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        output.writeInt(unsigned, BitIoMiscellaneousConstants.SIZE_NIBBLE, value);
    }

    public static void writeNibbleUnsigned(final BitOutput output, final int value) throws IOException {
        writeNibble(output, true, value);
    }

    protected MiscellaneousBitOutput() {
        super();
    }
}
