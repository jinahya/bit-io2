package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ByteArrayAdapterAsciiPrintableTest {

    static byte[] randomBytes() {
        final int length = ThreadLocalRandom.current().nextInt(8192, 65536);
        final byte[] bytes = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ThreadLocalRandom.current().nextInt(0x20, 0x7F);
        }
        return bytes;
    }

    static Stream<Arguments> randomBytesAndLengthSizes() {
        return IntStream.range(0, 16)
                .mapToObj(i -> {
                    final byte[] randomBytes = randomBytes();
                    final int lengthSize = (int) Math.ceil(Math.log10(randomBytes.length) / Math.log10(2));
                    return Arguments.of(randomBytes, lengthSize);
                });
    }

    @MethodSource({"randomBytesAndLengthSizes"})
    @ParameterizedTest
    void test(final byte[] randomBytes, final int lengthSize) throws IOException {
//        log.debug("chars length: {}", randomBytes.length);
//        final ValueAdapter<byte[]> adapter = new ByteArrayWriterUnsignedAsciiPrintable(lengthSize);
//        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
//        adapter.write(output, randomBytes);
//        final long padded = output.align();
//        log.debug("bytes length: {}", baos.size());
//        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//        final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
//        final byte[] actual = adapter.read(input);
//        final long discarded = input.align();
//        assertThat(actual).isEqualTo(randomBytes);
//        assertThat(discarded).isEqualTo(padded);
    }
}
