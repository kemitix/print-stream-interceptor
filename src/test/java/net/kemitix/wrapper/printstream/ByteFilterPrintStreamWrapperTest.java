package net.kemitix.wrapper.printstream;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for {@link ByteFilterPrintStreamWrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class ByteFilterPrintStreamWrapperTest {

    private OutputStream out;

    private PrintStream original;

    private PrintStreamWrapper.ByteFilter predicate;

    private PrintStream existing;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        original = new PrintStream(out);
        existing = PrintStreamWrapper.passthrough(original);
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
    public void whenPredicateTrueAndWrappingOriginalThenFilterWritesByte() {
        //given
        predicate = o -> true;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.write('x');
        //then
        assertThat(out.toString()).isEqualTo("x");
    }

    @Test
    public void whenPredicateTrueAndWrappingWrapperThenFilterWritesByte() {
        //given
        predicate = o -> true;
        final PrintStream wrapper = PrintStreamWrapper.filter(existing, predicate);
        //when
        wrapper.write('x');
        //then
        assertThat(out.toString()).isEqualTo("x");
    }

    @Test
    public void whenPredicateTrueThenFilterWritesByteArray() throws IOException {
        //given
        predicate = o -> true;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.write("test".getBytes());
        //then
        assertThat(out.toString()).isEqualTo("test");
    }

    @Test
    public void whenPredicateFalseThenFilterDoesNotWriteByte() {
        //given
        predicate = o -> false;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.write('x');
        //then
        assertThat(out.toString()).isEqualTo("");
    }

    @Test
    public void whenPredicateFalseThenFilterDoesNotWritesByteArray() throws IOException {
        //given
        predicate = o -> false;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.write("test".getBytes());
        //then
        assertThat(out.toString()).isEqualTo("");
    }

    @Test
    public void whenPredicateTrueThenFilterWritesByteArraySubsection() {
        //given
        predicate = o -> true;
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.write("test".getBytes(), 1, 2);
        //then
        assertThat(out.toString()).isEqualTo("es");
    }

    @Test
    public void whenPredicateSelectNonEThenFilterWritesByteArrayWithoutAnyEs() throws IOException {
        //given
        predicate = o -> o != 'e';
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.write("test".getBytes());
        //then
        assertThat(out.toString()).isEqualTo("tst");
    }

    @Test
    public void whenPredicateSelectsNonSThenFilterPrintsStringWithoutAnySs() {
        //given
        predicate = o -> o != 's';
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.print("test");
        //then
        assertThat(out.toString()).isEqualTo("tet");
    }

    @Test
    public void whenPredicateSelectsNonOThenFilterPrintsObjectWithoutAnyOs() {
        //given
        predicate = o -> o != 'O';
        final PrintStream wrapper = PrintStreamWrapper.filter(original, predicate);
        //when
        wrapper.print(new Object());
        //then
        assertThat(out.toString()).startsWith("java.lang.bject@");
    }
}
