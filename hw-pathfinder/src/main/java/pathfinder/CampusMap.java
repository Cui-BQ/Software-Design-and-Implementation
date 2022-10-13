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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusMap implements ModelAPI {

    private Graph<Point, Double> campusMap;
    List<CampusBuilding> buildingList;
    List<CampusPath> pathList;

    /**
     * CampusMap constructor,
     * read buildings from "campus_buildings.tsv" and store in buildingList.
     * read Paths from "campus_paths.tsv" and store in pathList.
     * And create a campusMap by using path data.
     */
    public CampusMap (){
        buildingList = CampusPathsParser.parseCampusBuildings("campus_buildings.tsv");
        pathList = CampusPathsParser.parseCampusPaths("campus_paths.tsv");
        campusMap = new Graph<>();
    }

    @Override
    public boolean shortNameExists(String shortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        for (CampusBuilding building : buildingList){
            if (shortName.equals(building.getShortName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String longNameForShort(String shortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        for (CampusBuilding building : buildingList){
            if (shortName.equals(building.getShortName())) {
                return building.getLongName();
            }
        }
        throw new IllegalArgumentException("the short name provided does not exist");
    }

    @Override
    public Map<String, String> buildingNames() {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        Map<String,String> buildingName = new HashMap<>();
        for (CampusBuilding building : buildingList){
            buildingName.put(building.getShortName(), building.getLongName());
        }
        return buildingName;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        if (startShortName == null || endShortName == null ||
                !shortNameExists(startShortName) || !shortNameExists(endShortName)) {
            throw new IllegalArgumentException();
        }
        for (CampusPath path : pathList){
            Graph<Point, Double>.Node node1 = campusMap.new Node(new Point(path.getX1(), path.getY1()));
            Graph<Point, Double>.Node node2 = campusMap.new Node(new Point(path.getX2(), path.getY2()));
            campusMap.addNode(node1);
            campusMap.addNode(node2);
            campusMap.addEdge(node1, node2, path.getDistance());
            campusMap.addEdge(node2, node1, path.getDistance());
        }
        Point start = null;
        Point dest = null;
        for (CampusBuilding building : buildingList) {
            if (building.getShortName().equals(startShortName)) {
                start = new Point(building.getX(), building.getY());
            }
            if (building.getShortName().equals(endShortName)) {
                dest = new Point(building.getX(), building.getY());
            }
        }
        return DijkstrasPathFinder.DijkstraPathFinder(start, dest, campusMap);
    }

}
