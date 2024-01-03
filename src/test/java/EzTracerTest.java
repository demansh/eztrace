import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.PrintStream;

public class EzTracerTest {

    private final PrintStream outMock = Mockito.mock(PrintStream.class);

    @Before
    public void setUp() {
        System.setOut(outMock);
    }


    @Test
    public void stop_shouldPrint() {
        EzTracer.start();
        EzTracer.lap("This#foo", this::foo);
        EzTracer.stop();

        Mockito.verify(outMock).println(ArgumentMatchers.matches("^  This#foo\t\t[0-9]\\d*$"));
    }

    @Test
    public void anotherLap_shouldAddLine() {
        EzTracer.start();
        EzTracer.lap("This#foo", this::foo);
        EzTracer.lap("This#bar", this::bar);
        EzTracer.stop();

        Mockito.verify(outMock).println(ArgumentMatchers.matches("^  This#foo\t\t[0-9]\\d*$"));
        Mockito.verify(outMock).println(ArgumentMatchers.matches("^  This#bar\t\t[0-9]\\d*$"));
    }

    @Test
    public void nestedLap_shouldAddTabulation() {
        EzTracer.start();
        EzTracer.lap("This#nestedFoo", this::nestedFoo);
        EzTracer.stop();

        Mockito.verify(outMock).println(ArgumentMatchers.matches("^  This#nestedFoo\t\t[0-9]\\d*$"));
        Mockito.verify(outMock).println(ArgumentMatchers.matches("^    This#foo\t\t[0-9]\\d*$"));
    }

    @Test
    public void lapBeforeStart_shouldBeSkipped() {
        EzTracer.lap("This#foo", this::foo);
        EzTracer.start();
        EzTracer.stop();

        Mockito.verifyNoInteractions(outMock);
    }

    @Test
    public void lapOnCallable_shouldReturnCallableResult() {
        EzTracer.start();
        String result = EzTracer.lap("This#foo", this::foo);
        EzTracer.stop();

        Assert.assertEquals("foo", result);
    }

    private String foo() {
        return "foo";
    }

    private String bar() {
        return "bar";
    }

    private String nestedFoo() {
        return EzTracer.lap("This#foo", this::foo);
    }
}
