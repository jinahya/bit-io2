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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.awaitility.Awaitility.await;

@Slf4j
class FloatUtilsTest {

    @Test
    void getFloatReader__() throws InterruptedException {
        final ReferenceQueue<Integer> referenceQueue = new ReferenceQueue<>();
        final List<Reference<?>> referenceList = Collections.synchronizedList(new ArrayList<>());
        final List<Thread> threads = IntStream.range(0, 128)
                .<Runnable>mapToObj(i -> () -> {
                    for (int es = FloatConstants.SIZE_MIN_EXPONENT; es <= FloatConstants.SIZE_EXPONENT; es++) {
                        for (int ss = FloatConstants.SIZE_MIN_SIGNIFICAND; ss <= FloatConstants.SIZE_SIGNIFICAND; ss++) {
                            FloatUtils.getFloatReader(
                                    FloatConstraints.requireValidExponentSize(es),
                                    FloatConstraints.requireValidSignificandSize(ss),
                                    referenceQueue,
                                    esr -> {
                                        referenceList.add(esr);
                                    },
                                    referenceQueue,
                                    ssr -> {
                                        referenceList.add(ssr);
                                    }
                            );
                        }
                    }
                })
                .map(Thread::new)
                .parallel()
                .peek(Thread::start)
                .toList();
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        log.debug("all instantiated: {}", FloatUtils.READER_INSTANCES.size());
        log.debug("number of references added: {}", referenceList.size());
        final Thread thread = new Thread(() -> {
            int count = 0;
            while (!Thread.currentThread().isInterrupted()) {
//                log.debug("size: {}", FloatUtils.READER_INSTANCES.size());
                try {
                    final Reference<? extends Integer> reference = referenceQueue.remove(1000L);
                    if (reference != null) {
//                        log.debug("removed: {}, {}", reference, reference.get());
                        count++;
                    }
                } catch (final InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            log.debug("number of references removed: {}", count);
        });
        thread.start();
        referenceList.forEach(Reference::enqueue);
        referenceList.clear();
        log.debug("all references enqueued");
        System.gc();
        await().atMost(10L, TimeUnit.MINUTES).until(FloatUtils.READER_INSTANCES::isEmpty);
        thread.interrupt();
        thread.join();
        assert FloatUtils.READER_INSTANCES.isEmpty();
    }

    @Test
    void getFloatWriter__() throws InterruptedException {
        final ReferenceQueue<Integer> referenceQueue = new ReferenceQueue<>();
        final List<Reference<?>> referenceList = Collections.synchronizedList(new ArrayList<>());
        final List<Thread> threads = IntStream.range(0, 128)
                .<Runnable>mapToObj(i -> () -> {
                    for (int es = FloatConstants.SIZE_MIN_EXPONENT; es <= FloatConstants.SIZE_EXPONENT; es++) {
                        for (int ss = FloatConstants.SIZE_MIN_SIGNIFICAND; ss <= FloatConstants.SIZE_SIGNIFICAND; ss++) {
                            FloatUtils.getFloatWriter(
                                    FloatConstraints.requireValidExponentSize(es),
                                    FloatConstraints.requireValidSignificandSize(ss),
                                    referenceQueue,
                                    esr -> {
                                        referenceList.add(esr);
                                    },
                                    referenceQueue,
                                    ssr -> {
                                        referenceList.add(ssr);
                                    }
                            );
                        }
                    }
                })
                .map(Thread::new)
                .parallel()
                .peek(Thread::start)
                .toList();
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        log.debug("all instantiated: {}", FloatUtils.WRITER_INSTANCES.size());
        log.debug("number of references added: {}", referenceList.size());
        final Thread thread = new Thread(() -> {
            int count = 0;
            while (!Thread.currentThread().isInterrupted()) {
//                log.debug("size: {}", FloatUtils.WRITER_INSTANCES.size());
                try {
                    final Reference<? extends Integer> reference = referenceQueue.remove(1000L);
                    if (reference != null) {
//                        log.debug("removed: {}, {}", reference, reference.get());
                        count++;
                    }
                } catch (final InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            log.debug("number of references removed: {}", count);
        });
        thread.start();
        referenceList.forEach(Reference::enqueue);
        referenceList.clear();
        log.debug("all references enqueued");
        System.gc();
        await().atMost(10L, TimeUnit.MINUTES).until(FloatUtils.WRITER_INSTANCES::isEmpty);
        thread.interrupt();
        thread.join();
        assert FloatUtils.WRITER_INSTANCES.isEmpty();
    }
}
