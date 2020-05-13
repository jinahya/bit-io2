package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ValueWriterTest {

    @Test
    void testNullable() {
        final ValueWriter<User> instance = ValueWriter.nullable(new UserWriter());
        assertNotNull(instance);
    }
}
