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
import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
final class ByteIoParameters {

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> arrayByteIoParameters() {
        final byte[][] holder = new byte[1][];
        final ByteOutput output = new ArrayByteOutput(() -> (holder[0] = new byte[1048576]));
        final ByteInput input = new ArrayByteInput(() -> requireNonNull(holder[0], "holder[0] is null"));
        return Stream.of(arguments(output, input));
    }

    static Stream<Arguments> bufferByteIoParameters() {
        final ByteBuffer[] holder = new ByteBuffer[1];
        final ByteOutput output = new BufferByteOutput(() -> (holder[0] = allocate(1048576)));
        final ByteInput input = new BufferByteInput(
                () -> (ByteBuffer) requireNonNull(holder[0], "holder[0] is null").flip());
        return Stream.of(arguments(output, input));
    }

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

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> channelByteIoParameters() {
        final ByteArrayOutputStream[] holder = new ByteArrayOutputStream[1];
        final ChannelByteOutput2 output = new ChannelByteOutput2(
                () -> newChannel((holder[0] = new ByteArrayOutputStream())));
        final ByteInput input = new ChannelByteInput2(
                () -> newChannel(new ByteArrayInputStream(
                        requireNonNull(holder[0], "holder[0] is null").toByteArray())));
        return Stream.of(arguments(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> byteIoParameters() {
        return Stream.of(arrayByteIoParameters(), bufferByteIoParameters(), dataByteIoParameters(),
                         streamByteIoParameters(), channelByteIoParameters())
                .flatMap(s -> s);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteIoParameters() {
        super();
    }
}
