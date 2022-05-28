package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.function.Supplier;

public class BitOutputStreamWrapper
        extends OutputStream {

    public static OutputStream of(final BitOutput bitOutput) {
        Objects.requireNonNull(bitOutput, "bitOutput is null");
        return new BitOutputStreamWrapper(() -> bitOutput);
    }

    public BitOutputStreamWrapper(final Supplier<? extends BitOutput> supplier) {
        super();
        this.supplier = Objects.requireNonNull(supplier, "supplier is null");
    }

    @Override
    public void write(final int b) throws IOException {
        wrapped(true).writeByte8((byte) b);
    }

    @Override
    public void flush() throws IOException {
        super.flush(); // does nothing
        final BitOutput result = wrapped(false);
        if (result != null) {
            result.flush();
        }
    }

    @Override
    public void close() throws IOException {
        super.close(); // does nothing
        final BitOutput result = wrapped(false);
        if (result != null) {
            result.close();
        }
    }

    protected BitOutput wrapped(final boolean initialize) {
        BitOutput result = wrapped;
        if (result != null) {
            return result;
        }
        if (!initialize) {
            return null;
        }
        synchronized (this) {
            if (wrapped == null) {
                wrapped = Objects.requireNonNull(supplier.get(), "supplier returned null");
            }
            return wrapped;
        }
    }

    private final Supplier<? extends BitOutput> supplier;

    private volatile BitOutput wrapped;
}
