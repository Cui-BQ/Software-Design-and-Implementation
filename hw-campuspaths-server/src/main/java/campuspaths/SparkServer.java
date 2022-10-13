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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.Map;

public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        // TODO: Create all the Spark Java routes you need here.
        Spark.get("/get-all-building-name", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                CampusMap map = new CampusMap();
                Map<String, String> buildingNames = map.buildingNames();
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(buildingNames);
                return jsonResponse;
            }
        });

        Spark.get("/find-shortest-path", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String startString = request.queryParams("start");
                String endString = request.queryParams("end");
                if(startString == null || endString == null) {
                    Spark.halt(400, "must have start and end");
                }
                CampusMap map = new CampusMap();
                Path<Point> path = null;
                try {
                    path = map.findShortestPath(startString, endString);
                } catch (IllegalArgumentException e){
                    Spark.halt(400, "Invalid start point and/or end point");
                }
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(path);
                return jsonResponse;
            }
        });

    }

}
