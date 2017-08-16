package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;
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
public class RedirectPrintStreamWrapperTest {

    private OutputStream out;

    private PrintStream original;

    private Wrapper<PrintStream> existing;

    private PrintStream redirectTo;

    private OutputStream redirect;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        original = new PrintStream(out);
        existing = new PassthroughPrintStreamWrapper(original);
        redirect = new ByteArrayOutputStream();
        redirectTo = new PrintStream(redirect);
    }

    @Test
    public void requiresOriginalPrintStream() {
        //given
        original = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            new RedirectPrintStreamWrapper(original, redirectTo);
        })
                                        //and
                                        .withMessage("Null output stream");
    }

    @Test
    public void requiresOriginalPrintStreamInterceptor() {
        //given
        existing = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            new RedirectPrintStreamWrapper(existing, redirectTo);
        })
                                        //and
                                        .withMessage("wrapper");
    }

    @Test
    public void requiresRedirectToOnPrintStream() {
        //given
        redirectTo = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            new RedirectPrintStreamWrapper(original, redirectTo);
        })
                                        //and
                                        .withMessage("redirectTo");
    }

    @Test
    public void requiresRedirectToOnPrintStreamInterceptor() {
        //given
        redirectTo = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            new RedirectPrintStreamWrapper(existing, redirectTo).asCore();
        })
                                        //and
                                        .withMessage("redirectTo");
    }

    @Test
    public void whenWriteByteThenDoNotWriteToOriginal() {
        //given
        final PrintStream printStream = new RedirectPrintStreamWrapper(original, redirectTo).asCore();
        //when
        printStream.write('x');
        //then
        assertThat(out.toString()).isEmpty();
    }

    @Test
    public void whenWriteByteThenWriteToRedirectTo() {
        //given
        final PrintStream printStream = new RedirectPrintStreamWrapper(original, redirectTo).asCore();
        //when
        printStream.write('x');
        //then
        assertThat(redirect.toString()).isEqualTo("x");
    }

    @Test
    public void whenWriteByteArrayThenDoNotWriteToOriginal() throws IOException {
        //given
        final PrintStream printStream = new RedirectPrintStreamWrapper(original, redirectTo).asCore();
        //when
        printStream.write("test".getBytes());
        //then
        assertThat(out.toString()).isEmpty();
    }

    @Test
    public void whenWriteByteArrayThenWriteToRedirectTo() throws IOException {
        //given
        final PrintStream printStream = new RedirectPrintStreamWrapper(original, redirectTo).asCore();
        //when
        printStream.write("test".getBytes());
        //then
        assertThat(redirect.toString()).isEqualTo("test");
    }

    @Test
    public void whenExistingInterceptorAndWriteByteThenWriteToRedirectTo() {
        //given
        final PrintStream printStream = new RedirectPrintStreamWrapper(existing, redirectTo).asCore();
        //when
        printStream.write('x');
        //then
        assertThat(redirect.toString()).isEqualTo("x");
    }
}
