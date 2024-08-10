package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileManager {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the File Manager!");
        while (true) {
            displayMenu();
            int choice = getMenuChoice();

            try {
                switch (choice) {
                    case 1: listDirectory(); break;
                    case 2: copyFile(); break;
                    case 3: moveFile(); break;
                    case 4: deleteFile(); break;
                    case 5: searchFiles(); break;
                    case 6: createDirectory(); break;
                    case 7: deleteDirectory(); break;
                    case 8:
                        System.out.println("Thank you for using File Manager. Goodbye!");
                        return;
                    default: System.out.println("Invalid option. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again or choose a different operation.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n--- File Manager Menu ---");
        System.out.println("1. List Directory");
        System.out.println("2. Copy File");
        System.out.println("3. Move File");
        System.out.println("4. Delete File");
        System.out.println("5. Search Files");
        System.out.println("6. Create Directory");
        System.out.println("7. Delete Directory");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getMenuChoice() {
        int choice = -1;
        while (choice < 1 || choice > 8) {
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline left-over
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 8.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        return choice;
    }

    private static void listDirectory() throws IOException {
        System.out.print("Enter directory path to list: ");
        Path dir = Paths.get(scanner.nextLine());
        if (Files.exists(dir) && Files.isDirectory(dir)) {
            System.out.println("Listing contents of directory: " + dir);
            try (var stream = Files.list(dir)) {
                stream.forEach(System.out::println);
            }
            System.out.println("Directory listing complete.");
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }
    }

    private static void copyFile() throws IOException {
        System.out.print("Enter source file path: ");
        Path source = Paths.get(scanner.nextLine());
        System.out.print("Enter destination file path (including filename): ");
        Path dest = Paths.get(scanner.nextLine());

        if (Files.exists(source) && Files.isRegularFile(source)) {
            System.out.println("Copying file from " + source + " to " + dest);
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully.");
        } else {
            System.out.println("Source file does not exist or is not a file.");
        }
    }


    private static void moveFile() throws IOException {
        System.out.print("Enter source file path: ");
        Path source = Paths.get(scanner.nextLine());
        System.out.print("Enter destination file path (including filename): ");
        Path dest = Paths.get(scanner.nextLine());

        if (Files.exists(source) && Files.isRegularFile(source)) {
            if (Files.isDirectory(dest)) {
                System.out.println("Destination path must include a filename.");
            } else {
                System.out.println("Moving file from " + source + " to " + dest);
                Files.move(source, dest, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File moved successfully.");
            }
        } else {
            System.out.println("Source file does not exist or is not a file.");
        }
    }


    private static void deleteFile() throws IOException {
        System.out.print("Enter path of file to delete: ");
        Path file = Paths.get(scanner.nextLine());
        if (Files.exists(file) && Files.isRegularFile(file)) {
            System.out.println("Attempting to delete file: " + file);
            Files.delete(file);
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("File does not exist or is not a file.");
        }
    }

    private static void searchFiles() throws IOException {
        System.out.print("Enter directory path to search: ");
        Path dir = Paths.get(scanner.nextLine());
        System.out.print("Enter file name or extension to search for: ");
        String searchTerm = scanner.nextLine();

        if (Files.exists(dir) && Files.isDirectory(dir)) {
            System.out.println("Searching for files in " + dir + " with term: " + searchTerm);
            try (Stream<Path> stream = Files.walk(dir)) {
                stream.filter(p -> p.getFileName().toString().contains(searchTerm))
                        .forEach(System.out::println);
            }
            System.out.println("Search complete.");
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }
    }


    private static void createDirectory() throws IOException {
        System.out.print("Enter path of directory to create (Include the name of directory: ");
        Path dir = Paths.get(scanner.nextLine());
        if (Files.exists(dir)) {
            System.out.println("Directory already exists.");
        } else {
            System.out.println("Attempting to create directory: " + dir);
            Files.createDirectory(dir);
            System.out.println("Directory created successfully.");
        }
    }

    private static void deleteDirectory() throws IOException {
        System.out.print("Enter path of directory to delete: ");
        Path dir = Paths.get(scanner.nextLine());
        if (Files.exists(dir) && Files.isDirectory(dir)) {
            try (var stream = Files.list(dir)) {
                if (stream.findAny().isEmpty()) { // Check if directory is empty
                    System.out.println("Attempting to delete directory: " + dir);
                    Files.delete(dir);
                    System.out.println("Directory deleted successfully.");
                } else {
                    System.out.println("Directory is not empty. Cannot delete.");
                }
            }
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }
    }

}
