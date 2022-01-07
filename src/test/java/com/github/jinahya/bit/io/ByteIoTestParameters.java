package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 Jinahya, Inc.
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
import org.junit.jupiter.params.provider.Arguments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.channels.Channels.newChannel;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
final class ByteIoTestParameters {

    private static final int BYTES = 1048576;

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> buffer() {
        final ByteBuffer[] holder = new ByteBuffer[1];
        final ByteOutput output = new BufferByteOutput(() -> (holder[0] = allocate(BYTES)));
        final ByteInput input = new BufferByteInput(
                () -> (ByteBuffer) requireNonNull(holder[0], "holder[0] is null").flip());
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> buffer2() {
        final ByteBuffer[] holder = new ByteBuffer[1];
        final ByteOutput output = new BufferByteOutput(() -> (holder[0] = allocateDirect(BYTES)));
        final ByteInput input = new BufferByteInput(
                () -> (ByteBuffer) requireNonNull(holder[0], "holder[0] is null").flip());
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> buffer3() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(BYTES);
        final ByteOutput output = BufferByteOutput.adapting(() -> newChannel(baos));
        final ByteInput input = BufferByteInput.adapting(() -> newChannel(new ByteArrayInputStream(baos.toByteArray())));
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> buffer4() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(BYTES);
        final ByteOutput output = BufferByteOutput.adapting(() -> newChannel(baos));
        final byte[] array;
        try {
            final Field f = ByteArrayOutputStream.class.getDeclaredField("buf");
            f.setAccessible(true);
            array = (byte[]) f.get(baos);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        final ByteInput input = BufferByteInput.adapting(() -> newChannel(new ByteArrayInputStream(array)));
        return Stream.of(arguments(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> data() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ByteOutput output = new DataByteOutput(
                () -> new DataOutputStream(holder[0] = new ByteArrayOutputStream()));
        final ByteInput input = new DataByteInput(
                () -> new DataInputStream(new ByteArrayInputStream(
                        requireNonNull(holder[0], "holder[0] is null").toByteArray())));
        return Stream.of(arguments(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> stream() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ByteOutput output = new StreamByteOutput(() -> (holder[0] = new ByteArrayOutputStream()));
        final ByteInput input = new StreamByteInput(
                () -> new ByteArrayInputStream(requireNonNull(holder[0], "holder[0] is null").toByteArray()));
        return Stream.of(arguments(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> byteIos() {
        return Stream.of(
                        buffer(),
                        buffer2(),
                        buffer3(),
                        buffer4(),
                        data(),
                        stream()
                )
                .flatMap(s -> s);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteIoTestParameters() {
        throw new AssertionError("initialization is not allowed");
    }
}
