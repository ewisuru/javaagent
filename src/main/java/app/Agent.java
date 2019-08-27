package app;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Running agent");
        // registers the transformer
        inst.addTransformer(new DynamicTransformer());
    }
}
