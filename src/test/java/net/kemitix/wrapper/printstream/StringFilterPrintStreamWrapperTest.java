package net.kemitix.wrapper.printstream;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for {@link StringFilterPrintStreamWrapper}.
 */
public class StringFilterPrintStreamWrapperTest {

    private OutputStream out;

    private PrintStream original;

    private PrintStream existing;

    private PrintStreamWrapper.StringFilter predicate;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        original = new PrintStream(out);
        existing = PrintStreamWrapper.transform(original, (String in) -> in);
    }

    @Test
    public void requirePredicateWhenWrappingPrintStream() {
        //given
        predicate = null;
        //then
        assertThatNullPointerException().isThrownBy(() -> {
            //when
            PrintStreamWrapper.filter(original, predicate);
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
            PrintStreamWrapper.filter(existing, predicate);
        })
                                        //and
                                        .withMessage("predicate");
    }

    @Test
    public void whenPredicateTrueThenFilterPrintsLine() {
        //given
        predicate = o -> true;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.println("test");
        //then
        assertThat(out.toString()).isEqualTo("test" + System.lineSeparator());
    }

    @Test
    public void whenPredicateTrueThenFilterPrintsString() {
        //given
        predicate = o -> true;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.print("test");
        //then
        assertThat(out.toString()).isEqualTo("test");
    }

    @Test
    public void whenPredicateFalseThenFilterIgnoresPrint() {
        //given
        predicate = o -> false;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.print("test");
        //then
        assertThat(out.toString()).isEmpty();
    }

    @Test
    public void whenPredicateFalseThenFilterIgnoresPrintln() {
        //given
        predicate = o -> false;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.println("test");
        //then
        assertThat(out.toString()).isEmpty();
    }
}
