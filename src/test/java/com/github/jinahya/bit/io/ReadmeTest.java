package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr2u;

public class ReadmeTest {

    @Test
    void test1() throws IOException {
        wr2u(o -> {
            o.writeBoolean(true);       // 1 bit   1
            o.writeInt(true, 3, 1);     // 3 bits  4
            o.writeLong(false, 37, 0L); // 37 bits 41
            final long padded = o.align(1);
            assert padded == 7L;
            assert (padded + 41) % Byte.SIZE == 0;
            return i -> {
                boolean v1 = i.readBoolean();    // 1 bit   1
                int v2 = i.readInt(true, 3);     // 3 bits  4
                assert v2 == 1;
                long v3 = i.readLong(false, 37); // 37 bits 41
                assert v3 == 0L;
                long discarded = i.align(1);
                assert discarded == 7L;
                assert (discarded + 41) % Byte.SIZE == 0;
            };
        });
    }
}
