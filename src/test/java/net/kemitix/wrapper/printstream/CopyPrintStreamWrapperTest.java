package net.kemitix.wrapper.printstream;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
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

    private OutputStream out;

    private PrintStream original;

    private OutputStream copy;

    private PrintStream copyTo;

    private PrintStreamWrapper existing;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        original = new PrintStream(out);
        copy = new ByteArrayOutputStream();
        copyTo = new PrintStream(copy);
        existing = new PassthroughPrintStreamWrapper(original);
    }

    @Test
    public void requiresOriginalPrintStream() {
        //given
        original = null;
        //when
        final ThrowableAssert.ThrowingCallable code = this::interceptOriginal;
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("Null output stream");
    }

    @Test
    public void requiresOriginalPrintStreamInterceptor() {
        //given
        existing = null;
        //when
        final ThrowableAssert.ThrowingCallable code = this::interceptExisting;
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("wrapper");
    }

    @Test
    public void requiresCopyToOnPrintStream() {
        //given
        copyTo = null;
        //when
        final ThrowableAssert.ThrowingCallable code = this::interceptOriginal;
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("copyTo");
    }

    @Test
    public void requiresCopyToOnPrintStreamInterceptor() {
        //given
        copyTo = null;
        //when
        final ThrowableAssert.ThrowingCallable code = this::interceptExisting;
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("copyTo");
    }

    @Test
    public void whenWriteByteThenWriteToOriginal() {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        interceptor.write('x');
        //then
        assertThat(out.toString()).isEqualTo("x");
    }

    private PrintStream interceptOriginal() {
        return new CopyPrintStreamWrapper(original, copyTo).wrapperSubject();
    }

    @Test
    public void whenWriteByteThenWriteToCopyTo() {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        interceptor.write('x');
        //then
        assertThat(copy.toString()).isEqualTo("x");
    }

    @Test
    public void whenWriteByteArrayThenWriteToOriginal() throws IOException {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        interceptor.write("test".getBytes());
        //then
        assertThat(out.toString()).isEqualTo("test");
    }

    @Test
    public void whenWriteByteArrayThenWriteToCopyTo() throws IOException {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        interceptor.write("test".getBytes());
        //then
        assertThat(copy.toString()).isEqualTo("test");
    }

    @Test
    public void whenExistingInterceptorAndWriteByteThenWriteToCopyTo() {
        //given
        final PrintStream interceptor = interceptExisting();
        //when
        interceptor.write('x');
        //then
        assertThat(copy.toString()).isEqualTo("x");
    }

    private PrintStream interceptExisting() {
        return new CopyPrintStreamWrapper(existing, copyTo).wrapperSubject();
    }
}
