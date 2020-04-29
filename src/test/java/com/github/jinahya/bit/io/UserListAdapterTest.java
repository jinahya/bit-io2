package com.github.jinahya.bit.io;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;

import static com.github.jinahya.bit.io.User.newRandomInstance;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserListAdapterTest {

    @MethodSource({"com.github.jinahya.bit.io.ByteIoParameters#byteIoParameters"})
    @ParameterizedTest
    void test(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
              @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final List<User> expected
                = range(0, current().nextInt(1, 128)).mapToObj(i -> newRandomInstance()).collect(toList());
        output.writeValue(new UserListAdapter(7), expected);
        final long padded = output.align();
        final List<User> actual = input.readValue(new UserListAdapter(7));
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }
}