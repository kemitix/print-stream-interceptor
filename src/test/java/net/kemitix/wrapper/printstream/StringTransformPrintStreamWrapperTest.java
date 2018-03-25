package net.kemitix.wrapper.printstream;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Tests for {@link StringTransformPrintStreamWrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class StringTransformPrintStreamWrapperTest {

    private OutputStream out;

    private PrintStream original;

    private Function<String, String> transformer;

    private PrintStream existing;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        original = new PrintStream(out);
        transformer = Function.identity();
        existing = PrintStreamWrapper.passthrough(original);
    }

    @Test
    public void requireTransformerWhenWrappingPrintStream() {
        //given
        transformer = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            new StringTransformPrintStreamWrapper(original, transformer);
        })
                                        //and
                                        .withMessage("transformer");
    }

    @Test
    public void requireTransformerWhenWrappingWrapper() {
        //given
        transformer = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            new StringTransformPrintStreamWrapper(existing, transformer);
        })
                                        //and
                                        .withMessage("transformer");
    }

    @Test
    public void canTransformStringWhenWrappingPrintStream() {
        //given
        final String in = "message in";
        final String expected = "message OUT";
        transformer = s -> s.replace("in", "OUT");
        //when
        final PrintStream printStream = new StringTransformPrintStreamWrapper(original, transformer);
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
        //when
        final PrintStream printStream = new StringTransformPrintStreamWrapper(existing, transform);
        printStream.println(in);
        //then
        assertThat(out.toString()).isEqualTo(expected);
    }
}
