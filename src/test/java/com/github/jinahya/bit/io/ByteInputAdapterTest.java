package com.github.jinahya.bit.io;

import static java.util.Objects.requireNonNull;

abstract class ByteInputAdapterTest<T extends ByteInputAdapter<U>, U> {

    ByteInputAdapterTest(final Class<T> adapterClass, final Class<U> sourceClass) {
        super();
        this.adapterClass = requireNonNull(adapterClass, "adapterClass is null");
        this.sourceClass = requireNonNull(sourceClass, "sourceClass is null");
    }

    final Class<T> adapterClass;

    final Class<U> sourceClass;
}
