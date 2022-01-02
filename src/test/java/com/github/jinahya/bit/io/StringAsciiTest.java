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
class StringAsciiTest {

    static Stream<Arguments> randomBytesAndMaximumCharactersStream() {
        return ByteArrayAsciiTest.randomBytesAndLengthSizeStream()
                .map(a -> {
                    final DefaultArgumentsAccessor accessor = new DefaultArgumentsAccessor(a.get());
                    final byte[] randomBytes = accessor.get(0, byte[].class);
                    final Integer lengthSize = accessor.getInteger(1);
                    final int maximumCharacters = (int) Math.pow(2, lengthSize);
                    return Arguments.of(new String(randomBytes, StandardCharsets.US_ASCII), maximumCharacters);
                });
    }

    @MethodSource({"randomBytesAndMaximumCharactersStream"})
    @ParameterizedTest
    void test(final String expected, final int maximumCharacters) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
        final StringWriter writer = StringWriter.ascii(maximumCharacters, false);
        writer.write(output, expected);
        final long padded = output.align();
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
        final StringReader reader = StringReader.ascii(maximumCharacters, false);
        final String actual = reader.read(input);
        final long discarded = input.align();
        assertThat(actual).isEqualTo(expected);
        assertThat(discarded).isEqualTo(padded);
    }
}
