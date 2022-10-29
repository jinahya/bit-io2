package com.github.jinahya.bit.io;

import java.io.IOException;

public class UserWriter
        implements BitWriter<User> {

    @Override
    public void write(final BitOutput output, final User value) throws IOException {
        nameWriter.write(output, value.getName());
        output.writeInt(true, 7, value.getAge());
    }

    private final BitWriter<String> nameWriter = StringWriter.compressedUtf8();
}
