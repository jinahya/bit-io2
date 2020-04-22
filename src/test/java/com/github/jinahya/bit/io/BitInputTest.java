package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.IntStream;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({MockitoExtension.class})
@Slf4j
class BitInputTest {

    // -----------------------------------------------------------------------------------------------------------------
    static IntStream illegalSizeForLongSigned() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Long.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Long.SIZE) + 1);
        return builder.build();
    }

    static IntStream illegalSizeForLongUnsigned() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Long.SIZE);
        builder.add(current().nextInt() >>> 1 | Long.SIZE);
        return builder.build();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @DisplayName("assert readLong(false, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"illegalSizeForLongSigned"})
    @ParameterizedTest
    void assertReadLongSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> input.readLong(false, size));
    }

    @DisplayName("assert readLong(true, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"illegalSizeForLongUnsigned"})
    @ParameterizedTest
    void assertReadLongUnsignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> input.readLong(true, size));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    private void stub() {
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Spy
    private BitInput input;
}