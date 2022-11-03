package com.github.jinahya.bit.io.recipe;

import com.github.jinahya.bit.io.BitIoConstants;
import com.github.jinahya.bit.io.BitIoTestUtils;
import com.github.jinahya.bit.io.ListReader;
import com.github.jinahya.bit.io.ListWriter;
import com.github.jinahya.bit.io.User;
import com.github.jinahya.bit.io.UserReader;
import com.github.jinahya.bit.io.UserWriter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class List_Wr_User_Test {

    private static Stream<List<User>> randomValueStream() {
        return Stream.of(
                IntStream.range(0, 16)
                        .mapToObj(i -> User.newRandomInstance())
                        .collect(Collectors.toList())
        );
    }

    @MethodSource({"randomValueStream"})
    @ParameterizedTest
    void wr__(final List<User> expected) throws IOException {
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

    @MethodSource({"randomValueStream"})
    @ParameterizedTest
    void wr__UncompressedCount(final List<User> expected) throws IOException {
        final var actual = BitIoTestUtils.wr1u(o -> {
            new ListWriter<>(new UserWriter()).countWriter(BitIoConstants.COUNT_WRITER).write(o, expected);
            final var padded = o.align(1);
            return i -> {
                try {
                    return new ListReader<>(new UserReader()).countReader(BitIoConstants.COUNT_READER).read(i);
                } finally {
                    final var discarded = i.align(1);
                    assert discarded == padded;
                }
            };
        });
        assertThat(actual).isEqualTo(expected);
    }
}
