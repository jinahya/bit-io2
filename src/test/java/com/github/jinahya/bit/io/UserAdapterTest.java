package com.github.jinahya.bit.io;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserAdapterTest extends ValueAdapterTest<UserAdapter, User> {

    UserAdapterTest() {
        super(UserAdapter.class, User.class);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoParameters#byteIoParameters"})
    @ParameterizedTest
    void test(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
              @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final User expected = User.newRandomInstance();
        output.writeValue(new UserAdapter(), expected);
        final long padded = output.align();
        final User actual = input.readValue(new UserAdapter());
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }
}