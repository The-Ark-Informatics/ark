/*

 * Tree.java

 *

 * Created on May 6, 2003, 11:25 AM

 */



package neuragenix.common;



// sun packages

import java.util.Enumeration;

import javax.swing.tree.*;



/**

 *

 * @author  hh

 */

public class Tree 

{

    /**

     * Root of the tree

     */

    protected DefaultMutableTreeNode root;

    

    /**

     * Current node

     */

    protected DefaultMutableTreeNode currentNode;

    

    

    /**

     * Constructor

     */

    public Tree()

    {

        this(new DefaultMutableTreeNode("root"));

    }

    

    public Tree(DefaultMutableTreeNode root)

    {

        this.root = root;

        currentNode = root;

    }

    

    /**

     * Get the root node of the tree

     */

    public DefaultMutableTreeNode getRoot()

    {

        return root;

    }

    

    /**

     * Set the current node of the tree

     */

    public void setCurrentNode(DefaultMutableTreeNode current)

    {

        currentNode = current;

    }

    

    public void removeCurrentNode()

    {

        DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) currentNode.getParent();

        currentNode.removeFromParent();

        setCurrentNode(tempNode);

    }

    

    /**

     * Get the current node of the tree

     */

    public DefaultMutableTreeNode getCurrentNode()

    {

        return currentNode;

    }

    

    /**

     * Add a node to the tree

     */

    public void addNode(DefaultMutableTreeNode node)

    {

        currentNode.add(node);

    }

    

    public Enumeration breadthFirstEnumeration()

    {

        return root.breadthFirstEnumeration();

    }

    

    public Enumeration depthFirstEnumeration()

    {

        return root.depthFirstEnumeration();

    }

    

    public Enumeration postorderEnumeration()

    {

        return root.postorderEnumeration();

    }

    

    public Enumeration preorderEnumeration()

    {

        return root.preorderEnumeration();

    }

}
