package org.sidequest.parley.scripts.analyzers;

import org.apache.commons.io.FileUtils;
import org.sidequest.parley.scripts.model.ProjectStructureModels.ClassStructure;
import org.sidequest.parley.scripts.model.ProjectStructureModels.MethodInfo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CodeAnalyzer {
    private static final Pattern IMPORT_PATTERN =
            Pattern.compile("import\\s+([\\w\\.\\*]+);");
    private static final Pattern ANNOTATION_PATTERN =
            Pattern.compile("@([\\w\\.]+)(?:\\([^)]*\\))?");
    private static final Pattern METHOD_PATTERN = Pattern.compile(
            "(?<visibility>public|private|protected)?\\s*" +
                    "(?<static>static\\s+)?" +
                    "(?<final>final\\s+)?" +
                    "(?<async>async\\s+)?" +
                    "(?<generic><[^>]+>\\s*)?" +
                    "(?<returnType>[\\w\\<\\>\\[\\]\\s\\.]+)\\s+" +
                    "(?<name>\\w+)\\s*" +
                    "\\((?<params>[^)]*)\\)\\s*" +
                    "(?<throws>throws\\s+[\\w\\s,\\.]+)?\\s*" +
                    "(?:\\{|;)", // Handle both method bodies and interface methods
            Pattern.MULTILINE
    );

    private static final Pattern METHOD_ANNOTATION_PATTERN = Pattern.compile(
            "@([\\w\\.]+)(?:\\([^)]*\\))?\\s*$",
            Pattern.MULTILINE
    );

    public static ClassStructure analyzeJavaFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Invalid file provided for analysis");
        }

        ClassStructure cls = new ClassStructure();
        cls.name = file.getName().replace(".java", "");
        cls.path = file.getAbsolutePath();

        try {
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            cls.content = content;

            cls.imports = extractImports(content);
            cls.annotations = extractAnnotations(content);
            cls.type = determineClassType(content);
            cls.methods = extractMethods(content);
            cls.businessRules = extractBusinessRules(content);
            cls.relatedClasses = findRelatedClasses(content);
            cls.dependencies = findDependencies(content);

            return cls;
        } catch (IOException e) {
            throw new IOException("Failed to analyze Java file: " + file.getName(), e);
        }
    }

    public static List<String> extractImports(String content) {
        if (content == null || content.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> imports = new ArrayList<>();
        Matcher matcher = IMPORT_PATTERN.matcher(content);
        while (matcher.find()) {
            imports.add(matcher.group(1));
        }
        return imports;
    }

    public static List<String> extractAnnotations(String content) {
        if (content == null || content.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> annotations = new ArrayList<>();
        Matcher matcher = ANNOTATION_PATTERN.matcher(content);
        while (matcher.find()) {
            annotations.add(matcher.group());
        }
        return annotations;
    }

    public static List<MethodInfo> extractMethods(String content) {
        if (content == null || content.isEmpty()) {
            return new ArrayList<>();
        }

        List<MethodInfo> methods = new ArrayList<>();
        Matcher matcher = METHOD_PATTERN.matcher(content);

        while (matcher.find()) {
            try {
                MethodInfo method = new MethodInfo();
                method.name = matcher.group("name");

                String returnType = matcher.group("returnType");
                String generic = matcher.group("generic");
                method.returnType = (generic != null) ? generic.trim() + " " + returnType.trim() : returnType.trim();

                method.visibility = matcher.group("visibility") != null ?
                        matcher.group("visibility") : "package-private";
                method.isAsync = matcher.group("async") != null;
                method.parameters = parseParameters(matcher.group("params"));
                method.annotations = extractMethodAnnotations(
                        content.substring(Math.max(0, matcher.start() - 200), matcher.start())
                );
                method.exceptions = parseExceptions(matcher.group("throws"));
                methods.add(method);
            } catch (Exception e) {
                System.err.println("Error parsing method at position " + matcher.start() + ": " + e.getMessage());
            }
        }
        return methods;
    }

    private static List<String> extractMethodAnnotations(String precedingContent) {
        if (precedingContent == null || precedingContent.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> annotations = new ArrayList<>();
        Matcher matcher = METHOD_ANNOTATION_PATTERN.matcher(precedingContent);
        while (matcher.find()) {
            annotations.add(matcher.group());
        }
        return annotations;
    }

    private static List<String> parseParameters(String params) {
        if (params == null || params.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(params.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static List<String> parseExceptions(String throwsClause) {
        if (throwsClause == null || throwsClause.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(throwsClause.replace("throws", "").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public static List<String> extractBusinessRules(String content) {
        if (content == null || content.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> rules = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                "/\\*\\*\\s*(.*?)\\s*\\*/|//\\s*(.+)",
                Pattern.DOTALL | Pattern.MULTILINE
        );
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            try {
                String comment = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
                if (comment != null && (
                        comment.toLowerCase().contains("rule") ||
                                comment.toLowerCase().contains("validation") ||
                                comment.toLowerCase().contains("requirement"))) {
                    rules.add(comment.trim());
                }
            } catch (Exception e) {
                System.err.println("Error parsing business rule comment: " + e.getMessage());
            }
        }
        return rules;
    }

    public static String determineClassType(String content) {
        if (content == null || content.isEmpty()) {
            return "unknown";
        }

        // Use more precise pattern matching to avoid false positives
        Pattern classPattern = Pattern.compile("\\s(class|interface|enum|@interface|record)\\s");
        Matcher matcher = classPattern.matcher(content);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "class"; // default to class if no specific type is found
    }

    private static List<String> findRelatedClasses(String content) {
        if (content == null || content.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> related = new HashSet<>();
        Pattern pattern = Pattern.compile(
                "(?:extends|implements|<)\\s+([\\w\\s,\\.]+)(?:>|\\{|\\s)"
        );
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            Arrays.stream(matcher.group(1).split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(related::add);
        }
        return new ArrayList<>(related);
    }

    private static List<String> findDependencies(String content) {
        if (content == null || content.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> dependencies = new HashSet<>();

        // Spring Framework dependencies
        if (content.contains("@Autowired") ||
                content.contains("@Component") ||
                content.contains("@Service") ||
                content.contains("@Repository")) {
            dependencies.add("Spring Framework");
        }

        // JPA dependencies
        if (content.contains("@Entity") ||
                content.contains("@Table") ||
                content.contains("@Column")) {
            dependencies.add("JPA");
        }

        // Security dependencies
        if (content.contains("@Secured") ||
                content.contains("@PreAuthorize") ||
                content.contains("@RolesAllowed")) {
            dependencies.add("Spring Security");
        }

        // Testing dependencies
        if (content.contains("@Test") ||
                content.contains("@Mock") ||
                content.contains("@MockBean")) {
            dependencies.add("Testing Framework");
        }

        return new ArrayList<>(dependencies);
    }
}