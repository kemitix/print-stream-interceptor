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

    private final PrintStream original = new PrintStream(out);

    private final PrintStream existing = PrintStreamWrapper.filter(original, (String in) -> true);

    private PrintStream copyTo = new PrintStream(copy);

    @Test
    public void requiresOriginalPrintStream() {
        //given
        final ThrowableAssert.ThrowingCallable code = () ->
                PrintStreamWrapper.copy(null, copyTo);
        //then
        assertThatNullPointerException()
                .isThrownBy(code)
                .withMessage("Null output stream");
    }

    @Test
    public void requiresCopyToPrintStream() {
        //given
        final ThrowableAssert.ThrowingCallable code = () ->
                PrintStreamWrapper.copy(original, null);
        //then
        assertThatNullPointerException()
                .isThrownBy(code)
                .withMessage("copyTo");
    }

    @Test
    public void requiresCopyToOnPrintStream() {
        //given
        copyTo = null;
        //when
        final ThrowableAssert.ThrowingCallable code = () ->
                PrintStreamWrapper.copy(original, copyTo);
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
                PrintStreamWrapper.copy(existing, copyTo);
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("copyTo");
    }

    @Test
    public void whenWriteByteThenWriteToOriginal() {
        //given
        final PrintStream interceptor = PrintStreamWrapper.copy(original, copyTo);
        //when
        interceptor.write('x');
        //then
        assertThat(out.toString()).isEqualTo("x");
    }

    @Test
    public void whenWriteByteThenWriteToCopyTo() {
        //given
        final PrintStream interceptor = PrintStreamWrapper.copy(original, copyTo);
        //when
        interceptor.write('x');
        //then
        assertThat(copy.toString()).isEqualTo("x");
    }

    @Test
    public void whenWriteByteArrayThenWriteToOriginal() throws IOException {
        //given
        final PrintStream interceptor = PrintStreamWrapper.copy(original, copyTo);
        //when
        interceptor.write("test".getBytes());
        //then
        assertThat(out.toString()).isEqualTo("test");
    }

    @Test
    public void whenWriteByteArrayThenWriteToCopyTo() throws IOException {
        //given
        final PrintStream interceptor = PrintStreamWrapper.copy(original, copyTo);
        //when
        interceptor.write("test".getBytes());
        //then
        assertThat(copy.toString()).isEqualTo("test");
    }

    @Test
    public void whenExistingInterceptorAndWriteByteThenWriteToCopyTo() {
        //given
        final PrintStream interceptor = PrintStreamWrapper.copy(existing, copyTo);
        //when
        interceptor.write('x');
        //then
        assertThat(copy.toString()).isEqualTo("x");
    }

}
