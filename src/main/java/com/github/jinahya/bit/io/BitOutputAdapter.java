package com.github.jinahya.bit.io;

import java.io.IOException;
import java.util.function.Supplier;

import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_BYTE;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeUnsigned8;
import static java.util.Objects.requireNonNull;

public class BitOutputAdapter implements BitOutput {

    public BitOutputAdapter(final Supplier<? extends ByteOutput> outputSupplier) {
        super();
        this.outputSupplier = requireNonNull(outputSupplier, "byteOutputSupplier is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void writeInt(final boolean unsigned, int size, int value) throws IOException {
        requireValidSizeInt(unsigned, size);
        if (!unsigned) {
            writeInt(true, 1, value < 0 ? 1 : 0);
            size--;
            if (size > 0) {
                writeInt(true, size, value);
            }
            return;
        }
        assert unsigned;
        final int quotient = size >> SIZE_EXPONENT_BYTE;
        final int remainder = size & (Byte.SIZE - 1);
        if (remainder > 0) {
            unsigned8(remainder, value >> (quotient << SIZE_EXPONENT_BYTE));
        }
        for (int i = Byte.SIZE * (quotient - 1); i >= 0; i -= Byte.SIZE) {
            unsigned8(Byte.SIZE, value >> i);
        }
    }

    @Override
    public long align(int bytes) throws IOException {
        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }
        long bits = 0; // the number of bits to pad
        if (available < Byte.SIZE) {
            bits += available;
            writeInt(true, available, 0x00);
        }
        assert available == Byte.SIZE;
        for (; count % bytes > 0; bits += Byte.SIZE) {
            writeInt(true, Byte.SIZE, 0x00);
        }
        assert count % bytes == 0L;
        return bits;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes an unsigned {@code int} value of specified bit size which is, in maximum, {@value java.lang.Byte#SIZE}.
     *
     * @param size  the number of lower bits to write; between {@code 1} and {@value java.lang.Byte#SIZE}, both
     *              inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInputAdapter#unsigned8(int)
     */
    void unsigned8(final int size, int value) throws IOException {
        requireValidSizeUnsigned8(size);
        final int required = size - available;
        if (required > 0) {
            unsigned8(available, value >> required);
            unsigned8(required, value);
            return;
        }
        octet <<= size;
        octet |= (value & ((1 << size) - 1));
        available -= size;
        if (available == 0) {
            assert octet >= 0 && octet < 256;
            output().write(octet);
            count++;
            octet = 0x00;
            available = Byte.SIZE;
        }
    }

    ByteOutput output() {
        if (output == null) {
            output = outputSupplier.get();
        }
        return output;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final Supplier<? extends ByteOutput> outputSupplier;

    private transient ByteOutput output;


    /**
     * The current octet.
     */
    private int octet;

    /**
     * The number of available bits in {@link #octet} for writing.
     */
    private int available = Byte.SIZE;

    /**
     * The number of bytes written so far.
     */
    private long count;
}
