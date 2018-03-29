package net.kemitix.wrapper.printstream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ByteBufferSegmentTest {

    @Test
    public void canScanFromBufferIntoConsumer() {
        //given
        final byte[] bytes = {'a', 'b', 'c', 'd'};
        final List<Byte> capture = new ArrayList<>();
        //when
        new ByteBufferSegment(bytes, 1, 2)
                .forEach(capture::add);
        //then
        assertThat(capture).containsExactly((byte) 'b', (byte) 'c');
    }

    @Test
    public void requiredLengthGreaterThenOrEqualToZero() {
        assertThatExceptionOfType(IndexOutOfBoundsException.class)
                .isThrownBy(() -> {
                    new ByteBufferSegment(new byte[0], 0, -1)
                            .forEach(b -> {});
                });
    }

    @Test
    public void whenOffsetBeyondBufferThenConsumeNothing() {
        //given
        final byte[] bytes = {'a', 'b', 'c', 'd'};
        //then
        assertThatExceptionOfType(IndexOutOfBoundsException.class)
                .isThrownBy(() -> {
                    new ByteBufferSegment(bytes, 6, 2)
                            .forEach(b -> {});
                });
    }
}
