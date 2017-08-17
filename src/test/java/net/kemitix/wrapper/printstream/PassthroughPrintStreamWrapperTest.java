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
import net.kemitix.wrapper.WrapperState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Tests for {@link PassthroughPrintStreamWrapper}s.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class PassthroughPrintStreamWrapperTest {

    private OutputStream out;

    private PrintStream original;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        original = new PrintStream(out);
    }

    @Test
    public void providesRequiredWrapperState() {
        //given
        final Wrapper<PrintStream> wrapper = new PassthroughPrintStreamWrapper(original);
        //when
        final WrapperState<PrintStream> state = wrapper.getWrapperState();
        //then
        assertThat(state).isNotNull();
    }

    @Test
    public void canWriteAByteUnmodified() {
        //given
        final PrintStream wrapper = new PassthroughPrintStreamWrapper(original);
        //when
        wrapper.write('x');
        //then
        assertThat(out.toString()).isEqualTo("x");
    }

    @Test
    public void canWriteByteArrayUnmodified() throws IOException {
        //given
        final PrintStream wrapper = new PassthroughPrintStreamWrapper(original);
        //when
        wrapper.write("test".getBytes());
        //then
        assertThat(out.toString()).isEqualTo("test");
    }

    @Test
    public void canWriteByteArraySubsectionUnmodified() throws IOException {
        //given
        final PrintStream wrapper = new PassthroughPrintStreamWrapper(original);
        //when
        wrapper.write("test".getBytes(), 1, 2);
        //then
        assertThat(out.toString()).isEqualTo("es");
    }

    @Test
    public void writeNullByteArrayWillThrowNullPointerException() throws IOException {
        //given
        final PrintStream wrapper = new PassthroughPrintStreamWrapper(original);
        //then
        assertThatNullPointerException().isThrownBy(
                //when
                () -> wrapper.write(null));
    }

    @Test
    public void writeNullByteArraySubsectionWillThrowNullPointerException() {
        //given
        final PrintStream wrapper = new PassthroughPrintStreamWrapper(original);
        //then
        assertThatNullPointerException().isThrownBy(
                //when
                () -> wrapper.write(null, 0, 0));
    }

    @Test
    public void writeByteToSecondWrapperDelegatesToFirst() {
        //given
        final OutputStream redirectTo = new ByteArrayOutputStream();
        final Wrapper<PrintStream> first = new RedirectPrintStreamWrapper(original, new PrintStream(redirectTo));
        final PassthroughPrintStreamWrapper second = new PassthroughPrintStreamWrapper(first);
        //when
        second.write('x');
        //then
        assertThat(redirectTo.toString()).isEqualTo("x");
        assertThat(out.toString()).isEmpty();
    }

    @Test
    public void writeStringToSecondWrapperDelegatesToFirst() {
        //given
        final OutputStream redirectTo = new ByteArrayOutputStream();
        final Wrapper<PrintStream> first = new RedirectPrintStreamWrapper(original, new PrintStream(redirectTo));
        final PassthroughPrintStreamWrapper second = new PassthroughPrintStreamWrapper(first);
        //when
        second.print("test");
        //then
        assertThat(redirectTo.toString()).isEqualTo("test");
        assertThat(out.toString()).isEmpty();
    }
}
