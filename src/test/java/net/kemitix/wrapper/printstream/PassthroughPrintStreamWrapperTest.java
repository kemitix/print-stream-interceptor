/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Paul Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for {@link PassthroughPrintStreamWrapper}s.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class PassthroughPrintStreamWrapperTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    private final PrintStream original = new PrintStream(out);

    @Test
    public void canWriteAByteUnmodified() {
        //given
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //when
        wrapper.write('x');
        //then
        assertThat(out.toString()).isEqualTo("x");
    }

    @Test
    public void canWriteByteArrayUnmodified() throws IOException {
        //given
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //when
        wrapper.write("test".getBytes());
        //then
        assertThat(out.toString()).isEqualTo("test");
    }

    @Test
    public void canWriteByteArraySubsectionUnmodified() {
        //given
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //when
        wrapper.write("test".getBytes(), 1, 2);
        //then
        assertThat(out.toString()).isEqualTo("es");
    }

    @Test
    public void writeNullByteArrayWillThrowNullPointerException() {
        //given
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //then
        assertThatNullPointerException().isThrownBy(
                //when
                () -> wrapper.write(null));
    }

    @Test
    public void writeNullByteArraySubsectionWillThrowNullPointerException() {
        //given
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //then
        assertThatNullPointerException().isThrownBy(
                //when
                () -> wrapper.write(null, 0, 0));
    }

    @Test
    public void writeByteToSecondWrapperDelegatesToFirst() {
        //given
        final OutputStream copyTo = new ByteArrayOutputStream();
        final PrintStream first = PrintStreamWrapper.copy(
                original, new PrintStream(copyTo));
        final PrintStream second = PrintStreamWrapper.filter(first, (String in) -> true);
        //when
        second.write('x');
        //then
        assertThat(copyTo.toString()).isEqualTo("x");
        assertThat(out.toString()).isEqualTo("x");
    }

    @Test
    public void writeStringToSecondWrapperDelegatesToFirst() {
        //given
        final OutputStream copyTo = new ByteArrayOutputStream();
        final PrintStream first = PrintStreamWrapper.copy(
                original, new PrintStream(copyTo));
        final PrintStream second = PrintStreamWrapper.filter(first, (String in) -> true);
        //when
        second.print("test");
        //then
        assertThat(copyTo.toString()).isEqualTo("test");
        assertThat(out.toString()).isEqualTo("test");
    }

    @Test
    public void whenOffsetAndLengthGreaterThenBufLengthThenThrowException() {
        //given
        final byte[] buf = "test".getBytes();
        final int off = 1;
        final int len = 4;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //when
        final ThrowableAssert.ThrowingCallable code = () ->
                wrapper.write(buf, off, len);
        //then
        assertThatCode(code).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void whenOffsetLessThanZeroThenThrowException() {
        //given
        final byte[] buf = "test".getBytes();
        final int off = -1;
        final int len = 4;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //when
        final ThrowableAssert.ThrowingCallable code = () -> wrapper.write(buf, off, len);
        //then
        assertThatCode(code).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void whenLengthIsZeroThenIsValid() {
        //given
        final byte[] buf = "test".getBytes();
        final int off = 3;
        final int len = 0;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        final ThrowableAssert.ThrowingCallable code = () -> {
            //when
            wrapper.write(buf, off, len);
        };
        //then
        assertThatCode(code).doesNotThrowAnyException();
    }

    @Test
    public void whenLengthLessThanZeroThenThrowException() {
        //given
        final byte[] buf = "test".getBytes();
        final int off = 3;
        final int len = -1;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //when
        final ThrowableAssert.ThrowingCallable code = () -> wrapper.write(buf, off, len);
        //then
        assertThatCode(code).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void whenOffsetGreaterThanBufLengthThenThrowException() {
        //given
        final byte[] buf = "test".getBytes();
        final int off = 4;
        final int len = 1;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //when
        final ThrowableAssert.ThrowingCallable code = () -> wrapper.write(buf, off, len);
        //then
        assertThatCode(code).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void whenLengthGreaterThanBufLengthThenThrowException() {
        //given
        final byte[] buf = "test".getBytes();
        final int off = 0;
        final int len = 5;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //when
        final ThrowableAssert.ThrowingCallable code = () -> wrapper.write(buf, off, len);
        //then
        assertThatCode(code).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void whenOffsetAndLengthMatchFullBufThenIsValid() {
        //given
        final byte[] buf = "test".getBytes();
        final int off = 0;
        final int len = 4;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, (String in) -> true);
        //when
        wrapper.write(buf, off, len);
        //then
        assertThat(out.toByteArray()).isEqualTo(buf);
    }

    @Test
    public void canGetInnerWrapper() {
        //given
        final PrintStream printStream = new PrintStream(new ByteArrayOutputStream());
        final PrintStream wrapper = PrintStreamWrapper.filter(printStream, (String in) -> true);
        //when
        final Optional<Wrapper<PrintStream>> result = PrintStreamWrapper.unwrap(wrapper);
        //then
        assertThat(result).isNotEmpty();
        result.ifPresent(printStreamWrapper -> {
            assertThat(printStreamWrapper.getInnerWrapper()).isEmpty();
            assertThat(printStreamWrapper.getWrapperSubject()).isSameAs(printStream);
                }
        );
    }

    @Test
    public void whenNormalPrintStreamThenInnerWrapperIsEmpty() {
        //given
        final PrintStream printStream = new PrintStream(new ByteArrayOutputStream());
        //when
        final Optional<Wrapper<PrintStream>> result = PrintStreamWrapper.unwrap(printStream);
        //then
        assertThat(result).isEmpty();
    }
}
