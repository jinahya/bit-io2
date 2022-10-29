package com.github.jinahya.bit.io;

public class _DoubleStringReader
        extends ArrayReader<String[]> {

    public _DoubleStringReader(final BitReader<? extends String[]> elementReader) {
        super(
                i -> {
                    return new String[0];
                },
                String[].class
        );
    }
}
