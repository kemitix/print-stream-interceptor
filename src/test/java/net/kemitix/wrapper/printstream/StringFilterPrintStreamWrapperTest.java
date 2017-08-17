package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for {@link StringFilterPrintStreamWrapper}.
 */
public class StringFilterPrintStreamWrapperTest {

    private OutputStream out;

    private PrintStream original;

    private Wrapper<PrintStream> existing;

    private Predicate<String> predicate;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        original = new PrintStream(out);
        existing = new PassthroughPrintStreamWrapper(original);
    }

    @Test
    public void requirePredicateWhenWrappingPrintStream() {
        //given
        predicate = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            new StringFilterPrintStreamWrapper(original, predicate);
        })
                                        //and
                                        .withMessage("predicate");
    }

    @Test
    public void requirePredicateWhenWrappingWrapper() {
        //given
        predicate = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            new StringFilterPrintStreamWrapper(existing, null);
        })
                                        //and
                                        .withMessage("predicate");
    }

    @Test
    public void whenPredicateTrueThenFilterPrintsLine() throws IOException {
        //given
        predicate = o -> true;
        final PrintStream wrapper = new StringFilterPrintStreamWrapper(original, predicate);
        //when
        wrapper.println("test");
        //then
        assertThat(out.toString()).isEqualTo("test\n");
    }

    @Test
    public void whenPredicateTrueThenFilterPrintsString() throws IOException {
        //given
        predicate = o -> true;
        final PrintStream wrapper = new StringFilterPrintStreamWrapper(original, predicate);
        //when
        wrapper.print("test");
        //then
        assertThat(out.toString()).isEqualTo("test");
    }

    @Test
    public void whenPredicateFalseThenFilterIgnoresPrint() {
        //given
        predicate = o -> false;
        final PrintStream wrapper = new StringFilterPrintStreamWrapper(original, predicate);
        //when
        wrapper.print("test");
        //then
        assertThat(out.toString()).isEmpty();
    }

    @Test
    public void whenPredicateFalseThenFilterIgnoresPrintln() {
        //given
        predicate = o -> false;
        final PrintStream wrapper = new StringFilterPrintStreamWrapper(original, predicate);
        //when
        wrapper.println("test");
        //then
        assertThat(out.toString()).isEmpty();
    }
}
