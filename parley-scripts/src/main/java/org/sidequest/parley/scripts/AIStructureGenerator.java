package org.sidequest.parley.scripts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.sidequest.parley.scripts.analyzers.CodeAnalyzer;
import org.sidequest.parley.scripts.analyzers.DatabaseAnalyzer;
import org.sidequest.parley.scripts.model.ProjectStructureModels.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AIStructureGenerator {
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static final List<String> IGNORE_DIRECTORIES = Arrays.asList(
            ".git", ".idea", "node_modules", "target", "bin", "build", "dist");

    public static void main(String[] args) {
        try {
            String projectRoot = findProjectRoot();
            if (projectRoot == null) {
                System.out.println("Could not find project root directory.");
                return;
            }

            ProjectStructure structure = analyzeProject(new File(projectRoot));
            String json = mapper.writeValueAsString(structure);

            // Write to file
            Path outputPath = Paths.get(projectRoot, "ai-project-structure.json");
            Files.write(outputPath, json.getBytes());
            System.out.println("AI-optimized project structure written to: " + outputPath);
        } catch (Exception e) {
            System.err.println("Error generating project structure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String findProjectRoot() {
        Path current = Paths.get("").toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml")) ||
                    Files.exists(current.resolve("build.gradle")) ||
                    Files.exists(current.resolve("package.json"))) {
                return current.toString();
            }
            current = current.getParent();
        }
        return null;
    }

    private static ProjectStructure analyzeProject(File rootDir) throws IOException {
        ProjectStructure structure = new ProjectStructure();
        structure.projectName = rootDir.getName();
        structure.rootPath = rootDir.getAbsolutePath();
        structure.modules = new ArrayList<>();
        structure.dependencies = new ArrayList<>();
        structure.designPatterns = new HashMap<>();
        structure.securityFeatures = new HashMap<>();
        structure.configurations = new HashMap<>();
        structure.dbSchema = new HashMap<>();

        // Analyze project configuration files
        analyzeProjectConfiguration(rootDir, structure);

        // Find and analyze all modules
        findModules(rootDir).forEach(moduleDir -> {
            try {
                ModuleStructure module = analyzeModule(moduleDir);
                structure.modules.add(module);
            } catch (IOException e) {
                System.err.println("Error analyzing module: " + moduleDir + " - " + e.getMessage());
            }
        });

        return structure;
    }

    private static void analyzeProjectConfiguration(File rootDir, ProjectStructure structure) throws IOException {
        // Analyze Maven POM
        File pomFile = new File(rootDir, "pom.xml");
        if (pomFile.exists()) {
            analyzePomFile(pomFile, structure);
        }

        // Analyze Gradle build file
        File gradleFile = new File(rootDir, "build.gradle");
        if (gradleFile.exists()) {
            analyzeGradleFile(gradleFile, structure);
        }

        // Analyze application properties/yaml files
        analyzePropertyFiles(rootDir, structure);
    }

    private static List<File> findModules(File rootDir) {
        List<File> modules = new ArrayList<>();
        if (isModuleDirectory(rootDir)) {
            modules.add(rootDir);
        }

        File[] files = rootDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && !IGNORE_DIRECTORIES.contains(file.getName())) {
                    modules.addAll(findModules(file));
                }
            }
        }
        return modules;
    }

    private static boolean isModuleDirectory(File dir) {
        return dir.isDirectory() && (
                new File(dir, "pom.xml").exists() ||
                        new File(dir, "build.gradle").exists() ||
                        new File(dir, "src").exists()
        );
    }

    private static ModuleStructure analyzeModule(File moduleDir) throws IOException {
        ModuleStructure module = new ModuleStructure();
        module.name = moduleDir.getName();
        module.path = moduleDir.getAbsolutePath();
        module.packages = new ArrayList<>();
        module.resources = new HashMap<>();
        module.dependencies = new ArrayList<>();
        module.apiEndpoints = new HashMap<>();

        // Analyze source files
        File srcDir = new File(moduleDir, "src/main/java");
        if (srcDir.exists()) {
            analyzeSourceDirectory(srcDir, module);
        }

        // Analyze resources
        File resourcesDir = new File(moduleDir, "src/main/resources");
        if (resourcesDir.exists()) {
            analyzeResourceDirectory(resourcesDir, module);
        }

        return module;
    }

    private static void analyzeSourceDirectory(File srcDir, ModuleStructure module) throws IOException {
        Map<String, PackageStructure> packageMap = new HashMap<>();

        Files.walk(srcDir.toPath())
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        File file = path.toFile();
                        String packageName = calculatePackageName(srcDir, file);
                        PackageStructure pkg = packageMap.computeIfAbsent(packageName,
                                k -> new PackageStructure() {{
                                    name = k;
                                    classes = new ArrayList<>();
                                }});

                        ClassStructure cls = CodeAnalyzer.analyzeJavaFile(file);
                        pkg.classes.add(cls);

                        // Check for database entities
                        if (cls.annotations.stream().anyMatch(a -> a.contains("@Entity"))) {
                            String content = new String(Files.readAllBytes(path));
                            TableInfo tableInfo = DatabaseAnalyzer.extractTableInfo(content);
                            if (tableInfo.name != null) {
                                module.resources.put("db_table_" + tableInfo.name, path.toString());
                            }
                        }

                        // Check for API endpoints
                        analyzeApiEndpoints(cls, module);

                    } catch (IOException e) {
                        System.err.println("Error analyzing file: " + path + " - " + e.getMessage());
                    }
                });

        module.packages.addAll(packageMap.values());
    }

    private static String calculatePackageName(File srcDir, File file) {
        String relativePath = file.getParentFile().getAbsolutePath()
                .substring(srcDir.getAbsolutePath().length() + 1);
        return relativePath.replace(File.separatorChar, '.');
    }

    private static void analyzeApiEndpoints(ClassStructure cls, ModuleStructure module) {
        if (cls.annotations.stream().anyMatch(a -> a.contains("@RestController") ||
                a.contains("@Controller"))) {
            cls.methods.forEach(method -> {
                List<String> mappings = method.annotations.stream()
                        .filter(a -> a.contains("Mapping"))
                        .collect(Collectors.toList());

                if (!mappings.isEmpty()) {
                    ApiEndpointInfo endpoint = new ApiEndpointInfo();
                    endpoint.method = extractHttpMethod(mappings.get(0));
                    endpoint.path = extractPath(mappings.get(0));
                    endpoint.parameters = method.parameters;
                    endpoint.responseType = method.returnType;

                    module.apiEndpoints.put(endpoint.method + " " + endpoint.path, endpoint);
                }
            });
        }
    }

    private static String extractHttpMethod(String mapping) {
        if (mapping.contains("GetMapping")) return "GET";
        if (mapping.contains("PostMapping")) return "POST";
        if (mapping.contains("PutMapping")) return "PUT";
        if (mapping.contains("DeleteMapping")) return "DELETE";
        if (mapping.contains("PatchMapping")) return "PATCH";
        return "UNKNOWN";
    }

    private static String extractPath(String mapping) {
        int startIndex = mapping.indexOf("(");
        int endIndex = mapping.lastIndexOf(")");
        if (startIndex > 0 && endIndex > startIndex) {
            String content = mapping.substring(startIndex + 1, endIndex);
            if (content.contains("value = ")) {
                return content.substring(content.indexOf("\"") + 1, content.lastIndexOf("\""));
            }
            if (content.startsWith("\"")) {
                return content.substring(1, content.lastIndexOf("\""));
            }
        }
        return "/";
    }

    private static void analyzeResourceDirectory(File resourcesDir, ModuleStructure module) throws IOException {
        Files.walk(resourcesDir.toPath())
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    String relativePath = resourcesDir.toPath().relativize(path).toString();
                    module.resources.put(relativePath, path.toString());
                });
    }

    private static void analyzePomFile(File pomFile, ProjectStructure structure) throws IOException {
        // Add basic Maven project analysis
        structure.dependencies.add("Maven");
        // TODO: Add detailed POM analysis
    }

    private static void analyzeGradleFile(File gradleFile, ProjectStructure structure) throws IOException {
        // Add basic Gradle project analysis
        structure.dependencies.add("Gradle");
        // TODO: Add detailed Gradle build file analysis
    }

    private static void analyzePropertyFiles(File rootDir, ProjectStructure structure) throws IOException {
        // Analyze application.properties/yaml files
        File[] propertyFiles = rootDir.listFiles((dir, name) ->
                name.startsWith("application.") &&
                        (name.endsWith(".properties") || name.endsWith(".yml") || name.endsWith(".yaml"))
        );

        if (propertyFiles != null) {
            for (File file : propertyFiles) {
                structure.configurations.put(file.getName(), file.getAbsolutePath());
            }
        }
    }
}