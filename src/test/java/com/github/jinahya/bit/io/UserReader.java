package com.github.jinahya.bit.io;

import java.io.IOException;

public class UserReader
        implements BitReader<User> {

    @Override
    public User read(final BitInput input) throws IOException {
        final String name = nameReader.read(input);
        final int age = input.readInt(true, 7);
        return new User(name, age);
    }

    private final BitReader<String> nameReader = StringReader.compressedUtf8();
}
