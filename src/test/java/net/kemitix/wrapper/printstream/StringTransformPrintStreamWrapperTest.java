package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for {@link StringTransformPrintStreamWrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class StringTransformPrintStreamWrapperTest {

    @Mock
    private PrintStream printStream;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void requireFunctionWhenWrappingPrintStream() {
        //given
        final Function<String, String> function = null;
        //when
        final ThrowableAssert.ThrowingCallable code =
                () -> new StringTransformPrintStreamWrapper(printStream, function);
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("transformer");
    }

    @Test
    public void requireFunctionWhenWrappingWrapper() {
        //given
        final Wrapper<PrintStream> existing = new PassthroughPrintStreamWrapper(printStream);
        final Function<String, String> function = null;
        //when
        final ThrowableAssert.ThrowingCallable code = () -> new StringTransformPrintStreamWrapper(existing, function);
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("transformer");
    }

    @Test
    public void canTransformStringWhenWrappingPrintStream() {
        //given
        final String in = "message in";
        final String expected = "message OUT";
        final Function<String, String> transform = s -> s.replace("in", "OUT");
        final OutputStream out = new ByteArrayOutputStream();
        //when
        final PrintStream printStream = new StringTransformPrintStreamWrapper(new PrintStream(out), transform).asCore();
        printStream.print(in);
        //then
        assertThat(out.toString()).isEqualTo(expected);
    }

    @Test
    public void canTransformStringWhenWrappingWrapper() {
        //given
        final String in = "message in";
        final String expected = "message OUT\n";
        final Function<String, String> transform = s -> s.replace("in", "OUT");
        final OutputStream out = new ByteArrayOutputStream();
        final Wrapper<PrintStream> passthrough = new PassthroughPrintStreamWrapper(new PrintStream(out));
        //when
        final PrintStream printStream = new StringTransformPrintStreamWrapper(passthrough, transform).asCore();
        printStream.println(in);
        //then
        assertThat(out.toString()).isEqualTo(expected);
    }
}
