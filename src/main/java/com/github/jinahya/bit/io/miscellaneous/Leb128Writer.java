package com.github.jinahya.bit.io.miscellaneous;

import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.IntWriter;
import com.github.jinahya.bit.io.LongWriter;

import java.io.IOException;
import java.util.Objects;

public abstract class Leb128Writer
        implements IntWriter, LongWriter {

    public static class OfUnsigned
            extends Leb128Writer {

        /**
         * Creates a new instance.
         */
        public OfUnsigned() {
            super();
        }

        @Override
        public void writeLong(final BitOutput output, long value) throws IOException {
            Objects.requireNonNull(output, "output is null");
            if (value < 0L) {
                throw new IllegalArgumentException("negative value: " + value);
            }
            while (true) {
                final int group = (int) (value & 0x7FL);
                final boolean last = (value >>= 7) == 0L;
                output.writeInt(true, 1, last ? 0 : 1);
                output.writeInt(true, 7, group);
                if (last) {
                    break;
                }
            }
        }
    }

    public static class OfSigned
            extends Leb128Writer {

        /**
         * Creates a new instance.
         */
        public OfSigned() {
            super();
        }

        @Override
        public void writeLong(final BitOutput output, long value) throws IOException {
            Objects.requireNonNull(output, "output is null");
            if (value < 0L) {
                throw new IllegalArgumentException("negative value: " + value);
            }
            while (true) {
                final int group = (int) (value & 0x7FL);
                value >>= 7;
                if ((value == 0 && (group & 0x40) == 0) || (value == -1 && (group & 0x40) != 0)) {
                    output.writeInt(true, Byte.SIZE, group);
                    return;
                }
                output.writeInt(true, Byte.SIZE, (group | 0x80));
            }
        }
    }

    protected Leb128Writer() {
        super();
    }

    @Override
    public void writeInt(BitOutput output, final int value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        if (value < 0) {
            throw new IllegalArgumentException("negative value: " + value);
        }
        writeLong(output, value);
    }
}
