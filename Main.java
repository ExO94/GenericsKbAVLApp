/*
  Student Number: FRTETH003
  Name: Ethan Fortuin
  Date: 24/03/25
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static final String filePath = "GenericsKB.txt";

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        AVLTreeDatabase<Entry> database = new AVLTreeDatabase<>(filePath);
        database.readFile();

        boolean exit = false;
        while (!exit)
        {
            System.out.println("\nSelect an option:");
            System.out.println("1. Query a single term");
            System.out.println("2. Query multiple terms from a file");
            System.out.println("3. Display comparison statistics");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice)
            {
                case 1:
                    querySingleTerm(scanner, database);
                    break;
                case 2:
                    queryTermsFromFile(scanner, database);
                    break;
                case 3:
                    database.printInstrumentation();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
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
    private static void queryTermsFromFile(Scanner scanner, AVLTreeDatabase<Entry> database)
    {
        System.out.print("Enter the path to the file containing terms: ");
        String queryFilePath = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(queryFilePath)))
        {
            String line;
            int found = 0;
            int notFound = 0;

            while ((line = br.readLine()) != null)
            {
                String term = line.trim();
                Entry entry = database.find(term);

                if (entry != null)
                {
                    System.out.println("Found: " + entry);
                    found++;
                }
                else
                {
                    System.out.println("Term not found: " + term);
                    notFound++;
                }
            }

            System.out.println("\nSearch results:");
            System.out.println("Total terms searched: " + (found + notFound));
            System.out.println("Terms found: " + found);
            System.out.println("Terms not found: " + notFound);

        }
        catch (IOException e)
        {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Query a single term from user input
     *
     * @param scanner Scanner for user input
     * @param database The AVL tree database to query
     */
    private static void querySingleTerm(Scanner scanner, AVLTreeDatabase<Entry> database)
    {
        System.out.print("Enter a term to search for: ");
        String term = scanner.nextLine();

        Entry entry = database.find(term);

        if (entry != null)
        {
            System.out.println("Found: " + entry);
        }
        else
        {
            System.out.println("Term not found: " + term);
        }
    }

    /**
     * Randomly rearranges lines within a text file.
     *
     * @param inputFileName The file path of the original file.
     * @param outputFileName The file path for the randomized output file.
     * @throws IOException If an I/O error occurs.
     */
    public static void randomizeFileLines(String inputFileName, String outputFileName) throws IOException
    {
        java.util.List<String> lines = new java.util.ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                lines.add(line);
            }
        }

        java.util.Collections.shuffle(lines);

        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(outputFileName)))
        {
            for (String line : lines)
            {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}