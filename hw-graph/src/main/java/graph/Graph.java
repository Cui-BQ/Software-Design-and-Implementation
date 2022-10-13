package graph;


import java.util.*;

/**
 * This is a mutable graph that can be used to store nodes and edges.
 * No 2 nodes can have the same node data, but labels are not unique.
 * There can have multiple edges between two different nodes,
 * but no 2 edges with the same parent and child nodes will have the same edge label.
 */

public class Graph<D, L> {

    private Map<Node, Set<Edge>> graph;
    public static final boolean DEBUG = false;

    //Abstraction Function:
    //For a given Graph g, it's empty if g.graph is empty.
    //g cannot has null nodes or null edges or null edgeLabels.
    //
    //Representation Invariant:
    //graph != null &&
    //for each node in graph, node != null &&
    //for each edge from each node in graph, edge != null

    /**
     * @spec.effects Constructs a new empty graph.
     */
    public Graph() {
        graph = new HashMap<>();
        checkRep();
    }

    /**
     * Add a new node to the graph.
     *
     * @param node the new node to be added.
     * @throws IllegalArgumentException if node == null.
     * @spec.modifies this
     * @spec.effects Add node to this if node doesn't already exist in this.
     * @return true if node was successfully added to the graph,
     * i.e. node is not already in the graph. Returns false, if node is
     * already in the graph.
     */
    public boolean addNode(Node node){
        checkRep();
        if (node == null) {
            throw new IllegalArgumentException("node is null");
        }
        if (contains(node)){
            return false;
        }
        graph.put(node, new HashSet<>());
        checkRep();
        return true;
    }

    /**
     * Creates an new edge from parentNode to childNode with label edgeLabel in the graph.
     *
     * @param parentNode the beginning node of the added edge.
     * @param childNode the ending node of the added edge.
     * @param edgeLabel the name of the added edge.
     * @throws IllegalArgumentException if parentNode or childNode isn't already exist in graph,
     * parentNode or childNode or edgeLabel == null.
     * @spec.modifies this
     * @spec.effects Creates an new edge from parentNode to childNode with label edgeLabel in the graph
     * if edgeLabel doesn't already exist between parentNode and childNode.
     * @return true if edgeLabel was successfully added between parentNode and childNode in the graph,
     * i.e. edgeLabel is not already between parentNode and childNode in the graph. Returns false otherwise.
     */
    public boolean addEdge(Node parentNode, Node childNode, L edgeLabel){
        checkRep();
        if (parentNode == null || childNode == null || edgeLabel == null
                || !contains(parentNode) || !contains(childNode)) {
            throw new IllegalArgumentException("invalid params");
        }
        if (graph.get(parentNode).contains(new Edge(edgeLabel, childNode))){
            return false;
        }
        graph.get(parentNode).add(new Edge(edgeLabel, childNode));
        checkRep();
        return true;
    }

    /**
     * return a Set of node that contains each node of the graph.
     *
     * @return a Set of node that contains each node of the graph, return an empty node Set if graph is empty.
     */
    public HashSet<Node> ListNodes(){
        checkRep();
        HashSet<Node> ret = new HashSet<>();
        graph.forEach((key, value) -> ret.add(new Node(key.getData())));
        checkRep();
        return ret;
    }

    /**
     * return a Set of Edges that contains each edges of the parent node.
     *
     * @param parentNode the parent node.
     * @throws IllegalArgumentException if !contains(parentNode) or parentNode == null.
     * @return a Set of Edges that contains each edges of the parent node.
     * return an empty edge set if parentNode doesn't have any edge/child node.
     */
    public HashSet<Edge> ListEdges(Node parentNode){
        checkRep();
        if (!contains(parentNode) || parentNode == null){
            throw new IllegalArgumentException("Invalid parameter");
        }
        HashSet<Edge> ret = new HashSet<>();
        for (Edge e : graph.get(parentNode)){
            ret.add(e);
        }
        checkRep();
        return ret;
    }

    /**
     * This method returns true if this graph contains
     * the specified node, false otherwise
     *
     * @param node the specified node to be checked if its in graph
     * @return true if this graph contains the specified node, false otherwise
     * @throws IllegalArgumentException if node is null.
     */
    public boolean contains(Node node){
        if (node == null){
            throw new IllegalArgumentException("node is null");
        }
        return graph.containsKey(node);
    }

    private void checkRep(){
        if (DEBUG) {
            assert (graph != null): "graph is null";
            graph.forEach((key, value) -> {
                assert (key != null): "node is null";
                for (Edge e : value){
                    assert (e != null): "edge is null";
                }
            });
        }
    }



    /**
     * The inner immutable class node, it stores the node data and uses for the graph class.
     */
    public class Node {

        private D data;

        //Abstraction Function:
        //For a given Node n, the Node data contained in n.data.
        //
        //Representation Invariant:
        // data != null

        /**
         * @param nodeData the data to be stored in the new node.
         * @spec.effects Constructs a new node that stores nodeData.
         */
        public Node (D nodeData){
            data = nodeData;
            checkRep();
        }

        /**
         * @return the node's data.
         */
        public D getData (){
            return data;
        }

        /**
         * Standard equality operation.
         *
         * @param obj the object to be compared for equality
         * @return true if and only if 'obj' is an instance of a node and 'this' and 'obj' store the same data.
         */
        @Override
        public boolean equals (Object obj){
            if (obj instanceof Graph<?,?>.Node){
                if ( ((Graph<?,?>.Node)obj).data.equals(data) ){
                    return true;
                }
            }
            return false;
        }

        /**
         * Standard hashCode function.
         *
         * @return an int that all objects equal to this will also return
         */
        @Override
        public int hashCode (){
            return data.hashCode() * 31;
        }

        private void checkRep() {
            assert (data != null): "dataNode is null";
        }
    }

    /**
     * The inner immutable class edge, it stores the edge label and the child node.
     * Use edge to connect between nodes.
     * They are directed, go from a parent to a child, and contain a label.
     */
    public class Edge {
        private L label;
        private Node childNode;

        //Abstraction Function:
        //For a given Edge e, e.label represents the label/name of e,
        //and the destination node of e contained in e.childNode.
        //
        //Representation Invariant:
        // label != null
        // childNode != null
        /**
         * @param label the label of the new edge.
         * @param childNode the end node of the new edge.
         * @spec.effects Constructs a new edge with a label and stores the end node.
         */
        public Edge (L label, Node childNode){
            this.label = label;
            this.childNode = childNode;
            checkRep();
        }

        /**
         * @return the edge's label.
         */
        public L getLabel (){
            return label;
        }

        /**
         * @return the childNode's data
         */
        public D getChildNodeData (){
            return childNode.getData();
        }

        /**
         * @return the childNode/destination node of the edge.
         */
        public Node getChildNode (){
            return new Node(childNode.getData());
        }

        /**
         * Standard equality operation.
         *
         * @param obj the object to be compared for equality
         * @return true if and only if 'obj' is an instance of a edge
         * and 'this' and 'obj' store the same label and childNode.
         */
        @Override
        public boolean equals (Object obj){
            if (obj instanceof Graph<?,?>.Edge){
                if ( ((Graph<?,?>.Edge)obj).label.equals(label)
                        && ((Graph<?,?>.Edge)obj).childNode.equals(childNode)){
                    return true;
                }
            }
            return false;
        }

        /**
         * Standard hashCode function.
         *
         * @return an int that all objects equal to this will also return
         */
        @Override
        public int hashCode (){
            return 31 * label.hashCode() * childNode.hashCode();
        }

        private void checkRep() {
            assert (label != null): "label is null";
            assert (childNode != null): "childNode is null";
        }
    }

}
