package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

class FilterBitWriter_Mapping_Test {

    @Test
    void __() {
        final BitWriter<byte[]> delegate = ByteArrayWriter.compressedAscii(true);
        final Function<String, byte[]> mapper = t -> t.getBytes(StandardCharsets.US_ASCII);
        final BitWriter<String> writer = FilterBitWriter.mapping(delegate, mapper);
    }
}
