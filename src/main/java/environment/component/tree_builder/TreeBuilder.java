package environment.component.tree_builder;


import environment.component.tree_builder.nodes.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public class TreeBuilder
{
    private Node root;

    private Node current;

    public TreeBuilder() {}

    public TreeBuilder(Node root) {
        root.setRoot(root);

        this.root = root;
        this.current = root;
    }

    final public TreeBuilder setRoot(String name) throws Exception {
        if (this.root != null) {
            throw new Exception("root is already defined");
        }

        this.root = new MapNode(name);
        this.root.setLevel(0);
        this.current = this.root;

        return this;
    }

    @Contract("null -> fail")
    @NotNull
    final public TreeBuilder addChild(Node node) throws Exception {
        if (this.current == null) {
            throw new Exception("Can't find current node");
        }

        if (!(this.current instanceof MapNode)) {
            throw new Exception("Node " + this.current.getClass().getName() + " can't have children");
        }

        this.current.addChild(node);
        node.setRoot(this.root)
            .setParent(this.current)
            .setLevel(current.getLevel() + 1);

        return this;
    }

    final public TreeBuilder end() throws Exception {
        Node next = this.current.getParent();

        if (next == null && this.current != this.root) {
            throw new Exception("Unexpected end of builder");
        }

        this.current = next == this.root ? this.root : next;

        return this;
    }

    final public TreeBuilder next(Node next) {
        next.setRoot(this.root);
        next.setParent(this.current);

        this.current.addChild(next);
        this.current = next.setLevel(this.current.getLevel() + 1);

        return this;
    }

    final public Node getRoot() {
        return this.root;
    }

    final public String getRootName() {
        return this.root.getName();
    }
}
