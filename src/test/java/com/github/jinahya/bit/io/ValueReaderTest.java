package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A class for testing {@link ValueReader} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class ValueReaderTest {

    /**
     * Tests {@link ValueReader#nullable(ValueReader)} method.
     */
    @Test
    void testNullable() {
        final ValueReader<User> instance = ValueReader.nullable(new UserReader());
        assertNotNull(instance);
    }
}
