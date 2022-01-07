package com.github.jinahya.bit.io;

import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @RepeatedTest(1)
    void wr__single() throws IOException {
        BitIoTestUtils.wr2v(o -> {
            final User expected = User.newRandomInstance();
            new User.Writer().write(o, expected);
            return i -> {
                final User actual = new User.Reader().read(i);
                assertThat(actual).isEqualTo(expected);
            };
        });
    }
}
