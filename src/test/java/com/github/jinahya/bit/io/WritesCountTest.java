package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.util.function.ObjIntConsumer;

class WritesCountTest {

    private static class WritesCountImpl
            implements WritesCount<WritesCountImpl> {

        private ObjIntConsumer<? super BitOutput> countWriter;
    }

    @Test
    void countWriter__() {
        new WritesCountImpl().countWriter((o, c) -> {
        });
    }
}
