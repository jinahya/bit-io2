package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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
    static Stream<Arguments> bufferByteIoTestParameters() {
        final ByteBuffer[] holder = new ByteBuffer[1];
        final ByteOutput output = new BufferByteOutput(() -> (holder[0] = allocate(BYTES)));
        final ByteInput input = new BufferByteInput(
                () -> (ByteBuffer) requireNonNull(holder[0], "holder[0] is null").flip());
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> bufferByteIoTestParameters2() {
        final ByteBuffer[] holder = new ByteBuffer[1];
        final ByteOutput output = new BufferByteOutput(() -> (holder[0] = allocateDirect(BYTES)));
        final ByteInput input = new BufferByteInput(
                () -> (ByteBuffer) requireNonNull(holder[0], "holder[0] is null").flip());
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> bufferByteIoTestParameters3() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ByteOutput output = BufferByteOutput.from(() -> newChannel(holder[0] = new ByteArrayOutputStream()));
        final ByteInput input = BufferByteInput.from(
                () -> newChannel(
                        new ByteArrayInputStream(requireNonNull(holder[0], "holder[0] is null").toByteArray())));
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> bufferByteIoTestParameters4() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(BYTES);
        final ByteOutput output = BufferByteOutput.from(newChannel(baos));
        final byte[] array;
        try {
            final Field f = ByteArrayOutputStream.class.getDeclaredField("buf");
            f.setAccessible(true);
            array = (byte[]) f.get(baos);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        final ByteInput input = BufferByteInput.from(newChannel(new ByteArrayInputStream(array)));
        return Stream.of(arguments(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> dataByteIoTestParameters() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ByteOutput output = new DataByteOutput(
                () -> new DataOutputStream(holder[0] = new ByteArrayOutputStream()));
        final ByteInput input = new DataByteInput(
                () -> new DataInputStream(new ByteArrayInputStream(
                        requireNonNull(holder[0], "holder[0] is null").toByteArray())));
        return Stream.of(arguments(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> streamByteIoTestParameters() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ByteOutput output = new StreamByteOutput(() -> (holder[0] = new ByteArrayOutputStream()));
        final ByteInput input = new StreamByteInput(
                () -> new ByteArrayInputStream(requireNonNull(holder[0], "holder[0] is null").toByteArray()));
        return Stream.of(arguments(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> channelByteIoTestParameters() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ChannelByteOutput output = new ChannelByteOutput(
                () -> {
                    holder[0] = new ByteArrayOutputStream();
                    return newChannel(holder[0]);
                });
        final ByteInput input = new ChannelByteInput(
                () -> {
                    final byte[] bytes = requireNonNull(holder[0], "holder[0] is null").toByteArray();
                    return newChannel(new ByteArrayInputStream(bytes));
                });
        return Stream.of(arguments(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> ByteIoTestParameters() {
        return Stream.of(bufferByteIoTestParameters(),
                         bufferByteIoTestParameters2(),
                         bufferByteIoTestParameters3(),
                         bufferByteIoTestParameters4(),
                         dataByteIoTestParameters(),
                         streamByteIoTestParameters(),
                         channelByteIoTestParameters())
                .flatMap(s -> s);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteIoTestParameters() {
        super();
    }
}
