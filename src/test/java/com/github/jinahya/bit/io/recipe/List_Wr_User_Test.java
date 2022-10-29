package com.github.jinahya.bit.io.recipe;

import com.github.jinahya.bit.io.BitIoTestUtils;
import com.github.jinahya.bit.io.ListReader;
import com.github.jinahya.bit.io.ListWriter;
import com.github.jinahya.bit.io.User;
import com.github.jinahya.bit.io.UserReader;
import com.github.jinahya.bit.io.UserWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class List_Wr_User_Test {

    @Test
    void wr__() throws IOException {
        final List<User> expected = IntStream.range(0, 16)
                .mapToObj(i -> User.newRandomInstance())
                .collect(Collectors.toList());
        final var actual = BitIoTestUtils.wr1u(o -> {
            new ListWriter<>(new UserWriter()).write(o, expected);
            final var padded = o.align(1);
            return i -> {
                try {
                    return new ListReader<>(new UserReader()).read(i);
                } finally {
                    final var discarded = i.align(1);
                    assert discarded == padded;
                }
            };
        });
        assertThat(actual).isEqualTo(expected);
    }
}
