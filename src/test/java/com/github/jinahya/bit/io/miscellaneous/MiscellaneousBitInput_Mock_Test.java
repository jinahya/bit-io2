package com.github.jinahya.bit.io.miscellaneous;

import com.github.jinahya.bit.io.BitInput;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class})
class MiscellaneousBitInput_Mock_Test {

    @Nested
    class NibbleTest {

        @Test
        void writeNibbleUnsigned__() throws IOException {
            final var value = MiscellaneousBitInput.readNibbleUnsigned(input);
            assertThat(value)
                    .isGreaterThanOrEqualTo(0x00)
                    .isLessThan(0x10);
        }
    }

    @Spy
    private BitInput input;
}
