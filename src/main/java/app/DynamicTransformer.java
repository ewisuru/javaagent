package app;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class DynamicTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        byte[] byteCode = classfileBuffer;

        // into the transformer will arrive every class loaded so we filter
        // to match only what we need
        if (className.equals("com/zenika/analyze/service/AccountService")) {

            System.out.println("Found class com.zenika.analyze.service.AccountService");
            try {
                // retrive default Javassist class pool
                ClassPool cp = ClassPool.getDefault();
                // get from the class pool our class with this qualified name
                CtClass cc = cp.get("com.zenika.analyze.service.AccountService");
                // get all the methods of the retrieved class
                CtMethod[] methods = cc.getDeclaredMethods();
                for(CtMethod meth : methods) {
                    System.out.println(meth.getName());
                    if (meth.getName().equals("validate")) {
                        System.out.println("Transforming method \"validate\"");
                        meth.setBody("return;");
                    }
                }
                // create the byteclode of the class
                byteCode = cc.toBytecode();
                // remove the CtClass from the ClassPool
                cc.detach();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return byteCode;
    }
}
