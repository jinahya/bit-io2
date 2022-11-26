package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class _CacheTest {

    static class Wrapping {

        Wrapping(final Wrapping wrapped) {
            super();
            this.wrapped = wrapped;
        }

        Wrapping() {
            this(null);
        }

        Wrapping wrapping() {
            return new Wrapping(wrapped);
        }

        private final Wrapping wrapped;
    }

    @Disabled
    @Test
    void test() throws InterruptedException {
        final _Cache<Wrapping, Wrapping> cache = new _Cache<>(Wrapping::wrapping);
        final int count = 2048;
        final List<Thread> threads = new ArrayList<>(count);
        final CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            final Wrapping key = new Wrapping();
            final Wrapping wrapped1 = cache.get(key);
            assert wrapped1 != null;
//            assert cache.get(key) == wrapped1;
            final Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(2000L);
                } catch (final InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
//                System.out.printf("<<<<<< wrapped1: %1$s\n", wrapped1);
//                assert cache.get(key) == wrapped1;
                latch.countDown();
            });
            thread.start();
            threads.add(thread);
        }
        final Thread thread = new Thread(() -> {
//            try {
//                Thread.sleep(5000L);
//            } catch (final InterruptedException ie) {
//                throw new RuntimeException(ie);
//            }
            for (int i = 0; ; i++) {
                if (i > 3) {
                    System.gc();
                    System.out.println("gc()");
                }
                try {
                    Thread.sleep(1000L);
                } catch (final InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
                final int size = cache.references.size();
                System.err.printf("size: %1$d\n", size);
                if (size == 0 && latch.getCount() == 0L) {
                    break;
                }
            }
        });
        thread.start();
        for (final Thread t : threads) {
            t.join();
        }
        latch.countDown();
        thread.join();
    }
}
