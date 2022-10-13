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

package pathfinder.scriptTestRunner;

import graph.Graph;
import pathfinder.DijkstrasPathFinder;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {
    private final PrintWriter output;
    private final BufferedReader input;
    private Map<String, Graph<String, Double>> graphs = new HashMap<String, Graph<String, Double>>();

    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        // TODO: Implement this, reading commands from `r` and writing output to `w`.
        // See GraphTestDriver as an example.
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        // TODO: Implement this.
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
                case "FindPath":
                    findPath(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
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
        graphs.put(graphName, new Graph<String, Double>());
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
        Graph<String, Double> current = graphs.get(graphName);
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
        Double edgeLabel = Double.parseDouble(arguments.get(3));

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {
        // TODO Insert your code here.
        Graph<String, Double> current = graphs.get(graphName);
        Graph<String, Double>.Node parent = current.new Node(parentName);
        if (parent.getData().equals("null")) parent = null;
        Graph<String, Double>.Node child = current.new Node(childName);
        if (child.getData().equals("null")) child = null;
        Double label = edgeLabel;
        if (edgeLabel.equals("null")) label = null;
        if (current.addEdge(parent, child, label)) {
            output.println(String.format("added edge %.3f", edgeLabel) + " from " + parentName + " to " + childName + " in " + graphName);
        } else {
            output.println("failed to add edge " + edgeLabel + " from " + parentName + " to " + childName + " in " + graphName);
        }
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to findGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        String start = arguments.get(1);
        String dest = arguments.get(2);
        findPath(graphName, start, dest);
    }

    private void findPath(String graphName, String start, String dest) {
        try {
            Path<String> path = DijkstrasPathFinder.DijkstraPathFinder(start, dest, graphs.get(graphName));
            double cost = 0;
            output.println("path from " + start + " to " + dest + ":");
            if (path == null) {
                output.println("no path found");
                return;
            }
            Iterator<Path<String>.Segment> iter = path.iterator();

            while (iter.hasNext()){
                Path<String>.Segment current = iter.next();
                output.println(current.getStart() + " to " + current.getEnd() + String.format(" with weight %.3f", current.getCost()));
                cost += current.getCost();
            }
            output.println(String.format("total cost: %.3f", cost));
        } catch (IllegalArgumentException e){
            if(e.toString().contains(start)) {
                output.println("unknown node " + start);
            }
            if(e.toString().contains(dest)) {
                output.println("unknown node " + dest);
            }
        }
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
        Graph<String, Double> current = graphs.get(graphName);
        Graph<String, Double>.Node parent = current.new Node(parentName);
        if (parent.getData().equals("null")) parent = null;
        ArrayList<Graph<String, Double>.Edge> list = new ArrayList<>(current.ListEdges(parent));
        Collections.sort(list, new Comparator<Graph<String, Double>.Edge>() {
            @Override
            public int compare(Graph<String, Double>.Edge o1, Graph<String, Double>.Edge o2) {
                if (o1.getChildNodeData().equals(o2.getChildNodeData())){
                    return o1.getLabel().compareTo(o2.getLabel());
                } else {
                    return o1.getChildNodeData().compareTo(o2.getChildNodeData());
                }
            }
        });
        output.print("the children of " + parentName + " in " + graphName + " are:");
        for (Graph<String, Double>.Edge n : list){
            output.print(" " + n.getChildNodeData() + String.format("(%.3f)", n.getLabel()));
        }
        output.println();
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


