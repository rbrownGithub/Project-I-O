package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * FileManager is a console application for basic file management operations.
 * It allows users to list, copy, move, delete files, search files, and manage directories.
 */
public class FileManager {
    // Scanner for reading user input
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Main method that runs the file manager application.
     * It displays a menu and processes user input in a loop until the user chooses to exit.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the File Manager!");
        while (true) {
            displayMenu(); // Display the menu options
            int choice = getMenuChoice(); // Get the user's choice

            try {
                // Execute the chosen operation
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
                        return; // Exit the loop and end the program
                    default: System.out.println("Invalid option. Please try again.");
                }
            } catch (IOException e) {
                // Catch and handle any IOExceptions that occur during file operations
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again or choose a different operation.");
            }
        }
    }

    /**
     * Displays the main menu of the application.
     */
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

    /**
     * Prompts the user to enter a menu choice and validates the input.
     * @return The validated menu choice.
     */
    private static int getMenuChoice() {
        int choice = -1;
        while (choice < 1 || choice > 8) {
            try {
                choice = scanner.nextInt(); // Read user input
                scanner.nextLine(); // Consume newline left-over
            } catch (InputMismatchException e) {
                // Handle invalid input (non-integer values)
                System.out.println("Invalid input. Please enter a number between 1 and 8.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        return choice;
    }

    /**
     * Lists the contents of a specified directory.
     * @throws IOException if an I/O error occurs
     */
    private static void listDirectory() throws IOException {
        System.out.print("Enter directory path to list: ");
        Path dir = Paths.get(scanner.nextLine()); // Read directory path from user
        if (Files.exists(dir) && Files.isDirectory(dir)) {
            System.out.println("Listing contents of directory: " + dir);
            try (var stream = Files.list(dir)) {
                stream.forEach(System.out::println); // Print each file/directory in the directory
            }
            System.out.println("Directory listing complete.");
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }
    }

    /**
     * Copies a file from one location to another.
     * @throws IOException if an I/O error occurs
     */
    private static void copyFile() throws IOException {
        System.out.print("Enter source file path: ");
        Path source = Paths.get(scanner.nextLine()); // Read source file path from user
        System.out.print("Enter destination file path (including filename): ");
        Path dest = Paths.get(scanner.nextLine()); // Read destination file path from user

        if (Files.exists(source) && Files.isRegularFile(source)) {
            System.out.println("Copying file from " + source + " to " + dest);
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING); // Copy file, replace if exists
            System.out.println("File copied successfully.");
        } else {
            System.out.println("Source file does not exist or is not a file.");
        }
    }

    /**
     * Moves a file from one location to another.
     * @throws IOException if an I/O error occurs
     */
    private static void moveFile() throws IOException {
        System.out.print("Enter source file path: ");
        Path source = Paths.get(scanner.nextLine()); // Read source file path from user
        System.out.print("Enter destination file path (including filename): ");
        Path dest = Paths.get(scanner.nextLine()); // Read destination file path from user

        if (Files.exists(source) && Files.isRegularFile(source)) {
            if (Files.isDirectory(dest)) {
                System.out.println("Destination path must include a filename.");
            } else {
                System.out.println("Moving file from " + source + " to " + dest);
                Files.move(source, dest, StandardCopyOption.REPLACE_EXISTING); // Move file, replace if exists
                System.out.println("File moved successfully.");
            }
        } else {
            System.out.println("Source file does not exist or is not a file.");
        }
    }

    /**
     * Deletes a specified file.
     * @throws IOException if an I/O error occurs
     */
    private static void deleteFile() throws IOException {
        System.out.print("Enter path of file to delete: ");
        Path file = Paths.get(scanner.nextLine()); // Read file path from user
        if (Files.exists(file) && Files.isRegularFile(file)) {
            System.out.println("Attempting to delete file: " + file);
            Files.delete(file); // Delete the file
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("File does not exist or is not a file.");
        }
    }

    /**
     * Searches for files within a specified directory based on file name or extension.
     * @throws IOException if an I/O error occurs
     */
    private static void searchFiles() throws IOException {
        System.out.print("Enter directory path to search: ");
        Path dir = Paths.get(scanner.nextLine()); // Read directory path from user
        System.out.print("Enter file name or extension to search for: ");
        String searchTerm = scanner.nextLine(); // Read search term from user

        if (Files.exists(dir) && Files.isDirectory(dir)) {
            System.out.println("Searching for files in " + dir + " with term: " + searchTerm);
            try (Stream<Path> stream = Files.walk(dir)) {
                stream.filter(p -> p.getFileName().toString().contains(searchTerm))
                        .forEach(System.out::println); // Print files matching search term
            }
            System.out.println("Search complete.");
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }
    }

    /**
     * Creates a new directory.
     * @throws IOException if an I/O error occurs
     */
    private static void createDirectory() throws IOException {
        System.out.print("Enter path of directory to create (Include the name of directory): ");
        Path dir = Paths.get(scanner.nextLine()); // Read directory path from user
        if (Files.exists(dir)) {
            System.out.println("Directory already exists.");
        } else {
            System.out.println("Attempting to create directory: " + dir);
            Files.createDirectory(dir); // Create the directory
            System.out.println("Directory created successfully.");
        }
    }

    /**
     * Deletes a specified directory if it is empty.
     * @throws IOException if an I/O error occurs
     */
    private static void deleteDirectory() throws IOException {
        System.out.print("Enter path of directory to delete: ");
        Path dir = Paths.get(scanner.nextLine()); // Read directory path from user
        if (Files.exists(dir) && Files.isDirectory(dir)) {
            // Check if the directory is empty
            try (var stream = Files.list(dir)) {
                if (stream.findAny().isEmpty()) {
                    System.out.println("Attempting to delete directory: " + dir);
                    Files.delete(dir); // Delete the directory
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
