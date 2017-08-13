package net.kemitix.interceptor.printstream;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for {@link CopyPrintStreamInterceptor}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class CopyPrintStreamInterceptorTest {

    @Mock
    private PrintStream original;

    @Mock
    private PrintStream copyTo;

    private PrintStreamInterceptor existing;

    @Before
    public void setUp() {
        initMocks(this);
        existing = new PassthroughPrintStreamInterceptor(original);
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
                                        .withMessage("interceptor");
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
        assertThatCode(() -> interceptor.write('x')).doesNotThrowAnyException();
        //then
        then(original).should()
                    .write('x');
    }

    @Test
    public void whenWriteByteThenWriteToCopyTo() {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        assertThatCode(() -> interceptor.write('x')).doesNotThrowAnyException();
        //then
        then(copyTo).should()
                    .write('x');
    }

    @Test
    public void whenWriteByteArrayThenWriteToOriginal() {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        assertThatCode(() -> interceptor.write("test".getBytes())).doesNotThrowAnyException();
        //then
        then(original).should()
                    .write("test".getBytes(), 0, 4);
    }

    @Test
    public void whenWriteByteArrayThenWriteToCopyTo() {
        //given
        final PrintStream interceptor = interceptOriginal();
        //when
        assertThatCode(() -> interceptor.write("test".getBytes())).doesNotThrowAnyException();
        //then
        then(copyTo).should()
                    .write("test".getBytes(), 0, 4);
    }

    @Test
    public void whenExistingInterceptorAndWriteByteThenWriteToCopyTo() {
        //given
        final PrintStream interceptor = interceptExisting();
        //when
        assertThatCode(() -> interceptor.write('x')).doesNotThrowAnyException();
        //then
        then(copyTo).should()
                    .write('x');
    }

    private PrintStream interceptOriginal() {
        return new CopyPrintStreamInterceptor(original, copyTo).asPrintStream();
    }

    private PrintStream interceptExisting() {
        return new CopyPrintStreamInterceptor(existing, copyTo).asPrintStream();
    }
}
