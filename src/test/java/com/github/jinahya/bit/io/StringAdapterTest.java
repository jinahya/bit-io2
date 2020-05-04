package com.github.jinahya.bit.io;

import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static com.github.jinahya.bit.io.StringAdapter.asciiAdapter;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A class for testing {@link StringAdapter} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class StringAdapterTest extends ValueAdapterTest<StringAdapter, String> {

    /**
     * Creates a new instance.
     */
    StringAdapterTest() {
        super(StringAdapter.class, String.class);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testAsciiAdapter(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                          @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final StringAdapter adapter = asciiAdapter(31);
        final String expected = new RandomStringGenerator.Builder()
                .withinRange(0, 127)
                .build()
                .generate(current().nextInt(128));
        output.writeValue(adapter, expected);
        final long padded = output.align();
        final String actual = input.readValue(adapter);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void test(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
              @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final StringAdapter adapter = new StringAdapter(new BytesAdapter(31, 8), UTF_8);
        final String expected = new RandomStringGenerator.Builder()
                .build()
                .generate(current().nextInt(128));
        output.writeValue(adapter, expected);
        final long padded = output.align();
        final String actual = input.readValue(adapter);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }
}