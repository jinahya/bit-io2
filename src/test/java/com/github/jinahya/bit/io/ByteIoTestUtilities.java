package com.github.jinahya.bit.io;

import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

final class ByteIoTestUtilities {

    static <R> R wr(final Supplier<? extends ByteOutput> outputSupplier,
                    final Supplier<? extends ByteInput> inputSupplier,
                    final Function<? super ByteOutput, ? extends Function<? super ByteInput, ? extends R>> f1)
            throws IOException {
        final Function<? super ByteInput, ? extends R> f2;
        try (ByteOutput output = outputSupplier.get()) {
            f2 = f1.apply(output);
            output.flush();
        }
        try (ByteInput input = inputSupplier.get()) {
            return f2.apply(input);
        }
    }

    static <R> R wrv(
            final CheckedFunction0<? extends ByteOutput> outputSupplier,
            final CheckedFunction0<? extends ByteInput> inputSupplier,
            final CheckedFunction1<? super ByteOutput, ? extends CheckedFunction1<? super ByteInput, ? extends R>> f1)
            throws IOException {
        return wr(
                () -> outputSupplier.unchecked().apply(),
                () -> inputSupplier.unchecked().apply(),
                o -> {
                    final CheckedFunction1<? super ByteInput, ? extends R> f2 = f1.unchecked().apply(o);
                    return i -> f2.unchecked().apply(i);
                }
        );
    }

    private ByteIoTestUtilities() {
        throw new AssertionError("instantiation is not allowed");
    }
}
