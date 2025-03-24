/*
  Student Number: FRTETH003
  Name : Ethan Fortuin
  Date : 23/03/25
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
public class AVLTreeDatabase<dataType extends Comparable<? super dataType>>3

{
    public String filePath;
    public BTNode<dataType> root;

    /**
     * Constructs an AVLTreeDatabase with the specified file path.
     *
     * @param filePath The path to the file containing the data
     */
    public AVLTreeDatabase(String filePath)
    {
        root = null;
        this.filePath = filePath;
        this.readFile();
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
        {
            return new BTNode<>(d, null, null);
        }
        if (d.compareTo(node.data) <= 0)
        {
            node.left = insert(d, node.left);
        }
        else
        {
            node.right = insert(d, node.right);
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
        int balanceFactor = balanceFactor(node);
        if (balanceFactor == 2)
        {
            if (balanceFactor(node.right) < 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        if (balanceFactor == -2)
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
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    /**
     * Returns the height of a node.
     *
     * @param node The node to get the height of
     * @return The height of the node, or -1 if the node is null
     */
    public int height(BTNode<dataType> node)
    {
        if (node != null)
        {
            return node.height;
        }
        return -1;
    }

    /**
     * Finds an entry in the tree by its term.
     *
     * @param term The term to search for
     * @return The Entry if found, null otherwise
     */
    public Entry find(String term)
    {
        return find(term, (BTNode<Entry>)root);
    }

    /**
     * Recursive helper method to find an entry by its term.
     *
     * @param term The term to search for
     * @param node The current node in the recursion
     * @return The Entry if found, null otherwise
     */
    private Entry find(String term, BTNode<Entry> node)
    {
        if (node == null)
        {
            return null;
        }

        int comparison = term.compareTo(node.data.term);
        if (comparison == 0)
        {
            return node.data;
        }
        else if (comparison < 0)
        {
            return find(term, node.left);
        }
        else
        {
            return find(term, node.right);
        }
    }

    /**
     * Reads entries from a file and inserts them into the AVL tree.
     */
    public void readFile()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split("\t");
                if (parts.length == 3)
                {
                    String term = parts[0];
                    String tree = parts[1];
                    double confidence = Double.parseDouble(parts[2]);
                    insert((dataType) new Entry(term, tree, confidence));
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("Error reading file: " + e.getMessage());
        }
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
    BTNode<dataType> left;
    BTNode<dataType> right;
    int height;

    /**
     * Constructs a BTNode with the given data and child nodes.
     *
     * @param d The data to store in this node
     * @param l The left child node
     * @param r The right child node
     */
    public BTNode(dataType d, BTNode<dataType> l, BTNode<dataType> r)
    {
        data = d;
        left = l;
        right = r;
        height = 1;
    }
}