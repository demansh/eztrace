How to use example:

Initial code:
```
public class IterativeCaller {
    public void iteration() {
        for (int i = 0; i < 3; i++) {
            new Calculator().calculate();
        }
    }

    private static class Calculator {
        public void calculate() {
            perform();
        }

        private void perform() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```

Code with tracing:
```
public class IterativeCaller {
    public void iteration() {
        EzTracer.start();
        EzTracer.lap("RunLoop", () -> {
            for (int i = 0; i < 3; i++) {
                EzTracer.lap("Calculator#calculate", () -> new Calculator().calculate());
            }
        });
        EzTracer.stop();
    }

    private static class Calculator {
        public void calculate() {
            EzTracer.lap("Calculator#perform", () -> perform());
        }

        private void perform() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```

Output will be like:
```
  RunLoop		609
    Calculator#calculate		201
      Calculator#perform		201
    Calculator#calculate		205
      Calculator#perform		205
    Calculator#calculate		202
      Calculator#perform		202
```
