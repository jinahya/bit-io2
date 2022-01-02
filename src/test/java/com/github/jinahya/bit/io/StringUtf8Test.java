package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.DefaultArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class StringUtf8Test {

    static Stream<Arguments> randomStringStream() {
        return ByteArrayUtf8Test.randomBytesAndLengthSizeStream()
                .map(a -> {
                    final DefaultArgumentsAccessor accessor = new DefaultArgumentsAccessor(a.get());
                    final byte[] randomBytes = accessor.get(0, byte[].class);
                    return Arguments.of(new String(randomBytes, StandardCharsets.UTF_8));
                });
    }

    @MethodSource({"randomStringStream"})
    @ParameterizedTest
    void test(final String expected) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
        final StringWriter writer = StringWriter.utf8();
        writer.write(output, expected);
        final long padded = output.align();
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
        final StringReader reader = StringReader.utf8();
        final String actual = reader.read(input);
        final long discarded = input.align();
        assertThat(actual).isEqualTo(expected);
        assertThat(discarded).isEqualTo(padded);
    }
}
