package org.sidequest.parley.scripts;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class FixImportsSQLfile {
    public static void main(String[] args) {
        try {
            String projectRoot = System.getProperty("user.dir");
            File inputFile = new File(projectRoot + "\\parley-service-jpa\\src\\main\\resources\\import.sql");
            Scanner scanner = new Scanner(inputFile);
            PrintWriter writer = new PrintWriter(projectRoot + "\\parley-service-jpa\\src\\main\\resources\\reformatted_import.txt");

            StringBuilder sb = new StringBuilder();
            String previousLine = null;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                sb.append(line);
                // Handles comments and MySQL delimiter
                if (line.startsWith("#") || line.startsWith("--")) {

                    writer.println(sb.toString());
                    writer.println("\n");
                    // Clear the StringBuilder for the next SQL statement
                    sb.setLength(0);
                }

                // If the line ends with a semicolon, it's the end of a SQL statement
                if (line.endsWith(";")) {
                    writer.println(sb.toString());
                    // Clear the StringBuilder for the next SQL statement
                    sb.setLength(0);
                } else if (!line.isEmpty() || (line.isEmpty() && (previousLine == null || !previousLine.isEmpty()))) {
                    // If the line doesn't end with a semicolon, add a space before the next line
                    sb.append(" ");
                }
                previousLine = line;
            }

            scanner.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}