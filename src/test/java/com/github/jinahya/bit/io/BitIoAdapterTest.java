package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTests.randomSizeForIntSigned;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForIntUnsigned;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForLong;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForIntSigned;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForIntUnsigned;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForLong;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BitIoAdapterTest {

    // --------------------------------------------------------------------------------------------------------- boolean
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testBoolean(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean expected = current().nextBoolean();
        output.writeBoolean(expected);
        assertEquals(7L, output.align());
        final boolean actual = input.readBoolean();
        assertEquals(7L, input.align());
        assertEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------------- int(false,)
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testIntSigned(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                       @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForIntSigned();
        final int expected = randomValueForIntSigned(size);
        final boolean unsigned = false;
        output.writeInt(unsigned, size, expected);
        final long padded = output.align();
        final int actual = input.readInt(unsigned, size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(discarded, padded);
    }

    // ------------------------------------------------------------------------------------------------------ int(true,)
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testIntUnsigned(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                         @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForIntUnsigned();
        final int expected = randomValueForIntUnsigned(size);
        final boolean unsigned = true;
        output.writeInt(unsigned, size, expected);
        final long padded = output.align();
        final int actual = input.readInt(unsigned, size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(discarded, padded);
    }

    // ---------------------------------------------------------------------------------------------------- long(false,)
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testLongSigned(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                        @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = false;
        final int size = randomSizeForLong(unsigned);
        final long expected = randomValueForLong(unsigned, size);
        output.writeLong(unsigned, size, expected);
        final long padded = output.align();
        final long actual = input.readLong(unsigned, size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(discarded, padded);
    }

    // ----------------------------------------------------------------------------------------------------- long(true,)
    @Disabled
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testLongUnsigned(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                          @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = false;
        final int size = randomSizeForLong(unsigned);
        final long expected = randomValueForLong(unsigned, size);
        output.writeLong(unsigned, size, expected);
        final long padded = output.align();
        final long actual = input.readLong(unsigned, size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(discarded, padded);
    }
}
