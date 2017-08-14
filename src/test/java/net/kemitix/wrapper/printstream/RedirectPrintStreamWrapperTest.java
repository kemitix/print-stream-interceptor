package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for {@link CopyPrintStreamWrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class RedirectPrintStreamWrapperTest {

    @Mock
    private PrintStream original;

    @Mock
    private PrintStream redirectTo;

    private Wrapper<PrintStream> existing;

    @Before
    public void setUp() {
        initMocks(this);
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
    public void requiresRedirectToOnPrintStream() {
        //given
        redirectTo = null;
        //when
        final ThrowableAssert.ThrowingCallable code = this::interceptOriginal;
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("redirectTo");
    }

    @Test
    public void requiresRedirectToOnPrintStreamInterceptor() {
        //given
        redirectTo = null;
        //when
        final ThrowableAssert.ThrowingCallable code = this::interceptExisting;
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("redirectTo");
    }

    @Test
    public void whenWriteByteThenDoNotWriteToOriginal() {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        assertThatCode(() -> interceptor.write('x')).doesNotThrowAnyException();
        //then
        then(original).should(never())
                      .write('x');
    }

    @Test
    public void whenWriteByteThenWriteToRedirectTo() {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        assertThatCode(() -> interceptor.write('x')).doesNotThrowAnyException();
        //then
        then(redirectTo).should()
                        .write('x');
    }

    @Test
    public void whenWriteByteArrayThenDoNotWriteToOriginal() {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        assertThatCode(() -> interceptor.write("test".getBytes())).doesNotThrowAnyException();
        //then
        then(original).should(never())
                      .write("test".getBytes(), 0, 4);
    }

    @Test
    public void whenWriteByteArrayThenWriteToRedirectTo() {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        assertThatCode(() -> interceptor.write("test".getBytes())).doesNotThrowAnyException();
        //then
        then(redirectTo).should()
                        .write("test".getBytes(), 0, 4);
    }

    @Test
    public void whenExistingInterceptorAndWriteByteThenWriteToRedirectTo() {
        //given
        final PrintStream interceptor = interceptExisting();
        //when
        assertThatCode(() -> interceptor.write('x')).doesNotThrowAnyException();
        //then
        then(redirectTo).should()
                        .write('x');
    }

    private PrintStream interceptOriginal() {
        return new RedirectPrintStreamWrapper(original, redirectTo).asCore();
    }

    private PrintStream interceptExisting() {
        return new RedirectPrintStreamWrapper(existing, redirectTo).asCore();
    }
}
