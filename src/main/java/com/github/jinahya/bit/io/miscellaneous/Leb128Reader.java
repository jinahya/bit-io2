package com.github.jinahya.bit.io.miscellaneous;

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.IntReader;
import com.github.jinahya.bit.io.LongReader;

import java.io.IOException;
import java.util.Objects;

public abstract class Leb128Reader
        implements IntReader, LongReader {

    public static class OfUnsigned
            extends Leb128Reader {

        /**
         * Creates a new instance.
         */
        public OfUnsigned() {
            super();
        }

        @Override
        public long readLong(final BitInput input) throws IOException {
            Objects.requireNonNull(input, "input is null");
            long value = 0L;
            for (int shift = 0; ; shift += 7) {
                final int group = input.readInt(true, Byte.SIZE);
                value |= ((group & 0x7FL) << shift);
                if ((group & 0x80) == 0) {
                    break;
                }
            }
            return value;
        }
    }

    protected Leb128Reader() {
        super();
    }

    @Override
    public int readInt(BitInput input) throws IOException {
        return Math.toIntExact(readLong(input));
    }
}
