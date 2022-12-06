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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

abstract class AbstractByteOutputTest<T extends AbstractByteOutput<?>> {

    protected AbstractByteOutputTest(final Class<T> outputClass) {
        super();
        this.outputClass = Objects.requireNonNull(outputClass, "outputClass is null");
    }

    protected abstract T newInstance(final int size) throws IOException;

    @Test
    void write__() throws IOException {
        final var size = ThreadLocalRandom.current().nextInt(128);
        final var instance = newInstance(size);
        for (var i = 0; i < size; i++) {
            instance.write(ThreadLocalRandom.current().nextInt(256));
        }
        try {
            instance.write(0);
        } catch (final Exception e) {
            // don't care.
        }
    }

    protected File tempFile() throws IOException {
        return File.createTempFile("tmp", "tmp", tempDir);
    }

    protected final Class<T> outputClass;

    @TempDir
    @Accessors(fluent = true)
    @Getter(AccessLevel.PROTECTED)
    private File tempDir;
}
