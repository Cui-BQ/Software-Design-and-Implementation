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
import EdgeList from "./EdgeList";
import Grid from "./Grid";
import GridSizePicker from "./GridSizePicker";

// Allows us to write CSS styles inside App.css, any any styles will apply to all components inside <App />
import "./App.css";

interface AppState {
    gridSize: number;  // size of the grid to display
    textContent : string;
    edges : Array<string>;
}

class App extends Component<{}, AppState> { // <- {} means no props.

    constructor(props: any) {
        super(props);
        this.state = {
            gridSize: 4,
            textContent: "",
            edges: [],
        };
    }

    updateGridSize = (newSize: number) => {
        if (newSize < this.state.gridSize || isNaN(newSize)){
            this.setState({
                gridSize: newSize,
                edges: [],
            });
        } else {
            this.setState({
                gridSize: newSize
            });
        }
    };

    updateTextContact = (newText: string) => {
        this.setState({
           textContent: newText
        });
    }

    updateEdges = (newEdges: Array<string>) => {
        this.setState({
            edges: newEdges
        });
    }

    render() {
        const canvas_size = 500;
        return (
            <div>
                <p id="app-title">Connect the Dots!</p>
                <GridSizePicker value={this.state.gridSize.toString()} currentValue={this.state.gridSize.toString()} onChange={this.updateGridSize}/>
                <Grid size={this.state.gridSize} width={canvas_size} height={canvas_size} edges={this.state.edges} recoverEdge={this.updateEdges}/>
                <EdgeList onChange={this.updateEdges} textContent={this.state.textContent} updateText={this.updateTextContact}/>
            </div>

        );
    }

}

export default App;
