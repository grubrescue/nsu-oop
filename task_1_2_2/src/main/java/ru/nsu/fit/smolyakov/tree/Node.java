package ru.nsu.fit.smolyakov.tree;

import java.util.LinkedList;
import java.util.List;

/**
 * Implemented for the joint use with class {@link Tree}.
 * May be instantiated only and if only it's returned by
 * {@link Tree#addNode} method.
 */
public class Node<T> {
    private Tree<T> proprietor;
    private Node<T> parent;
    private List<Node<T>> children = new LinkedList<>();

    private T value;

    Node(Node<T> parent, T value) {
        this(parent.proprietor, parent, value);
    }

    Node(Tree<T> proprietor, Node<T> parent, T value) {
        this.proprietor = proprietor;
        this.parent = parent;
        this.value = value;
    }

    Tree<T> getProprietor() {
        return proprietor;
    }

    Node<T> getParent() {
        return parent;
    }

    T getValue() {
        return value;
    }

    void addChild(Node<T> who) {
        children.add(who);
    }

    int getChildCount() {
        return children.size();
    }

    Node<T> getChild(int id) {
        return children.get(id);
    }

    List<Node<T>> getChildren() {
        return children;
    }

    void removeChild(int id) {
        children.remove(id);
    }

    void removeChild(Node<T> node) {
        children.remove(node);
    }

    void clearChildren() {
        children.clear();
    }

    /**
     * Compares the specified object with this node for equality. 
     * Returns true if the specified object is also a node, 
     * the two nodes have equal value and arrays of nodes,
     * which are also equal.
     * 
     * <p>Implemented for internal use with {@link Tree#equals}.
     * 
     * @param  obj  object to be compared for equality with this node
     * @return true if the specified object is equal to this node,
     *         false otherwise
     */
    @SuppressWarnings("unchecked") 
    boolean equalRecursive(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        Node<T> other = (Node<T>) obj;

        if (this.value != null 
            && !this.value.equals(other.value)) {
            return false;
        }

        for (int i = 0; i < this.children.size(); i++) {
            if (!(this.children.get(i).equalRecursive(other.children.get(i)))) {
                return false;
            }
        }

        return true;
    }
}
