package com.github.jinahya.bit.io.miscellaneous;

import com.github.jinahya.bit.io.BitInput;

import java.io.IOException;
import java.util.Objects;

class MiscellaneousBitInput {

    public static int readNibble(final BitInput input, final boolean unsigned) throws IOException {
        Objects.requireNonNull(input, "input is null");
        return input.readInt(unsigned, BitIoMiscellaneousConstants.SIZE_NIBBLE);
    }

    public static int readNibbleUnsigned(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        return readNibble(input, true);
    }

    protected MiscellaneousBitInput() {
        super();
    }
}
