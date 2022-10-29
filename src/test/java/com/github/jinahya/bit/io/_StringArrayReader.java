package com.github.jinahya.bit.io;

class _StringArrayReader
        extends _ArrayReader<String> {

    _StringArrayReader() {
        super(i -> "", String.class);
    }
}
