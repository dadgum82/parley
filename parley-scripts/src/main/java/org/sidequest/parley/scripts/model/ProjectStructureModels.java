package org.sidequest.parley.scripts.model;

import java.util.List;
import java.util.Map;

public class ProjectStructureModels {
    public static class ProjectStructure {
        public String projectName;
        public String rootPath;
        public List<ModuleStructure> modules;
        public List<String> dependencies;
        public Map<String, List<String>> designPatterns;
        public Map<String, List<String>> securityFeatures;
        public Map<String, String> configurations;
        public Map<String, TableInfo> dbSchema;
    }

    public static class ModuleStructure {
        public String name;
        public String path;
        public List<PackageStructure> packages;
        public Map<String, String> resources;
        public List<String> dependencies;
        public Map<String, ApiEndpointInfo> apiEndpoints;
    }

    public static class PackageStructure {
        public String name;
        public List<ClassStructure> classes;
    }

    public static class ClassStructure {
        public String name;
        public String path;
        public String type;
        public List<String> imports;
        public List<String> annotations;
        public String content;
        public List<MethodInfo> methods;
        public List<String> businessRules;
        public List<String> relatedClasses;
        public List<String> dependencies;
    }

    public static class MethodInfo {
        public String name;
        public String returnType;
        public List<String> parameters;
        public List<String> annotations;
        public String visibility;
        public boolean isAsync;
        public List<String> exceptions;
    }

    public static class ApiEndpointInfo {
        public String path;
        public String method;
        public String description;
        public List<String> parameters;
        public String requestBody;
        public String responseType;
    }

    public static class TableInfo {
        public String name;
        public List<ColumnInfo> columns;
        public List<String> relationships;
    }

    public static class ColumnInfo {
        public String name;
        public String type;
        public boolean isPrimaryKey;
        public boolean isForeignKey;
        public String referencedTable;
        public String referencedColumn;
        public boolean isNullable;
        public String defaultValue;
    }
}