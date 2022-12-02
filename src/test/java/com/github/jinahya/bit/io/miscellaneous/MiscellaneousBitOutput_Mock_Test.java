package com.github.jinahya.bit.io.miscellaneous;

import com.github.jinahya.bit.io.BitOutput;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@ExtendWith({MockitoExtension.class})
class MiscellaneousBitOutput_Mock_Test {

    @Nested
    class NibbleTest {

        @Test
        void writeNibbleUnsigned__() throws IOException {
            MiscellaneousBitOutput.writeNibbleUnsigned(output, ThreadLocalRandom.current().nextInt());
        }
    }

    @Spy
    private BitOutput output;
}
