package me.imspooks.des.clsm;

import javassist.CtClass;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.Instrumentation;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Code Transformer
 *
 * Heavily inspired by Grinderwolf's Slime-World-Manager
 */
public class CodeChangeFinder {

    private static final Pattern PATTERN = Pattern.compile("^(\\w+)\\s*\\((.*?)\\)\\s*@(.+?\\.txt)$");
    public static final boolean DEBUG = Boolean.getBoolean("desDebug");


    /**
     * Called before the server starts
     */
    @SuppressWarnings("unchecked")
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        final Map<String, CodeChange[]> changes = new HashMap<>();

        try (InputStream fileStream = CodeChangeFinder.class.getResourceAsStream("/list.yml")) {
            if (fileStream == null) {
                System.err.println("List of changes not found.");
                System.exit(1);
                return;
            }

            Yaml yaml = new Yaml();

            try (InputStreamReader reader = new InputStreamReader(fileStream)){
                Map<String, Object> data = yaml.load(reader);

                for (String originalClazz : data.keySet()) {
                    boolean optional = originalClazz.startsWith("__optional__");
                    String clazz = originalClazz.substring(optional ? 12 : 0);

                    if (!(data.get(originalClazz) instanceof ArrayList)) {
                        System.err.println("Invalid change list for class " + clazz + ".");
                        continue;
                    }

                    List<String> changeList = (List<String>) data.get(originalClazz);
                    CodeChange[] changeArray = new CodeChange[changeList.size()];

                    for (int i = 0; i < changeList.size(); i++) {
                        Matcher matcher = PATTERN.matcher(changeList.get(i));

                        // Check if pattern matches the input
                        if (!matcher.find()) {
                            System.err.println("Invalid change '" + changeList.get(i) + "' on class " + clazz + ".");
                            System.exit(1);
                        }

                        // Get method name
                        String methodName = matcher.group(1);

                        // Get parameters and parse them
                        String paramsString = matcher.group(2).trim();
                        String[] parameters;

                        if (paramsString.isEmpty()) {
                            parameters = new String[0];
                        } else {
                            parameters = matcher.group(2).split(",");
                        }

                        // Get code to insert
                        String location = matcher.group(3);
                        String content;
                        String insert;

                        try (InputStream changeStream = CodeChangeFinder.class.getResourceAsStream("/" + location)) {
                            if (changeStream == null) {
                                System.err.println("Failed to find data for change " + changeList.get(i) + " on class " + clazz + ".");
                                System.exit(1);
                                return;
                            }

                            // Convert file to String
                            byte[] contentByteArray = readAllBytes(changeStream);
                            content = new String(contentByteArray, StandardCharsets.UTF_8);

                            if (!content.startsWith("insert:")) {
                                System.err.println("No insert type found for change file /" + location);
                                System.exit(1);
                            }

                            // Get insert type
                            insert = content.split("\\n")[0].substring(7).trim();

                            if (insert.isEmpty() || (!insert.equalsIgnoreCase("before")
                                    && !insert.equalsIgnoreCase("after")
                                    && !insert.equalsIgnoreCase("replace")
                                    && !insert.matches("-?[0-9]+"))) {
                                System.err.println("Invalid insert type for change file /" + location + "\n\tFor options, see me.imspooks.des.clsm.CodeChange.insert");
                                System.exit(1);
                                return;
                            }

                            // Subtract the insert type from the main content
                            content = content.substring(content.indexOf('\n') + 1);
                        }

                        changeArray[i] = new CodeChange(methodName, parameters, content, insert, optional);
                    }

                    if (DEBUG) {
                        System.out.println("Loaded " + changeArray.length + " changes for class " + clazz + ".");
                    }

                    CodeChange[] oldChanges = changes.get(clazz);

                    if (oldChanges == null) {
                        changes.put(clazz, changeArray);
                    } else {
                        CodeChange[] newChanges = new CodeChange[oldChanges.length + changeArray.length];

                        System.arraycopy(oldChanges, 0, newChanges, 0, oldChanges.length);
                        System.arraycopy(changeArray, 0, newChanges, oldChanges.length, changeArray.length);

                        changes.put(clazz, newChanges);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add code transformer
        if (DEBUG) {
            CtClass.debugDump = "./dump";
        }
        instrumentation.addTransformer(new CodeTransformer(changes));
    }

    /**
     * Converts a stream to a byte array
     *
     * @param stream Stream to convert
     * @return Converted byte array
     * @throws IOException when the stream cannot be read
     */
    private static byte[] readAllBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int readLen;

        while ((readLen = stream.read(buffer)) != -1) {
            byteStream.write(buffer, 0, readLen);
        }

        return byteStream.toByteArray();
    }
}