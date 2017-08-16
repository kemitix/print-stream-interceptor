/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Paul Campbell
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for {@link PassthroughPrintStreamWrapper}s.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class PassthroughPrintStreamWrapperTest {

    @Mock
    private PrintStream intercepted;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void canWriteAByteUnmodified() {
        //given
        final PrintStream interceptor = new TestPrintStreamWrapper(intercepted);
        //when
        final ThrowableAssert.ThrowingCallable code = () -> interceptor.write('x');
        //then
        assertThatCode(code).doesNotThrowAnyException();
        then(intercepted).should()
                         .write('x');
    }

    @Test
    public void canWriteByteArrayUnmodified() throws IOException {
        //given
        final PrintStream interceptor = new TestPrintStreamWrapper(intercepted);
        //when
        final ThrowableAssert.ThrowingCallable code = () -> interceptor.write("test".getBytes());
        //then
        assertThatCode(code).doesNotThrowAnyException();
        then(intercepted).should()
                         .write("test".getBytes(), 0, 4);
    }

    @Test
    public void canWriteByteArraySubsectionUnmodified() throws IOException {
        //given
        final PrintStream interceptor = new TestPrintStreamWrapper(intercepted);
        //when
        final ThrowableAssert.ThrowingCallable code = () -> interceptor.write("test".getBytes(), 1, 2);
        //then
        assertThatCode(code).doesNotThrowAnyException();
        then(intercepted).should()
                         .write("test".getBytes(), 1, 2);
    }

    @Test
    public void writeNullByteArrayWillThrowNullPointerException() throws IOException {
        //given
        final PrintStream interceptor = new TestPrintStreamWrapper(intercepted);
        //when
        final ThrowableAssert.ThrowingCallable code = () -> interceptor.write(null);
        //then
        assertThatNullPointerException().isThrownBy(code);
    }

    @Test
    public void writeNullByteArraySubsectionWillThrowNullPointerException() {
        //given
        final PrintStream interceptor = new TestPrintStreamWrapper(intercepted);
        //when
        final ThrowableAssert.ThrowingCallable code = () -> interceptor.write(null, 0, 0);
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("buf");
    }

    @Test
    public void canGetOriginalPrintStreamFromSingleInterceptor() {
        //given
        final Wrapper<PrintStream> interceptor = new TestPrintStreamWrapper(intercepted);
        //when
        final PrintStream result = interceptor.getWrapperCore();
        //then
        assertThat(result).isSameAs(intercepted);
    }

    @Test
    public void canGetOriginalPrintStreamThroughMultipleInterceptors() {
        //given
        final Wrapper<PrintStream> existing = new TestPrintStreamWrapper(intercepted);
        final Wrapper<PrintStream> interceptor = new TestPrintStreamWrapper(existing);
        //when
        final PrintStream result = interceptor.getWrapperCore();
        //then
        assertThat(result).isSameAs(intercepted);
    }

    @Test
    public void writeByteToSecondInterceptorDelegatesThroughFirstToOriginal() {
        //given
        final TestPrintStreamWrapper existing = new TestPrintStreamWrapper(intercepted);
        final TestPrintStreamWrapper interceptor = new TestPrintStreamWrapper((Wrapper<PrintStream>) existing);
        //when
        interceptor.asCore()
                   .write('x');
        //then
        assertThat(interceptor.getWritten()).isEqualTo("x");
        assertThat(existing.getWritten()).isEqualTo("x");
        then(intercepted).should()
                         .write('x');
    }

    @Test
    public void writeByteArrayToSecondInterceptorDelegatesThroughFirstToOriginal() throws IOException {
        //given
        final TestPrintStreamWrapper existing = new TestPrintStreamWrapper(intercepted);
        final TestPrintStreamWrapper interceptor = new TestPrintStreamWrapper((Wrapper<PrintStream>) existing);
        //when
        interceptor.asCore()
                   .write("test".getBytes());
        //then
        assertThat(interceptor.getWritten()).isEqualTo("test");
        assertThat(existing.getWritten()).isEqualTo("test");
        then(intercepted).should()
                         .write("test".getBytes(), 0, 4);
    }

    @Test
    public void writeByteArraySubsectionToSecondInterceptorDelegatesThroughFirstToOriginal() {
        //given
        final TestPrintStreamWrapper existing = new TestPrintStreamWrapper(intercepted);
        final TestPrintStreamWrapper interceptor = new TestPrintStreamWrapper((Wrapper<PrintStream>) existing);
        //when
        interceptor.asCore()
                   .write("test".getBytes(), 1, 2);
        //then
        assertThat(interceptor.getWritten()).isEqualTo("es");
        assertThat(existing.getWritten()).isEqualTo("es");
        then(intercepted).should()
                         .write("test".getBytes(), 1, 2);
    }

    @Test
    public void canGetOriginalInterceptor() {
        //given
        final Wrapper<PrintStream> first = new TestPrintStreamWrapper(intercepted);
        final Wrapper<PrintStream> second = new TestPrintStreamWrapper(first);
        //when
        final Optional<Wrapper<PrintStream>> result = second.findInnerWrapper();
        //then
        assertThat(result).contains(first);
    }

    @Test
    public void whenTwoInterceptorsAndFirstIsRemovedThenCanWriteByte() throws IOException {
        //given
        final TestPrintStreamWrapper first = new TestPrintStreamWrapper(intercepted);
        final Wrapper<PrintStream> second = new TestPrintStreamWrapper((Wrapper<PrintStream>) first);
        //when
        second.removeWrapper(first);
        second.asCore()
              .write('x');
        //then
        assertThat(second.findInnerWrapper()).isEmpty();
        assertThat(first.getWritten()).isEmpty();
        then(intercepted).should()
                         .write('x');
    }

    @Test
    public void whenTwoInterceptorsAndFirstIsRemovedThenCanWriteByteArray() throws IOException {
        //given
        final TestPrintStreamWrapper first = new TestPrintStreamWrapper(intercepted);
        final Wrapper<PrintStream> second = new TestPrintStreamWrapper((Wrapper<PrintStream>) first);
        //when
        second.removeWrapper(first);
        second.asCore()
              .write("test".getBytes());
        //then
        assertThat(second.findInnerWrapper()).isEmpty();
        assertThat(first.getWritten()).isEmpty();
        then(intercepted).should()
                         .write("test".getBytes(), 0, 4);
    }

    @Test
    public void whenTwoInterceptorsAndFirstIsRemovedThenCanWriteByteArraySubset() throws IOException {
        //given
        final TestPrintStreamWrapper first = new TestPrintStreamWrapper(intercepted);
        final Wrapper<PrintStream> second = new TestPrintStreamWrapper((Wrapper<PrintStream>) first);
        //when
        second.removeWrapper(first);
        second.asCore()
              .write("test".getBytes(), 1, 2);
        //then
        assertThat(second.findInnerWrapper()).isEmpty();
        assertThat(first.getWritten()).isEmpty();
        then(intercepted).should()
                         .write("test".getBytes(), 1, 2);
    }

    @Test
    public void whenRemoveInterceptorIsNullThenThrowNullException() {
        //given
        final Wrapper<PrintStream> first = new TestPrintStreamWrapper(intercepted);
        final Wrapper<PrintStream> second = new TestPrintStreamWrapper(first);
        //when
        assertThatNullPointerException().isThrownBy(() -> second.removeWrapper(null))
                                        .withMessage("wrapper");
    }

    @Test
    public void whenThreeInterceptorsAndFirstIsRemovedThenOthersRemain() {
        //given
        final Wrapper<PrintStream> first = new TestPrintStreamWrapper(intercepted);
        final Wrapper<PrintStream> second = new TestPrintStreamWrapper(first);
        final Wrapper<PrintStream> third = new TestPrintStreamWrapper(second);
        //when
        third.removeWrapper(first);
        //then
        assertThat(third.findInnerWrapper()).contains(second);
        assertThat(second.findInnerWrapper()).isEmpty();
    }

    private class TestPrintStreamWrapper extends PassthroughPrintStreamWrapper {

        private StringBuilder bytesWritten = new StringBuilder();

        TestPrintStreamWrapper(final PrintStream out) {
            super(out);
        }

        TestPrintStreamWrapper(final Wrapper<PrintStream> interceptor) {
            super(interceptor);
        }

        @Override
        public void write(final int b) {
            super.write(b);
            bytesWritten.append((char) b);
        }

        @Override
        public void write(final byte[] buf, final int off, final int len) {
            super.write(buf, off, len);
            bytesWritten.append(new String(buf).toCharArray(), off, len);
        }

        private String getWritten() {
            return bytesWritten.toString();
        }
    }
}
