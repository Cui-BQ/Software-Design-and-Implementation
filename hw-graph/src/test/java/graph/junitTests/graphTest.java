package graph.junitTests;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import graph.*;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * This class contains a set of test cases that can be used to test the implementation of the
 * graph and its inner classes.
 */
public final class graphTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    // Create new graphs
    private static Graph<String, String> myGraph = new Graph<>();

    /**
     * Helper function for create node.
     */
    private Graph<String, String>.Node node (String nodeName){
        return myGraph.new Node(nodeName);
    }

    /**
     * Helper function for create edge.
     */
    private Graph<String, String>.Edge edge (String edgeLabel, String childNodeName){
        return myGraph.new Edge(edgeLabel, node(childNodeName));
    }

    /**
     * Helper function for create n nodes.
     */
    private HashSet<Graph<String, String>.Node> getSomeNodes (int n){
        HashSet<Graph<String, String>.Node> nodes = new HashSet<>();
        for (int i = 1; i <= n; i++){
            String name = "n" + String.valueOf(i);
            nodes.add(node(name));
        }
        return nodes;
    }

    /**
     * Helper function for create some edge from n1 to n_i.
     */
    private HashSet<Graph.Edge> edgesFromN1 (int[] n){
        HashSet<Graph.Edge> edges = new HashSet<>();
        for (int i = 0; i < n.length; i++){
            String label = "n1-n" + String.valueOf(n[i]);
            String childName = "n" + String.valueOf(n[i]);
            edges.add(edge(label, childName));
        }
        return edges;
    }

    /**
     * Node Class test
     */
    @Test
    public void testNodeCtorAndGetData() {
        Graph.Node node1 = node("node1");
        Graph.Node node2 = node("");
        assertEquals(node1.getData(), "node1");
        assertEquals(node2.getData(), "");
    }

    @Test
    public void testNodeEqualsAndHashCode() {
        assertFalse(node("A").equals(node("B")));
        assertNotEquals(node("aCode").hashCode(), node("oneCode").hashCode());
        assertTrue(node("same").equals(node("same")));
        assertEquals(node("sameCode").hashCode(), node("sameCode").hashCode());
    }

    /**
     * Edge Class test
     */
    @Test
    public void testEdgeCtorAndGetMethods() {
        Graph.Edge edge1 = edge("edge1", "node1");
        Graph.Edge empty = edge("", "");

        assertTrue(edge1.getChildNode().equals(node("node1")));
        assertEquals(edge1.getChildNodeData(), "node1");
        assertEquals(edge1.getLabel(), "edge1");
        assertTrue(empty.getChildNode().equals(node("")));
        assertEquals(empty.getChildNodeData(), "");
        assertEquals(empty.getLabel(), "");
    }

    @Test
    public void testEdgeEqualsAndHashCode() {
        Graph.Edge edge1 = edge("edge1", "node1");
        Graph.Edge edge2 = edge("edge2", "node2");
        Graph.Edge anotherEdge1 = edge("edge1", "node1");
        Graph.Edge empty = edge("", "");

        assertFalse(edge1.equals(edge2));
        assertFalse(edge1.equals(empty));
        assertTrue(edge1.equals(anotherEdge1));
        assertEquals(edge1.hashCode(), anotherEdge1.hashCode());
        assertNotEquals(edge2.hashCode(), anotherEdge1.hashCode());
    }

    /**
     * graph Class test
     */
    @Test
    public void testGraphAddNode() {
        myGraph = new Graph<String, String>();
        //Add 5 different nodes
        HashSet<Graph<String, String>.Node> nodeSet = getSomeNodes(5);
        for (Graph<String, String>.Node n : nodeSet){
            assertTrue(myGraph.addNode(n));
        }
        //Add the same nodes again
        for (Graph<String, String>.Node n : nodeSet){
            assertFalse(myGraph.addNode(n));
        }
    }

    @Test
    public void testGraphAddEdge() {
        myGraph = new Graph<String, String>();
        myGraph.addNode(node("n1"));
        myGraph.addNode(node("n2"));
        //add edges
        assertTrue(myGraph.addEdge(node("n1"), node("n2"), "n1-n2"));
        assertTrue(myGraph.addEdge(node("n2"), node("n1"), "n2-n1"));
        assertTrue(myGraph.addEdge(node("n1"), node("n1"), "n1-n1"));
        // Add the same content again
        assertFalse(myGraph.addEdge(node("n1"), node("n2"), "n1-n2"));
        // Add edges with different edge labels.
        assertTrue(myGraph.addEdge(node("n1"), node("n2"), "new_n1-n2"));
        assertTrue(myGraph.addEdge(node("n2"), node("n1"), "new_n2-n1"));
        assertTrue(myGraph.addEdge(node("n1"), node("n1"), "new_n1-n1"));
    }

    @Test
    public void testGraphListNodes() {
        //Empty graph
        myGraph = new Graph<String, String>();
        Set<Graph<String, String>.Node> ret = myGraph.ListNodes();
        assertTrue(ret.isEmpty());
        //Add 5 different nodes
        HashSet<Graph<String, String>.Node> nodeSet = getSomeNodes(10);
        for (Graph<String, String>.Node n : nodeSet){
            myGraph.addNode(n);
        }
        ret = myGraph.ListNodes();
        assertTrue(ret.containsAll(nodeSet));
        assertTrue(nodeSet.containsAll(ret));
        //Add some duplicate nodes, should still be the same as nodeSet.
        myGraph.addNode(node("n1"));
        myGraph.addNode(node("n8"));
        ret = myGraph.ListNodes();
        assertTrue(ret.containsAll(nodeSet));
        assertTrue(nodeSet.containsAll(ret));
    }

    @Test
    public void testGraphListEdges() {
        myGraph = new Graph<String, String>();
        //Add 5 different nodes
        HashSet<Graph<String, String>.Node> nodeSet = getSomeNodes(5);
        for (Graph<String, String>.Node n : nodeSet){
            myGraph.addNode(n);
        }
        //No outgoing edge
        HashSet<Graph<String, String>.Edge> ret = myGraph.ListEdges(node("n2"));
        assertTrue(ret.isEmpty());
        //Add edges from n1 to n1 n2 and n5.
        assertTrue(myGraph.addEdge(node("n1"), node("n1"), "n1-n1"));
        assertTrue(myGraph.addEdge(node("n1"), node("n2"), "n1-n2"));
        assertTrue(myGraph.addEdge(node("n1"), node("n5"), "n1-n5"));
        //Test contains edge n1-n1, n1-n2, n1-n5
        ret = myGraph.ListEdges(node("n1"));
        int[] array = {1,2,5};
        HashSet<Graph.Edge> edges = edgesFromN1(array);
        assertTrue(ret.containsAll(edges));
        assertTrue(edges.containsAll(ret));

        //Add edges from n1 to n3 and n4.
        assertTrue(myGraph.addEdge(node("n1"), node("n3"), "n1-n3"));
        assertTrue(myGraph.addEdge(node("n1"), node("n4"), "n1-n4"));
        //Test contains edge n1-n1, n1-n2, ... , n1-n5
        ret = myGraph.ListEdges(node("n1"));
        int[] arr = {1,2,3,4,5};
        edges = edgesFromN1(arr);
        assertTrue(ret.containsAll(edges));
        assertTrue(edges.containsAll(ret));
    }

    @Test
    public void testGraphContains() {
        myGraph = new Graph<String, String>();
        //Add 5 different nodes
        HashSet<Graph<String, String>.Node> nodeSet = getSomeNodes(5);
        for (Graph<String, String>.Node n : nodeSet){
            myGraph.addNode(n);
            assertTrue(myGraph.contains(n));
        }
        //Test node that doesn't exist
        assertFalse(myGraph.contains(node("new")));
    }
}
