package com.github.jinahya.bit.io;

import static java.util.Objects.requireNonNull;

abstract class ByteOutputAdapterTest<T extends ByteOutputAdapter<U>, U> {

    ByteOutputAdapterTest(final Class<T> adapterClass, final Class<U> targetClass) {
        super();
        this.adapterClass = requireNonNull(adapterClass, "adapterClass is null");
        this.targetClass = requireNonNull(targetClass, "targetClass is null");
    }

    final Class<T> adapterClass;

    final Class<U> targetClass;
}
