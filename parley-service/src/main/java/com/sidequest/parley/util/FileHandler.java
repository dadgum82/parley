package com.sidequest.parley.util;

public class FileHandler {
//    private final String DIRECTORY;
//    private final String FILENAME;
//    private final Path filePath;
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public FileHandler(String directory, String fileName) {
////
////        this.DIRECTORY = directory;
////        this.FILENAME = fileName;
////        System.out.println("directory: " + directory);
////        System.out.println("fileName: " + fileName);
////        String fileSeparator = System.getProperty("file.separator");
////        this.filePath = Paths.get(this.DIRECTORY + fileSeparator + this.FILENAME);
////
////    }
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public FileHandler(String directory) {
////        this.DIRECTORY = directory;
////        this.FILENAME = "";
////        this.filePath = Paths.get(this.DIRECTORY);
////    }
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//
//    public List<String> getDirectoryContents() {
//        List<String> directoryContents = new ArrayList<>();
//        File folder = new File(this.DIRECTORY);
//        File[] files = folder.listFiles();
//
//        for (File file : files) {
//            directoryContents.add(file.getName());
//        }
//        return directoryContents;
//    }
//
//    public Path getFilePath() {
//        String fileSeparator = System.getProperty("file.separator");
//        return Paths.get(this.DIRECTORY + fileSeparator + this.FILENAME);
//
//    }
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public boolean fileExists() {
////        Path filePath = this.getFilePath();
////        return Files.exists(filePath);
////    }
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public boolean createFile() {
////        Path filePath = this.getFilePath();
////        try {
////            Files.createFile(filePath);
////            return true;
////        } catch (IOException e) {
////            e.printStackTrace();
////            return false;
////        }
////    }
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//
//    public List<String> readFile() {
//        Path filePath = this.getFilePath();
//        try {
//            return Files.readAllLines(filePath, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public List<String[]> readCSVFile() throws IOException {
//        List<String[]> records = new ArrayList<>();
//        try (CSVParser parser = new CSVParser(new FileReader(this.filePath.toString()), CSVFormat.DEFAULT)) {
//            for (CSVRecord record : parser) {
//                String[] fields = new String[record.size()];
//                for (int i = 0; i < record.size(); i++) {
//                    fields[i] = record.get(i);
//                }
//                records.add(fields);
//            }
//        }
//        return records;
//    }
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public boolean appendMessageToFile(String message) {
////        Path filePath = this.getFilePath();
////        try {
////            String content = message + System.lineSeparator();
////            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
////
////            return true;
////        } catch (IOException e) {
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
//////            e.printStackTrace();
//////            return false;
//////        }
//////    }
////// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
////
////    public boolean appendMessageToFile(String[] content) {
////        try {
////            CSVPrinter printer = new CSVPrinter(new FileWriter(filePath.toString(), true), CSVFormat.DEFAULT);
////            printer.printRecord(content);
////            // Files.write(filePath, content.getBytes(StandardCharsets.UTF_8) ,
////            // StandardOpenOption.APPEND);
////            printer.flush();
////            printer.close();
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /*
//     * CSVPrinter printer = new CSVPrinter(new FileWriter("employees.csv"),
//     * CSVFormat.DEFAULT); printer.printRecord(employee); //create header row
//     * printer.printRecord("FirstName", "LastName", "Email", "Department"); //
//     * create data rows for (String[] employee : employees) {
//     * printer.printRecord(employee); } //close the printer after the file is
//     * complete printer.flush(); printer.close();
    //    */
}
