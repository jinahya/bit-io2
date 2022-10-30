package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

class FilterBitReader_Mapping_Test {

    @Test
    void __() {
        final BitReader<byte[]> delegate = ByteArrayReader.compressedAscii(true);
        final Function<byte[], String> mapper = u -> new String(u, StandardCharsets.US_ASCII);
        final BitReader<String> reader = FilterBitReader.mapping(delegate, mapper);
    }
}
