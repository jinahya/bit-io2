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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

abstract class AbstractByteOutputTest<T extends AbstractByteOutput<U>, U> {

    protected AbstractByteOutputTest(final Class<T> outputClass, final Class<U> targetClass) {
        super();
        this.outputClass = Objects.requireNonNull(outputClass, "outputClass is null");
        this.targetClass = Objects.requireNonNull(targetClass, "targetClass is null");
    }

    protected U newTarget() {
        return Mockito.mock(targetClass);
    }

    protected T newOutput() {
        try {
            return outputClass.getConstructor(targetClass).newInstance(newTarget());
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    @Test
    void write__() throws IOException {
        final int value = ThreadLocalRandom.current().nextInt(256);
        newOutput().write(value);
    }

    protected final Class<T> outputClass;

    protected final Class<U> targetClass;
}
