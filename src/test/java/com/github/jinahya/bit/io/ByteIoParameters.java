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
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static java.io.File.createTempFile;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.channels.Channels.newChannel;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
final class ByteIoParameters {

    private static final int BYTES = 1048576;

    // ----------------------------------------------------------------------------------------------------------- array
    @Deprecated
    static Stream<Arguments> arrayByteIoParameters() {
        final byte[][] holder = new byte[1][];
        final ByteOutput output = new ArrayByteOutput(() -> (holder[0] = new byte[BYTES]));
        final ByteInput input = new ArrayByteInput(() -> requireNonNull(holder[0], "holder[0] is null"));
        return Stream.of(arguments(output, input));
    }

    @Deprecated
    static Stream<Arguments> arrayByteIoParameters2() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ByteOutput output = ArrayByteOutput.from(() -> (holder[0] = new ByteArrayOutputStream()));
        final ByteInput input = ArrayByteInput.from(
                () -> new ByteArrayInputStream(requireNonNull(holder[0], "holder[0] is null").toByteArray()));
        return Stream.of(arguments(output, input));
    }

    @Deprecated
    static Stream<Arguments> arrayByteIoParameters3() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(BYTES);
        final ByteOutput output = ArrayByteOutput.from(baos);
        final byte[] array;
        try {
            final Field f = ByteArrayOutputStream.class.getDeclaredField("buf");
            f.setAccessible(true);
            array = (byte[]) f.get(baos);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        final ByteInput input = ArrayByteInput.from(new ByteArrayInputStream(array));
        return Stream.of(arguments(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> bufferByteIoParameters() {
        final ByteBuffer[] holder = new ByteBuffer[1];
        final ByteOutput output = new BufferByteOutput(() -> (holder[0] = allocate(BYTES)));
        final ByteInput input = new BufferByteInput(
                () -> (ByteBuffer) requireNonNull(holder[0], "holder[0] is null").flip());
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> bufferByteIoParameters2() {
        final ByteBuffer[] holder = new ByteBuffer[1];
        final ByteOutput output = new BufferByteOutput(() -> (holder[0] = allocateDirect(BYTES)));
        final ByteInput input = new BufferByteInput(
                () -> (ByteBuffer) requireNonNull(holder[0], "holder[0] is null").flip());
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> bufferByteIoParameters3() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ByteOutput output = BufferByteOutput.from(() -> newChannel(holder[0] = new ByteArrayOutputStream()));
        final ByteInput input = BufferByteInput.from(
                () -> newChannel(
                        new ByteArrayInputStream(requireNonNull(holder[0], "holder[0] is null").toByteArray())));
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> bufferByteIoParameters4() {
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
    static Stream<Arguments> dataByteIoParameters() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ByteOutput output = new DataByteOutput(
                () -> new DataOutputStream(holder[0] = new ByteArrayOutputStream()));
        final ByteInput input = new DataByteInput(
                () -> new DataInputStream(new ByteArrayInputStream(
                        requireNonNull(holder[0], "holder[0] is null").toByteArray())));
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> streamByteIoParameters() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ByteOutput output = new StreamByteOutput(() -> (holder[0] = new ByteArrayOutputStream()));
        final ByteInput input = new StreamByteInput(
                () -> new ByteArrayInputStream(requireNonNull(holder[0], "holder[0] is null").toByteArray()));
        return Stream.of(arguments(output, input));
    }

    // --------------------------------------------------------------------------------------------- random access files
    static Stream<Arguments> randomAccessByteIoParameters() {
        final File[] holder = new File[1];
        final ByteOutput output = new RandomAccessByteOutput(() -> {
            try {
                holder[0] = createTempFile("tmp", null);
                holder[0].deleteOnExit();
                return new RandomAccessFile(holder[0], "rw");
            } catch (final IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        });
        final ByteInput input = new RandomAccessByteInput(
                () -> {
                    try {
                        return new RandomAccessFile(requireNonNull(holder[0], "holder[0] is null"), "r");
                    } catch (final IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                });
        return Stream.of(arguments(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> channelByteIoParameters() {
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
    static Stream<Arguments> byteIoParameters() {
        return Stream.of(arrayByteIoParameters(),
                         arrayByteIoParameters2(),
                         arrayByteIoParameters3(),
                         bufferByteIoParameters(),
                         bufferByteIoParameters3(),
                         bufferByteIoParameters4(),
                         dataByteIoParameters(),
                         streamByteIoParameters(),
                         channelByteIoParameters(),
                         randomAccessByteIoParameters())
                .flatMap(s -> s);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteIoParameters() {
        super();
    }
}
