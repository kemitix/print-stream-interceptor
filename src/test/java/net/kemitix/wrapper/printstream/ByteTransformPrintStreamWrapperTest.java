package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Tests for {@link ByteTransformPrintStreamWrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class ByteTransformPrintStreamWrapperTest {

    private OutputStream out;

    private PrintStream original;

    private Function<Byte, Byte> transformer;

    private Wrapper<PrintStream> existing;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        original = new PrintStream(out);
        transformer = Function.identity();
        existing = new PassthroughPrintStreamWrapper(original);
    }

    @Test
    public void requireTransformerWhenWrappingPrintStream() {
        //given
        transformer = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            new ByteTransformPrintStreamWrapper(original, transformer);
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
            new ByteTransformPrintStreamWrapper(existing, transformer);
        })
                                        //and
                                        .withMessage("transformer");
    }

    @Test
    public void canTransformByteWhenWrappingPrintStream() {
        //given
        transformer = b -> (byte) 'x';
        //when
        final PrintStream printStream = new ByteTransformPrintStreamWrapper(original, transformer);
        printStream.write((int) 'a');
        //then
        assertThat(out.toString()).isEqualTo("x");
    }

    @Test
    public void canTransformByteWhenWrappingWrapper() {
        //given
        transformer = b -> (byte) 'y';
        //when
        final PrintStream wrapper = new ByteTransformPrintStreamWrapper(existing, transformer);
        wrapper.write((int) 'a');
        //then
        assertThat(out.toString()).isEqualTo("y");
    }

    @Test
    public void canTransformString() {
        //given
        transformer = b -> (byte) 'z';
        final PrintStream wrapper = new ByteTransformPrintStreamWrapper(original, transformer);
        //when
        wrapper.print("in");
        //then
        assertThat(out.toString()).isEqualTo("zz");
    }
}
