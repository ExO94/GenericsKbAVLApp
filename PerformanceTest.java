/*
  Student Number: FRTETH003
  Name: Ethan Fortuin
  Date: 28/03/25
*/

import java.io.*;
import java.util.*;

/**
 * This class is used to test and analyze the performance of AVL tree operations.
 * It runs experiments with varying dataset sizes and measures the number of comparisons
 * needed for insert and search operations.
 */
public class PerformanceTest
{
    private static final String FULL_DATA_FILE = "GenericsKB.txt";
    private static final String QUERY_FILE = "GenericsKB-queries.txt";
    private static final int[] DATASET_SIZES = {5, 50, 500, 5000, 50000};

    public static void main(String[] args)
    {
        try
        {
            runExperiment();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Runs the performance experiment for all dataset sizes and writes results to a CSV file.
     * @throws IOException If an I/O error occurs during file operations
     */
    private static void runExperiment() throws IOException
    {
        List<Entry> allEntries = loadAllEntries();
        List<String> queries = loadQueries();

        // Map to store results for each dataset size
        Map<Integer, ExperimentResult> results = new TreeMap<>();

        System.out.println("n,InsertMinComps,InsertAvgComps,InsertMaxComps,SearchMinComps,SearchAvgComps,SearchMaxComps");

        for (int n : DATASET_SIZES)
        {
            List<Entry> randomSubset = createRandomSubset(allEntries, n);
            ExperimentResult result = runExperimentWithSubset(randomSubset, queries);

            results.put(n, result);

            System.out.println(n + "," +
                    result.insertMinComps + "," +
                    result.insertAvgComps + "," +
                    result.insertMaxComps + "," +
                    result.searchMinComps + "," +
                    result.searchAvgComps + "," +
                    result.searchMaxComps);
        }
        writeResultsToCSV(results);
        System.out.println("\nResults have been written to results.csv");
    }


    /**
     * Loads all entries from the full data file.
     *
     * @return A list containing all entries from the data file
     * @throws IOException If an I/O error occurs while reading the file
     */
    private static List<Entry> loadAllEntries() throws IOException
    {
        List<Entry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FULL_DATA_FILE)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    String term = parts[0];
                    String tree = parts[1];
                    double confidence = Double.parseDouble(parts[2]);
                    entries.add(new Entry(term, tree, confidence));
                }
            }
        }
        return entries;
    }

    /**
     * Loads query terms from the query file.
     *
     * @return A list of query terms to be searched for
     * @throws IOException If an I/O error occurs while reading the file
     */
    private static List<String> loadQueries() throws IOException
    {
        List<String> queries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(QUERY_FILE)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                queries.add(line.trim());
            }
        }
        return queries;
    }

    /**
     * Creates a random subset of entries of the specified size.
     *
     * @param allEntries The complete list of entries
     * @param n The size of the subset to create
     * @return A random subset of entries with size n
     */
    private static List<Entry> createRandomSubset(List<Entry> allEntries, int n)
    {
        List<Entry> entriesCopy = new ArrayList<>(allEntries);
        Collections.shuffle(entriesCopy);
        return entriesCopy.subList(0, Math.min(n, entriesCopy.size()));
    }

    /**
     * Runs a single experiment with the given subset of entries and queries.
     *
     * @param entries The entries to insert into the AVL tree
     * @param queries The terms to search for in the AVL tree
     * @return An ExperimentResult containing statistics about the experiment
     */
    private static ExperimentResult runExperimentWithSubset(List<Entry> entries, List<String> queries)
    {

        AVLTreeDatabase<Entry> database = new AVLTreeDatabase<>(null);

        List<Integer> insertComparisons = new ArrayList<>();

        for (Entry entry : entries)
        {
            database.resetCounters();
            database.insert(entry);
            insertComparisons.add(database.getInsertComparisons());
        }

        List<Integer> searchComparisons = new ArrayList<>();
        for (String query : queries)
        {
            database.resetCounters();
            database.search(new Entry(query, "", 0.0));
            searchComparisons.add(database.getSearchComparisons());
        }
        return new ExperimentResult(
                Collections.min(insertComparisons),
                calculateAverage(insertComparisons),
                Collections.max(insertComparisons),
                Collections.min(searchComparisons),
                calculateAverage(searchComparisons),
                Collections.max(searchComparisons));
    }

    /**
     * Calculates the average value from a list of integers.
     *
     * @param values The list of integer values
     * @return The average of the values
     */
    private static double calculateAverage(List<Integer> values)
    {
        return values.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    /**
     * Writes the experiment results to a CSV file that can be used for plotting.
     *
     * @param results A map containing dataset sizes and their corresponding experiment results
     * @throws IOException If an I/O error occurs during file writing
     */
    private static void writeResultsToCSV(Map<Integer, ExperimentResult> results) throws IOException
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter("results.csv")))
        {
            writer.println("n,InsertMinComps,InsertAvgComps,InsertMaxComps,SearchMinComps,SearchAvgComps,SearchMaxComps");

            // Sort keys to ensure ordered output by dataset size
            List<Integer> sortedKeys = new ArrayList<>(results.keySet());
            Collections.sort(sortedKeys);

            for (Integer n : sortedKeys)
            {
                ExperimentResult result = results.get(n);
                writer.println(n + "," +
                        result.insertMinComps + "," +
                        result.insertAvgComps + "," +
                        result.insertMaxComps + "," +
                        result.searchMinComps + "," +
                        result.searchAvgComps + "," +
                        result.searchMaxComps);
            }
        }
    }

    /**
     * A class to store the results of a performance experiment.
     * Contains minimum, average, and maximum comparison counts for both insert and search operations.
     */
    static class ExperimentResult
    {
        int insertMinComps;
        double insertAvgComps;
        int insertMaxComps;
        int searchMinComps;
        double searchAvgComps;
        int searchMaxComps;

        public ExperimentResult(int insertMinComps, double insertAvgComps, int insertMaxComps,
                                int searchMinComps, double searchAvgComps, int searchMaxComps)
        {
            this.insertMinComps = insertMinComps;
            this.insertAvgComps = insertAvgComps;
            this.insertMaxComps = insertMaxComps;
            this.searchMinComps = searchMinComps;
            this.searchAvgComps = searchAvgComps;
            this.searchMaxComps = searchMaxComps;
        }
    }
}