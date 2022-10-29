package com.github.jinahya.bit.io;

class _DoubleStringArrayReader
        extends _ArrayReader<String[]> {

    _DoubleStringArrayReader() {
        super(i -> new String[0], String[].class);
    }
}
