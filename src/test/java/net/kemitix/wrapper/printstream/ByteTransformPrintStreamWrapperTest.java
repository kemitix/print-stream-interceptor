package net.kemitix.wrapper.printstream;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Tests for {@link ByteTransformPrintStreamWrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class ByteTransformPrintStreamWrapperTest {

    private final OutputStream out = new ByteArrayOutputStream();

    private final PrintStream original = new PrintStream(out);

    private final PrintStream existing = PrintStreamWrapper.passthrough(original);

    private PrintStreamWrapper.ByteTransform transformer = b -> b;

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
    public void canTransformByteWhenWrappingPrintStream() {
        //given
        transformer = b -> (byte) 'x';
        //when
        final PrintStream printStream = PrintStreamWrapper.transform(original, transformer);
        printStream.write((int) 'a');
        //then
        assertThat(out.toString()).isEqualTo("x");
    }

    @Test
    public void canTransformByteWhenWrappingWrapper() {
        //given
        transformer = b -> (byte) 'y';
        //when
        final PrintStream wrapper = PrintStreamWrapper.transform(existing, transformer);
        wrapper.write((int) 'a');
        //then
        assertThat(out.toString()).isEqualTo("y");
    }

    @Test
    public void canTransformString() {
        //given
        transformer = b -> (byte) 'z';
        final PrintStream wrapper = PrintStreamWrapper.transform(original, transformer);
        //when
        wrapper.print("in");
        //then
        assertThat(out.toString()).isEqualTo("zz");
    }
}
