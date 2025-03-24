/*
    Student Number: FRTETH003
    Name: Ethan Fortuin
    Date: 24/03/25
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AVLTreeMain
{
    public static final String filePath = "GenericsKB.txt";

    public static void main(String[] args)
    {
        AVLTreeDatabase<Entry> database = new AVLTreeDatabase<>(filePath);

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("GenericsKbAVLApp");
        System.out.println("================");


        while (!exit) {
            System.out.println("\nOptions:");
            System.out.println("1. Query terms from a file");
            System.out.println("2. Query a single term");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            int choice;
            try
            {
                choice = Integer.parseInt(scanner.nextLine().trim());
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            switch (choice)
            {
                case 1:
                    queryTermsFromFile(scanner, database);
                    break;
                case 2:
                    querySingleTerm(scanner, database);
                    break;
                case 3:
                    exit = true;
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }

        scanner.close();
    }

    /**
     * Query multiple terms from a file
     *
     * @param scanner Scanner for user input
     * @param database The AVL tree database to query
     */
    private static void queryTermsFromFile(Scanner scanner, AVLTreeDatabase<Entry> database) {
        System.out.print("Enter the name of the text file containing terms: ");
        String queryFileName = scanner.nextLine().trim();

        try (BufferedReader reader = new BufferedReader(new FileReader(queryFileName))) {
            String term;
            int count = 0;
            int found = 0;

            System.out.println("\nResults:");
            System.out.println("========");

            while ((term = reader.readLine()) != null) {
                count++;
                term = term.trim();
                if (!term.isEmpty()) {
                    Entry result = database.find(term);
                    if (result != null) {
                        found++;
                        System.out.println("Term: " + result.term);
                        System.out.println("Tree: " + result.tree);
                        System.out.println("Confidence: (" + result.confidence+")");
                        System.out.println();
                    } else {
                        System.out.println("Term not found: " + term);
                        System.out.println();
                    }
                }
            }

            System.out.println("Summary: Found " + found + " out of " + count + " terms.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.out.println("Make sure the file exists and is accessible.");
        }
    }

    /**
     * Query a single term from user input
     *
     * @param scanner Scanner for user input
     * @param database The AVL tree database to query
     */
    private static void querySingleTerm(Scanner scanner, AVLTreeDatabase<Entry> database) {
        System.out.print("Enter a term to search for: ");
        String term = scanner.nextLine().trim();

        Entry result = database.find(term);
        if (result != null) {
            System.out.println("\nResult:");
            System.out.println("========");
            System.out.println("Term: " + result.term);
            System.out.println("Tree: " + result.tree);
            System.out.println("Confidence:  (" + result.confidence+")");
        } else {
            System.out.println("Term not found: " + term);
        }
    }
}