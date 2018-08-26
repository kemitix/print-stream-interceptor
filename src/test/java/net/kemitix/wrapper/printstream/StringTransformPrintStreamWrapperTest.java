package net.kemitix.wrapper.printstream;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Tests for {@link StringTransformPrintStreamWrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class StringTransformPrintStreamWrapperTest {

    private final OutputStream out = new ByteArrayOutputStream();

    private final PrintStream original = new PrintStream(out);

    private final PrintStream existing = PrintStreamWrapper.filter(original, (String in) -> true);

    private PrintStreamWrapper.StringTransform transformer = s -> s;

    @Test
    public void requireTransformerWhenWrappingPrintStream() {
        //given
        transformer = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            PrintStreamWrapper.transform(original, transformer);
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
            PrintStreamWrapper.transform(existing, transformer);
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
        final PrintStream printStream = PrintStreamWrapper.transform(original, transformer);
        printStream.print(in);
        //then
        assertThat(out.toString()).isEqualTo(expected);
    }

    @Test
    public void canTransformStringWhenWrappingWrapper() {
        //given
        final String in = "message in";
        final PrintStreamWrapper.StringTransform transform =
                s -> s.replace("in", "OUT");
        //when
        final PrintStream printStream = PrintStreamWrapper.transform(existing, transform);
        printStream.println(in);
        //then
        assertThat(out.toString()).isEqualTo("message OUT" + System.lineSeparator());
    }
}
