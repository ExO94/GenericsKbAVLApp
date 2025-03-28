/*
  Student Number: FRTETH003
  Name: Ethan Fortuin
  Date: 23/03/25
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * AVLTreeDatabase is a self-balancing binary search tree that maintains
 * entries in a sorted order while keeping the tree balanced.
 *
 * @param <dataType> The type of data stored in the tree, must be Comparable
 */
public class AVLTreeDatabase<dataType extends Comparable<? super dataType>>
{
    public String filePath;
    public BTNode<dataType> root;
    public int searchComparisonCount = 0;
    public int insertComparisonCount = 0;

    /**
     * Constructs an AVLTreeDatabase with the specified file path.
     *
     * @param filePath The path to the file containing the data
     */
    public AVLTreeDatabase(String filePath)
    {
        this.filePath = filePath;
        root = null;
    }

    /**
     * Performs a left rotation on the given node.
     *
     * @param q The node to rotate
     * @return The new root of the rotated subtree
     */
    public BTNode<dataType> rotateLeft(BTNode<dataType> q)
    {
        BTNode<dataType> p = q.right;
        q.right = p.left;
        p.left = q;
        fixHeight(q);
        fixHeight(p);
        return p;
    }

    /**
     * Performs a right rotation on the given node.
     *
     * @param p The node to rotate
     * @return The new root of the rotated subtree
     */
    public BTNode<dataType> rotateRight(BTNode<dataType> p)
    {
        BTNode<dataType> q = p.left;
        p.left = q.right;
        q.right = p;
        fixHeight(p);
        fixHeight(q);
        return q;
    }

    /**
     * Inserts a new element into the AVL tree.
     *
     * @param d The element to insert
     */
    public void insert(dataType d)
    {
        root = insert(d, root);
    }

    /**
     * Recursive helper method to insert an element into the AVL tree.
     *
     * @param d The element to insert
     * @param node The current node in the recursion
     * @return The new root of the subtree after insertion and balancing
     */
    public BTNode<dataType> insert(dataType d, BTNode<dataType> node)
    {
        if (node == null)
            return new BTNode<dataType>(d);

        insertComparisonCount++;
        if (d.compareTo(node.data) < 0)
            node.left = insert(d, node.left);
        else
        {
            insertComparisonCount++;
            if (d.compareTo(node.data) > 0)
                node.right = insert(d, node.right);
            else
                return node;
        }

        return balance(node);
    }

    /**
     * Balances the AVL tree at the given node.
     *
     * @param node The node to balance
     * @return The new root of the balanced subtree
     */
    public BTNode<dataType> balance(BTNode<dataType> node)
    {
        fixHeight(node);

        if (balanceFactor(node) == 2)
        {
            if (balanceFactor(node.right) < 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        if (balanceFactor(node) == -2)
        {
            if (balanceFactor(node.left) > 0)
                node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        return node;
    }

    /**
     * Calculates the balance factor of a node.
     *
     * @param node The node to calculate the balance factor for
     * @return The balance factor
     */
    public int balanceFactor(BTNode<dataType> node)
    {
        return height(node.right) - height(node.left);
    }

    /**
     * Updates the height of a node.
     *
     * @param node The node to update the height for
     */
    public void fixHeight(BTNode<dataType> node)
    {
        int hl = height(node.left);
        int hr = height(node.right);
        node.height = (hl > hr ? hl : hr) + 1;
    }

    public void readFileRange(int start, int end)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;

            while ((line = br.readLine()) != null && count < end) {
                count++;
                if (count >= start) {
                    String[] parts = line.split("\t");
                    if (parts.length >= 3) {
                        String term = parts[0];
                        String tree = parts[1];
                        double confidence = Double.parseDouble(parts[2]);
                        insert((dataType) new Entry(term, tree, confidence));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the height of a node.
     *
     * @param node The node to get the height of
     * @return The height of the node, or -1 if the node is null
     */
    public int height(BTNode<dataType> node)
    {
        return node == null ? -1 : node.height;
    }

    /**
     * Finds an entry in the tree by its term.
     *
     * @param term The term to search for
     * @return The Entry if found, null otherwise
     */
    public Entry find(String term)
    {
        return find(term, (BTNode<Entry>) root);
    }

    /**
     * Recursive helper method to find an entry by its term.
     *
     * @param term The term to search for
     * @param node The current node in the recursion
     * @return The Entry if found, null otherwise
     */
    public Entry find(String term, BTNode<Entry> node)
    {
        if (node == null)
            return null;

        searchComparisonCount++; // Increment search comparisons counter
        int cmp = term.compareTo(node.data.term);

        if (cmp < 0)
            return find(term, node.left);
        else if (cmp > 0)
            return find(term, node.right);
        else
            return node.data;
    }

    /**
     * Reads entries from a file and inserts them into the AVL tree.
     */
    public void readFile()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    String term = parts[0];
                    String tree = parts[1];
                    double confidence = Double.parseDouble(parts[2]);
                    insert((dataType) new Entry(term, tree, confidence));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the comparison counters for both search and insert operations.
     */
    public void resetCounters()
    {
        searchComparisonCount = 0;
        insertComparisonCount = 0;
    }

    /**
     * Gets the number of comparisons made during the last insert operation.
     *
     * @return The count of insert comparisons
     */
    public int getInsertComparisons()
    {
        return insertComparisonCount;
    }

    /**
     * Gets the number of comparisons made during the last search operation.
     *
     * @return The count of search comparisons
     */
    public int getSearchComparisons()
    {
        return searchComparisonCount;
    }

    /**
     * Searches for an entry in the tree.
     *
     * @param entry The entry to search for
     * @return The found entry, or null if not found
     */
    public Entry search(Entry entry)
    {
        return find(entry.term);
    }

    /**
     * Returns the total number of comparisons made during operations
     */
    public int getComparisonCount()
    {
        return searchComparisonCount + insertComparisonCount;
    }

    /**
     * Prints instrumentation data about the tree operations
     */
    public void printInstrumentation()
    {
        System.out.println("\nKey comparisons by search: " + searchComparisonCount);
        System.out.println("Key comparisons by insert: " + insertComparisonCount);
    }
}

/**
 * Represents a node in the AVL tree.
 *
 * @param <dataType> The type of data stored in the node
 */
class BTNode<dataType>
{
    dataType data;
    BTNode<dataType> left, right;
    int height;

    /**
     * Constructs a new node with the given data.
     *
     * @param d The data to store in the node
     */
    public BTNode(dataType d)
    {
        data = d;
        height = 0;
    }
}