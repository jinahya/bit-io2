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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.DataOutput;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * A class for testing factory methods defined in {@link BitOutput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteInputAdapter_FromByteBuffer_Test
 */
class ByteOutputAdapter_From_Test {

    @Nested
    class InputStreamTest {

        @Test
        void __NullOutputStream() {
            final var outputStream = OutputStream.nullOutputStream();
            final var bitOutput = BitOutputFactory.from(outputStream);
            assertThat(bitOutput)
                    .isNotNull();
        }
    }

    @Nested
    class DataOutputTest {

        @Test
        void __ZeroCapacity() {
            final var dataOutput = Mockito.mock(DataOutput.class);
            final var bitOutput = BitOutputFactory.from(dataOutput);
            assertThat(bitOutput)
                    .isNotNull();
        }
    }

    @Nested
    class RandomAccessFileTest {

        @Test
        void __ZeroCapacity() {
            final var randomAccessFile = Mockito.mock(RandomAccessFile.class);
            final var bitOutput = BitOutputFactory.from(randomAccessFile);
            assertThat(bitOutput)
                    .isNotNull();
        }
    }

    @Nested
    class ByteBufferTest {

        @Test
        void __ZeroCapacity() {
            final var byteBuffer = ByteBuffer.allocate(0);
            final var bitOutput = BitOutputFactory.from(byteBuffer);
            assertThat(bitOutput)
                    .isNotNull();
        }
    }

    @Nested
    class WritableByteChannelTest {

        @Test
        void __Mock() {
            final var writableByteChannel = Mockito.mock(WritableByteChannel.class);
            final var bitOutput = BitOutputFactory.from(writableByteChannel);
            assertThat(bitOutput)
                    .isNotNull();
        }
    }
}
