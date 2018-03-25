package net.kemitix.wrapper.printstream;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Tests for {@link CopyPrintStreamWrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class CopyPrintStreamWrapperTest {

    private final OutputStream out = new ByteArrayOutputStream();

    private final OutputStream copy = new ByteArrayOutputStream();

    private PrintStream original = new PrintStream(out);

    private PrintStream copyTo = new PrintStream(copy);

    private PrintStream existing = PrintStreamWrapper.passthrough(original);

    @Test
    public void requiresOriginalPrintStream() {
        //given
        original = null;
        //when
        final ThrowableAssert.ThrowingCallable code = () ->
                new CopyPrintStreamWrapper(original, copyTo);
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("Null output stream");
    }

    @Test
    public void requiresOriginalPrintStreamInterceptor() {
        //given
        existing = null;
        //when
        final ThrowableAssert.ThrowingCallable code = () ->
                new CopyPrintStreamWrapper((PrintStreamWrapper) existing, copyTo);
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("wrapper");
    }

    @Test
    public void requiresCopyToOnPrintStream() {
        //given
        copyTo = null;
        //when
        final ThrowableAssert.ThrowingCallable code = () ->
                new CopyPrintStreamWrapper(original, copyTo);
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("copyTo");
    }

    @Test
    public void requiresCopyToOnPrintStreamInterceptor() {
        //given
        copyTo = null;
        //when
        final ThrowableAssert.ThrowingCallable code = () ->
                new CopyPrintStreamWrapper(existing, copyTo);
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("copyTo");
    }

    @Test
    public void whenWriteByteThenWriteToOriginal() {
        //given
        final PrintStream interceptor = new CopyPrintStreamWrapper(original, copyTo);
        //when
        interceptor.write('x');
        //then
        assertThat(out.toString()).isEqualTo("x");
    }

    @Test
    public void whenWriteByteThenWriteToCopyTo() {
        //given
        final PrintStream interceptor = new CopyPrintStreamWrapper(original, copyTo);
        //when
        interceptor.write('x');
        //then
        assertThat(copy.toString()).isEqualTo("x");
    }

    @Test
    public void whenWriteByteArrayThenWriteToOriginal() throws IOException {
        //given
        final PrintStream interceptor = new CopyPrintStreamWrapper(original, copyTo);
        //when
        interceptor.write("test".getBytes());
        //then
        assertThat(out.toString()).isEqualTo("test");
    }

    @Test
    public void whenWriteByteArrayThenWriteToCopyTo() throws IOException {
        //given
        final PrintStream interceptor = new CopyPrintStreamWrapper(original, copyTo);
        //when
        interceptor.write("test".getBytes());
        //then
        assertThat(copy.toString()).isEqualTo("test");
    }

    @Test
    public void whenExistingInterceptorAndWriteByteThenWriteToCopyTo() {
        //given
        final PrintStream interceptor = new CopyPrintStreamWrapper(existing, copyTo);
        //when
        interceptor.write('x');
        //then
        assertThat(copy.toString()).isEqualTo("x");
    }

}
