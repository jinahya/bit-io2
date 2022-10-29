package com.github.jinahya.bit.io;

import java.io.IOException;

public class UserReader
        implements BitReader<User> {

    @Override
    public User read(final BitInput input) throws IOException {
        final var value = new User();
        value.setName(nameReader.read(input));
        value.setAge(input.readInt(true, 7));
        return value;
    }

    private final BitReader<String> nameReader = StringReader.compressedUtf8();
}
