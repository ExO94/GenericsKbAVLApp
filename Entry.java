/*
  Student Number: FRTETH003
  Name : Ethan Fortuin
  Date : 24/03/25
*/

/**
 * Represents an entry in the AVL tree database.
 * Each entry contains a term, a tree, and a confidence value.
 * Entries are comparable based on their terms.
 */
public class Entry implements Comparable<Entry>
{
    public String term;
    public String tree;
    public double confidence;

    /**
     * Constructs a new Entry with the specified term, tree, and confidence.
     *
     * @param term       The term or key of the entry.
     * @param tree       The tree associated with the entry.
     * @param confidence The confidence value of the entry.
     */
    public Entry(String term, String tree, double confidence)
    {
        this.term = term;
        this.tree = tree;
        this.confidence = confidence;
    }

    /**
     * Compares this entry with another entry based on their terms.
     *
     * @param other The other entry to compare with.
     * @return A negative integer, zero, or a positive integer as this entry's term
     *         is less than, equal to, or greater than the specified entry's term.
     */
    @Override
    public int compareTo(Entry other)
    {
        return this.term.compareTo(other.term);
    }

    /**
     * Returns a string representation of this entry.
     *
     * @return A string representation of the entry in the format "term: tree (confidence)".
     */
    @Override
    public String toString()
    {
        return term + ": " + tree + " (" + confidence + ")";
    }
}