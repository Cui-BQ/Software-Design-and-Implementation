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

import React, {Component} from 'react';
import Map from "./Map";
import BuildingPicker from "./BuildingPicker"
import PathFinder from "./PathFinder";

import "./App.css";

interface AppState {
    path : Array<string>;
    startBuilding : string;
    endBuilding : string;
}

class App extends Component<{}, AppState> {

    constructor(props: any) {
        super(props);
        this.state = {
          path : [],
          startBuilding : "N/A",
          endBuilding : "N/A",
        };
    }

    componentDidUpdate() {
        this.render();
    }


    updateStart = (newStart : string) => {
        this.setState({
            startBuilding : newStart
        });
    }

    updateEnd = (newEnd : string) => {
        this.setState({
            endBuilding : newEnd
        });
    }

    updatePath = (newPath : Array<string>) => {
        this.setState({
            path: newPath
        });
    }

    render() {
        return (
            <div>
                <p id="app-title">CampusPaths GUI</p>
                <BuildingPicker start={this.state.startBuilding} end={this.state.endBuilding}
                                onStartChange={this.updateStart} onEndChange={this.updateEnd}/>
                <PathFinder start={this.state.startBuilding} end={this.state.endBuilding}
                            path={this.state.path} onChange={this.updatePath}/>
                <Map path={this.state.path} onChange={this.updatePath}/>
            </div>
        );
    }

}

export default App;
