package com.github.jinahya.bit.io;

import java.io.IOException;

import static com.github.jinahya.bit.io.BytesAdapter.bytesAdapter16;
import static java.nio.charset.StandardCharsets.UTF_8;

class UserAdapter implements ValueAdapter<User> {

    private static final ValueAdapter<String> NAME_ADAPTER
            = ValueAdapter.nullable(new StringValueAdapter(bytesAdapter16(Byte.SIZE), UTF_8));

    @Override
    public void write(final BitOutput output, final User value) throws IOException {
        NAME_ADAPTER.write(output, value.name);
        output.writeUnsignedInt(7, value.age);
    }

    @Override
    public User read(final BitInput input) throws IOException {
        final User value = new User();
        value.name = NAME_ADAPTER.read(input);
        value.age = input.readUnsignedInt(7);
        return value;
    }
}
