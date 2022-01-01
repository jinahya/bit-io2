package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class StringAdapterAsciiPrintableTest {

    static Stream<Arguments> randomBytesAndLengthSizes() {
        return ByteArrayAdapterAsciiPrintableTest.randomBytesAndLengthSizes();
    }

//    @MethodSource({"randomBytesAndLengthSizes"})
//    @ParameterizedTest
//    void test(final byte[] randomBytes, final int lengthSize) throws IOException {
//        log.debug("chars length: {}", randomBytes.length);
//        final StringAdapter adapter = StringAdapter.ascii(lengthSize, true);
//        final String expected = new String(randomBytes, StandardCharsets.US_ASCII);
//        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
//        adapter.write(output, expected);
//        final long padded = output.align();
//        log.debug("bytes length: {}", baos.size());
//        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//        final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
//        final String actual = adapter.read(input);
//        final long discarded = input.align();
//        assertThat(actual).isEqualTo(expected);
//        assertThat(discarded).isEqualTo(padded);
//    }
}
