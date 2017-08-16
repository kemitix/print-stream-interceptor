package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;
import org.assertj.core.api.ThrowableAssert;
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
 * Tests for {@link FilteredPrintStreamWrapper}.
 */
public class FilteredPrintStreamWrapperTest {

    private PrintStream printStream;

    private OutputStream out;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        printStream = new PrintStream(out);
    }

    @Test
    public void requirePredicateWhenWrappingPrintStream() {
        //given
        final Predicate<String> predicate = null;
        final ThrowableAssert.ThrowingCallable code = () -> {
            //when
            new FilteredPrintStreamWrapper(printStream, predicate);
        };
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("predicate");
    }

    @Test
    public void requirePredicateWhenWrappingWrapper() {
        //given
        final Wrapper<PrintStream> existing = new PassthroughPrintStreamWrapper(printStream);
        final ThrowableAssert.ThrowingCallable code = () -> {
            //when
            new FilteredPrintStreamWrapper(existing, null);
        };
        //then
        assertThatNullPointerException().isThrownBy(code)
                                        .withMessage("predicate");
    }

    @Test
    public void canCreateWithPrintStream() {
        //when
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(printStream, o -> true);
        //then
        assertThat(interceptor.getWrapperCore()).isSameAs(printStream);
        assertThat(interceptor.getWrapperDelegate()).isSameAs(printStream);
    }

    @Test
    public void canCreateWithPrintStreamInterceptor() {
        //given
        final Wrapper<PrintStream> existing = new PassthroughPrintStreamWrapper(printStream);
        //when
        final Wrapper<PrintStream> interceptor = new FilteredPrintStreamWrapper(existing, o -> true);
        //then
        assertThat(interceptor.getWrapperCore()).isSameAs(printStream);
        assertThat(interceptor.getWrapperDelegate()).isSameAs(existing);
        assertThat(interceptor.findInnerWrapper()).contains(existing);
    }

    @Test
    public void whenPredicateTrueThenFilterPrintsLine() throws IOException {
        //given
        final Predicate<String> predicate = o -> true;
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(printStream, predicate);
        //when
        interceptor.println("test");
        //then
        assertThat(out.toString()).isEqualTo("test\n");
    }

    @Test
    public void whenPredicateTrueThenFilterPrintsString() throws IOException {
        //given
        final Predicate<String> predicate = o -> true;
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(printStream, predicate);
        //when
        interceptor.print("test");
        //then
        assertThat(out.toString()).isEqualTo("test");
    }

    @Test
    public void whenPredicateFalseThenFilterIgnoresPrint() {
        //given
        final Predicate<String> predicate = o -> false;
        final OutputStream out = new ByteArrayOutputStream();
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(new PrintStream(out), predicate);
        //when
        interceptor.print("test");
        //then
        assertThat(out.toString()).isEmpty();
    }

    @Test
    public void whenPredicateFalseThenFilterIgnoresPrintln() {
        //given
        final Predicate<String> predicate = o -> false;
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(new PrintStream(out), predicate);
        //when
        interceptor.println("test");
        //then
        assertThat(out.toString()).isEmpty();
    }

    @Test
    public void predicateIgnoresDirectWrites() {
        //given
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(printStream, o -> false);
        //when
        interceptor.write('x');
        //then
        assertThat(out.toString()).isEqualTo("x");
    }
}
