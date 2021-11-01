package me.imspooks.des.clsm;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.Map;

import static me.imspooks.des.clsm.CodeChangeFinder.DEBUG;

@RequiredArgsConstructor
public class CodeTransformer implements ClassFileTransformer {

    private final Map<String, CodeChange[]> changes;

    @Override
    public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingTransformed, ProtectionDomain protectionDomain, byte[] bytes) {
        if (className != null) {
            if (changes.containsKey(className)) {
                String fixedClassName = className.replace("/", ".");

                if (DEBUG) {
                    System.out.println("Applying changes for class " + fixedClassName);
                }

                try {
                    ClassPool pool = ClassPool.getDefault();
                    pool.appendClassPath(new LoaderClassPath(classLoader));
                    CtClass ctClass = pool.get(fixedClassName);

                    for (CtMethod method : ctClass.getMethods()) {
                        if (method.getName().equalsIgnoreCase("saveChunk")) {
                            System.out.println("method = " + method);
                        }
                    }

                    for (CodeChange change : changes.get(className)) {
                        try {
                            CtMethod[] methods = ctClass.getDeclaredMethods(change.getMethodName());
                            boolean found = false;

                            main:
                            for (CtMethod method : methods) {
                                CtClass[] params = method.getParameterTypes();

                                if (params.length != change.getParams().length) {
                                    continue;
                                }

                                for (int i = 0; i < params.length; i++) {
                                    if (!change.getParams()[i].trim().equals(params[i].getName())) {
                                        continue main;
                                    }
                                }

                                found = true;

                                try {
                                    String insert = change.getInsert();
                                    String content = change.getContent();

                                    if (change.getInsert().equalsIgnoreCase("before")) {
                                        method.insertBefore(change.getContent());
                                    } else if (change.getInsert().equalsIgnoreCase("after")) {
                                        method.insertAfter(change.getContent());
                                    } else if (change.getInsert().equalsIgnoreCase("replace")) {
                                        throw new UnsupportedOperationException("Not supported yet.");
//                                        method.setBody(change.getContent());
                                    } else {
                                        method.insertAt(Integer.parseInt(insert), content);
                                    }
                                } catch (CannotCompileException ex) {
                                    if (!change.isOptional()) { // If it's an optional change we can ignore it
                                        throw ex;
                                    }
                                }
                                break;
                            }

                            if (!found && !change.isOptional()) {
                                throw new NotFoundException("Unknown method " + change.getMethodName());
                            }
                        } catch (CannotCompileException ex) {
                            throw new CannotCompileException("Method " + change.getMethodName(), ex);
                        }
                    }

                    return ctClass.toBytecode();
                } catch (NotFoundException | CannotCompileException | IOException ex) {
                    System.err.println("Failed to override methods from class " + fixedClassName + ".");
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
}