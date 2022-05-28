package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Supplier;

public class BitInputStreamWrapper
        extends InputStream {

    public static InputStream of(final BitInput bitInput) {
        Objects.requireNonNull(bitInput, "bitInput is null");
        return new BitInputStreamWrapper(() -> bitInput);
    }

    public BitInputStreamWrapper(final Supplier<? extends BitInput> supplier) {
        super();
        this.supplier = Objects.requireNonNull(supplier, "supplier is null");
    }

    @Override
    public int read() throws IOException {
        return wrapped(true).readByte8() & 0xFF;
    }

    @Override
    public void close() throws IOException {
        super.close(); // does nothing
        final BitInput result = wrapped(false);
        if (result != null) {
            result.close();
        }
    }

    protected BitInput wrapped(final boolean initialize) {
        BitInput result = wrapped;
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

    private final Supplier<? extends BitInput> supplier;

    private volatile BitInput wrapped;
}
