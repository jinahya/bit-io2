package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ValueReaderTest {

    @Test
    void testNullable() {
        final ValueReader<User> instance = ValueReader.nullable(new UserReader());
        assertNotNull(instance);
    }
}
