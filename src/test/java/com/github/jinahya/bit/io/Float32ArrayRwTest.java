package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

class Float32ArrayRwTest {

    @Nested
    class WriteNullableTest {

        @Test
        void nullable_null() throws IOException {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
            Float32ArrayWriter.writeNullableTo(output, null);
            final long padded = output.align();
            final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
            final float[] actual = Float32ArrayReader.readNullableFrom(input);
            final long discarded = input.align();
            assertThat(actual).isNull();
            assertThat(discarded).isEqualTo(padded);
        }

        @Test
        void nullable_nonnull() throws IOException {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
            final float[] expected = new float[current().nextInt(128)];
            for (int i = 0; i < expected.length; i++) {
                expected[i] = current().nextFloat();
            }
            Float32ArrayWriter.writeNullableTo(output, expected);
            final long padded = output.align();
            final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
            final float[] actual = Float32ArrayReader.readNullableFrom(input);
            final long discarded = input.align();
            assertThat(actual).isEqualTo(expected);
            assertThat(discarded).isEqualTo(padded);
        }
    }

    @Test
    void _nonnull() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
        final float[] expected = new float[current().nextInt(128)];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = current().nextFloat();
        }
        Float32ArrayWriter.writeTo(output, expected);
        final long padded = output.align();
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
        final float[] actual = Float32ArrayReader.readFrom(input);
        final long discarded = input.align();
        assertThat(actual).isEqualTo(expected);
        assertThat(discarded).isEqualTo(padded);
    }
}
