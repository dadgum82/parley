package org.sidequest.parley.scripts.analyzers;

import org.sidequest.parley.scripts.model.ProjectStructureModels.ColumnInfo;
import org.sidequest.parley.scripts.model.ProjectStructureModels.TableInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseAnalyzer {
    private static final Pattern TABLE_PATTERN =
            Pattern.compile("@Table\\(name\\s*=\\s*\"([^\"]+)\"\\)");
    private static final Pattern COLUMN_PATTERN =
            Pattern.compile("@Column\\(([^)]+)\\)\\s+private\\s+([^\\s]+)\\s+([^\\s;]+)");
    private static final Pattern ID_PATTERN =
            Pattern.compile("@Id");
    private static final Pattern MANY_TO_ONE_PATTERN =
            Pattern.compile("@ManyToOne[^)]*\\)[^\\n]*\\n[^@]*@JoinColumn\\(([^)]+)\\)");

    public static TableInfo extractTableInfo(String content) {
        TableInfo table = new TableInfo();
        table.columns = new ArrayList<>();
        table.relationships = new ArrayList<>();

        // Extract table name
        Matcher tableMatcher = TABLE_PATTERN.matcher(content);
        if (tableMatcher.find()) {
            table.name = tableMatcher.group(1);
        }

        // Extract columns
        String[] lines = content.split("\\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            // Check for ID
            boolean isId = ID_PATTERN.matcher(line).find();

            // Extract column info
            Matcher columnMatcher = COLUMN_PATTERN.matcher(line);
            if (columnMatcher.find()) {
                ColumnInfo column = new ColumnInfo();
                column.isPrimaryKey = isId;
                parseColumnAttributes(columnMatcher.group(1), column);
                column.type = columnMatcher.group(2);
                column.name = columnMatcher.group(3);
                table.columns.add(column);
            }

            // Extract relationships
            Matcher relationMatcher = MANY_TO_ONE_PATTERN.matcher(line);
            if (relationMatcher.find()) {
                String relationshipInfo = relationshipInfo(relationMatcher.group(1));
                table.relationships.add(relationshipInfo);
            }
        }

        return table;
    }

    private static void parseColumnAttributes(String attributes, ColumnInfo column) {
        // Parse nullability
        column.isNullable = !attributes.contains("nullable = false");

        // Parse length if present
        Pattern lengthPattern = Pattern.compile("length\\s*=\\s*(\\d+)");
        Matcher lengthMatcher = lengthPattern.matcher(attributes);
        if (lengthMatcher.find()) {
            // You might want to store this somewhere
        }

        // Parse default value if present
        Pattern defaultPattern = Pattern.compile("columnDefinition\\s*=\\s*\"([^\"]+)\"");
        Matcher defaultMatcher = defaultPattern.matcher(attributes);
        if (defaultMatcher.find()) {
            column.defaultValue = defaultMatcher.group(1);
        }
    }

    private static String relationshipInfo(String attributes) {
        Pattern namePattern = Pattern.compile("name\\s*=\\s*\"([^\"]+)\"");
        Matcher nameMatcher = namePattern.matcher(attributes);
        if (nameMatcher.find()) {
            return "References table via " + nameMatcher.group(1);
        }
        return "Unknown relationship";
    }
}