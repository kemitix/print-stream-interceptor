package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.IOException;
import java.io.PrintStream;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for {@link FilteredPrintStreamWrapper}.
 */
public class FilteredPrintStreamWrapperTest {

    @Mock
    private PrintStream printStream;

    @Captor
    private ArgumentCaptor<byte[]> writeCaptor;

    @Before
    public void setUp() {
        initMocks(this);
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
        final Predicate<String> predicate = "test"::equals;
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(printStream, predicate);
        //when
        interceptor.println("test");
        //then
        then(printStream).should()
                         .write(writeCaptor.capture(), eq(0), eq(4));
        assertThat(writeCaptor.getValue()).contains(new byte[]{'t', 'e', 's', 't', '\n'});
    }

    @Test
    public void whenPredicateTrueThenFilterPrintsString() throws IOException {
        //given
        final Predicate<String> predicate = "test"::equals;
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(printStream, predicate);
        //when
        interceptor.print("test");
        //then
        then(printStream).should()
                         .write(writeCaptor.capture(), eq(0), eq(4));
        assertThat(writeCaptor.getValue()).contains(new byte[]{'t', 'e', 's', 't'});
    }

    @Test
    public void whenPredicateFalseThenFilterIgnoresPrint() {
        //given
        final Predicate<String> predicate = "garbage"::equals;
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(printStream, predicate);
        //when
        assertThatCode(() -> interceptor.print("test")).doesNotThrowAnyException();
        //then
        then(printStream).shouldHaveZeroInteractions();
    }

    @Test
    public void whenPredicateFalseThenFilterIgnoresPrintln() {
        //given
        final Predicate<String> predicate = "garbage"::equals;
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(printStream, predicate);
        //when
        assertThatCode(() -> interceptor.println("test")).doesNotThrowAnyException();
        //then
        then(printStream).shouldHaveZeroInteractions();
    }

    @Test
    public void predicateIgnoresDirectWrites() {
        //given
        final FilteredPrintStreamWrapper interceptor = new FilteredPrintStreamWrapper(printStream, o -> false);
        //when
        assertThatCode(() -> interceptor.write('x')).doesNotThrowAnyException();
        //then
        then(printStream).should()
                         .write('x');
    }
}
