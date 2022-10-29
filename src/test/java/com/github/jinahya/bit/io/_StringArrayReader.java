package com.github.jinahya.bit.io;

class _DoubleStringArrayReader
        extends ArrayReader<String[]> {

    public _DoubleStringArrayReader() {
        super(
                i -> {
                    return new String[0];
                },
                String[].class
        );
    }
}
