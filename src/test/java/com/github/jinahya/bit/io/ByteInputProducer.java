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

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import static java.nio.ByteBuffer.allocate;
import static org.mockito.Mockito.mock;

@Slf4j
class ByteInputProducer {

    // ----------------------------------------------------------------------------------------------------------- array
    @Produces
    ArrayByteInput produceArrayByteInput(final InjectionPoint injectionPoint) {
        return new ArrayByteInput(() -> new byte[1]) {
            @Override
            public int read(final byte[] source) throws IOException {
                if (index == source.length) {
                    index = 0;
                }
                return super.read(source);
            }
        };
    }

    void disposeArrayByteInput(@Disposes final ArrayByteInput byteInput) {
        // does nothing.
    }

    // ---------------------------------------------------------------------------------------------------------- buffer
    @Produces
    BufferByteInput produceBufferByteInput(final InjectionPoint injectionPoint) {
        return new BufferByteInput(() -> (ByteBuffer) allocate(1).position(1)) {
            @Override
            public int read(final ByteBuffer source) throws IOException {
                if (!source.hasRemaining()) {
                    source.clear();
                }
                return super.read(source);
            }
        };
    }

    void disposeBufferByteInput(@Disposes final BufferByteInput byteInput) {
        // does nothing.
    }

    // ------------------------------------------------------------------------------------------------------------ data
    @Produces
    DataByteInput produceDataByteInput(final InjectionPoint injectionPoint) {
        return new DataByteInput(() -> new DataInputStream(new WhiteInputStream()));
    }

    void disposeDataByteInput(@Disposes final DataByteInput byteInput) {
        // does nothing.
    }

    // ---------------------------------------------------------------------------------------------------------- stream
    @Produces
    StreamByteInput produceStreamByteInput(final InjectionPoint injectionPoint) {
        return new StreamByteInput(WhiteInputStream::new);
    }

    void disposeStreamByteInput(@Disposes final StreamByteInput byteInput) {
        // does nothing.
    }

//    // --------------------------------------------------------------------------------------------------------- channel
//    @Produces
//    ChannelByteInput2 produceChannelByteInput(final InjectionPoint injectionPoint) {
//        return ChannelByteInput2.of(WhiteByteChannel::new);
//    }
//
//    void disposeChannelByteInput(@Disposes final ChannelByteInput2 byteInput) {
//        // does nothing.
//    }

    // ------------------------------------------------------------------------------------------------------------- raf
    @Produces
    RandomAccessByteInput produceRandomAccessFileByteInput(final InjectionPoint injectionPoint) {
        return new RandomAccessByteInput(() -> mock(RandomAccessFile.class));
    }

    void disposeRandomAccessFileByteInput(@Disposes final RandomAccessByteInput byteInput) {
    }
}
