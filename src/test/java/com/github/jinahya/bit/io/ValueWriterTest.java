package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A class for testing {@link ValueWriter} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class ValueWriterTest {

    /**
     * Tests {@link ValueWriter#nullable(ValueWriter)}.
     */
    @Test
    void testNullable() {
        final ValueWriter<User> instance = ValueWriter.nullable(new UserWriter());
        assertNotNull(instance);
    }
}
