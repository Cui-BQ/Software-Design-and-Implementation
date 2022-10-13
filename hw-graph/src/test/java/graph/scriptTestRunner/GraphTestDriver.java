/*
 * Copyright (C) 2021 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package graph.scriptTestRunner;

import graph.Graph;

import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads test scripts
 * from files for testing Graph.
 **/
public class GraphTestDriver {

    // *********************************
    // ***  Interactive Test Driver  ***
    // *********************************

    public static void main(String[] args) {
        try {
            if (args.length > 1) {
                printUsage();
                return;
            }

            GraphTestDriver td;

            if (args.length == 0) {
                td = new GraphTestDriver(new InputStreamReader(System.in), new OutputStreamWriter(System.out));
                System.out.println("Running in interactive mode.");
                System.out.println("Type a line in the script testing language to see the output.");
            } else {
                String fileName = args[0];
                File tests = new File(fileName);

                System.out.println("Reading from the provided file.");
                System.out.println("Writing the output from running those tests to standard out.");
                if (tests.exists() || tests.canRead()) {
                    td = new GraphTestDriver(new FileReader(tests), new OutputStreamWriter(System.out));
                } else {
                    System.err.println("Cannot read from " + tests.toString());
                    printUsage();
                    return;
                }
            }

            td.runTests();

        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("  Run the gradle 'build' task");
        System.err.println("  Open a terminal at hw-graph/build/classes/java/test");
        System.err.println("  To read from a file: java graph.scriptTestRunner.GraphTestDriver <name of input script>");
        System.err.println("  To read from standard in (interactive): java graph.scriptTestRunner.GraphTestDriver");
    }

    // ***************************
    // ***  JUnit Test Driver  ***
    // ***************************

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    // TODO for the student: Uncomment and parameterize the next line correctly:
    private final Map<String, Graph<String, String>> graphs = new HashMap<String, Graph<String, String>>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @spec.requires r != null && w != null
     * @spec.effects Creates a new GraphTestDriver which reads command from
     * {@code r} and writes results to {@code w}
     **/
    // Leave this constructor public
    public GraphTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    // Leave this method public
    public void runTests()
            throws IOException {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch (command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "Contains":
                    contains(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        // TODO Insert your code here.
        graphs.put(graphName, new Graph<String, String>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        // TODO Insert your code here.

        Graph<String, String> current = graphs.get(graphName);
        if (nodeName.equals("null")) {
            current.addNode(null);
        } else {
            if (current.addNode(current.new Node(nodeName))){
                output.println("added node " + nodeName + " to " + graphName);
            } else {
                output.println("failed to add node " + nodeName + " to " + graphName);
            }
        }


    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        // TODO Insert your code here.

        Graph<String, String> current = graphs.get(graphName);
        Graph<String, String>.Node parent = current.new Node(parentName);
        if (parent.getData().equals("null")) parent = null;
        Graph<String, String>.Node child = current.new Node(childName);
        if (child.getData().equals("null")) child = null;
        String label = edgeLabel;
        if (edgeLabel.equals("null")) label = null;
        if (current.addEdge(parent, child, label)) {
            output.println("added edge " + edgeLabel + " from " + parentName + " to " + childName + " in " + graphName);
        } else {
            output.println("failed to add edge " + edgeLabel + " from " + parentName + " to " + childName + " in " + graphName);
        }
    }

    private void listNodes(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        // TODO Insert your code here.

        Graph<String, String> current = graphs.get(graphName);
        output.print(graphName + " contains:");
        HashSet<Graph<String, String>.Node> set = current.ListNodes();
        ArrayList<String> list = new ArrayList<>();
        Iterator<Graph<String, String>.Node> iter = set.iterator();
        while (iter.hasNext()){
            list.add(iter.next().getData());
        }
        Collections.sort(list);
        for (String n : list){
            output.print(" " + n);
        }
        output.println();
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        // TODO Insert your code here.

        Graph<String, String> current = graphs.get(graphName);
        Graph<String, String>.Node parent = current.new Node(parentName);
        if (parent.getData().equals("null")) parent = null;
        HashSet<Graph<String, String>.Edge> set = current.ListEdges(parent);
        ArrayList<String[]> list = new ArrayList<>();
        for (Graph<String, String>.Edge e : set){
            String [] arr = new String[2];
            arr[0] = e.getChildNodeData();
            arr[1] = e.getLabel();
            list.add(arr);
        }
        Collections.sort(list, new Comparator<String[]>() {
            public int compare(String[] a, String[] b) {
                if (a[0].compareTo(b[0]) == 0){
                    return a[1].compareTo(b[1]);
                }
                return a[0].compareTo(b[0]);
            }
        });
        output.print("the children of " + parentName + " in " + graphName + " are:");
        for (String[] n : list){
            output.print(" " + n[0] + "(" + n[1] + ")");
        }
        output.println();
    }

    private void contains(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to Contains: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);
        contains(graphName, nodeName);
    }

    private void contains(String graphName, String nodeName) {
        Graph<String, String> current = graphs.get(graphName);
        Graph<String, String>.Node node = current.new Node(nodeName);
        if (node.getData().equals("null")) node = null;
        if (current.contains(node)){
            output.println(graphName +" contains " + nodeName + ": true");
        } else {
            output.println(graphName +" contains " + nodeName + ": false");
        }
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
