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
    public void canScanSingleByteFromBufferIntoConsumer() {
        //given
        final byte[] bytes = {'a', 'b', 'c', 'd'};
        final List<Byte> capture = new ArrayList<>();
        //when
        new ByteBufferSegment(bytes, 1, 1)
                .forEach(capture::add);
        //then
        assertThat(capture).containsExactly((byte) 'b');
    }

    @Test
    public void canScanZeroBytesFromBufferIntoConsumer() {
        //given
        final byte[] bytes = {'a', 'b', 'c', 'd'};
        final List<Byte> capture = new ArrayList<>();
        //when
        new ByteBufferSegment(bytes, 1, 0)
                .forEach(capture::add);
        //then
        assertThat(capture).isEmpty();
    }

    @Test
    public void whenAttemptToGoBeyondBufferEndThenThrowException() {
        //given
        final byte[] bytes = {'a', 'b', 'c', 'd'};
        final List<Byte> capture = new ArrayList<>();
        //then
        assertThatExceptionOfType(IndexOutOfBoundsException.class)
                .isThrownBy(() -> {
                    new ByteBufferSegment(bytes, 2, 3)
                            .forEach(capture::add);
                });
        assertThat(capture).isEmpty();
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
