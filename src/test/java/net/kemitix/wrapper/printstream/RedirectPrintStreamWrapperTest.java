package net.kemitix.wrapper.printstream;

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
public class RedirectPrintStreamWrapperTest {

    private final OutputStream out = new ByteArrayOutputStream();

    private final OutputStream redirect = new ByteArrayOutputStream();

    private final PrintStream original = new PrintStream(out);

    private final PrintStream existing = PrintStreamWrapper.passthrough(original);

    private final PrintStream redirectTo = new PrintStream(redirect);

    @Test
    public void requiresOriginalPrintStream() {
        assertThatNullPointerException()
                .isThrownBy(() -> PrintStreamWrapper.redirect((PrintStream) null, redirectTo))
                .withMessage("Null output stream");
    }

    @Test
    public void requiresOriginalPrintStreamInterceptor() {
        assertThatNullPointerException()
                .isThrownBy(() -> PrintStreamWrapper.redirect((PrintStreamWrapper) null, redirectTo))
                .withMessage("wrapper");
    }

    @Test
    public void requiresRedirectToOnPrintStream() {
        assertThatNullPointerException()
                .isThrownBy(() -> PrintStreamWrapper.redirect(original, null))
                .withMessage("redirectTo");
    }

    @Test
    public void requiresRedirectToOnPrintStreamInterceptor() {
        assertThatNullPointerException()
                .isThrownBy(() -> PrintStreamWrapper.redirect(existing, null))
                .withMessage("redirectTo");
    }

    @Test
    public void whenWriteByteThenDoNotWriteToOriginal() {
        //given
        final PrintStream printStream = PrintStreamWrapper.redirect(original, redirectTo);
        //when
        printStream.write('x');
        //then
        assertThat(out.toString()).isEmpty();
    }

    @Test
    public void whenWriteByteThenWriteToRedirectTo() {
        //given
        final PrintStream printStream = PrintStreamWrapper.redirect(original, redirectTo);
        //when
        printStream.write('x');
        //then
        assertThat(redirect.toString()).isEqualTo("x");
    }

    @Test
    public void whenWriteByteArrayThenDoNotWriteToOriginal() throws IOException {
        //given
        final PrintStream printStream = PrintStreamWrapper.redirect(original, redirectTo);
        //when
        printStream.write("test".getBytes());
        //then
        assertThat(out.toString()).isEmpty();
    }

    @Test
    public void whenWriteByteArrayThenWriteToRedirectTo() throws IOException {
        //given
        final PrintStream printStream = PrintStreamWrapper.redirect(original, redirectTo);
        //when
        printStream.write("test".getBytes());
        //then
        assertThat(redirect.toString()).isEqualTo("test");
    }

    @Test
    public void whenExistingInterceptorAndWriteByteThenWriteToRedirectTo() {
        //given
        final PrintStream printStream = PrintStreamWrapper.redirect(existing, redirectTo);
        //when
        printStream.write('x');
        //then
        assertThat(redirect.toString()).isEqualTo("x");
    }
}
