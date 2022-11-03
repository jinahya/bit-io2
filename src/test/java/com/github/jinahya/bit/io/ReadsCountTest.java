package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

class ReadsCountTest {

    private static class ReadsCountImpl
            implements ReadsCount<ReadsCountImpl> {

        private ToIntFunction<? super BitInput> countReader;
    }

    @Test
    void countReader__() {
        new ReadsCountImpl().countReader(i -> 0);
    }
}
