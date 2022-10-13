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

interface GridProps {
    size: number;    // size of the grid to display
    width: number;   // width of the canvas on which to draw
    height: number;  // height of the canvas on which to draw
    edges : Array<string>;
    recoverEdge (edges: Array<string>): void;
}

interface GridState {
    backgroundImage: any,  // image object rendered into the canvas (once loaded)
}

/**
 *  A simple grid with a variable size
 *
 *  Most of the assignment involves changes to this class
 */
class Grid extends Component<GridProps, GridState> {

    canvasReference: React.RefObject<HTMLCanvasElement>
    currentEdges: Array<string>

    constructor(props: GridProps) {
        super(props);
        this.state = {
            backgroundImage: null  // An image object to render into the canvas.
        };
        this.canvasReference = React.createRef();
        this.currentEdges = [];
    }

    componentDidMount() {
        // Since we're saving the image in the state and re-using it any time we
        // redraw the canvas, we only need to load it once, when our component first mounts.
        this.fetchAndSaveImage();
        this.redraw();
    }

    componentDidUpdate() {
        this.redraw()
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        const background = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./image.jpg";
    }

    redraw = () => {
        if (this.canvasReference.current === null) {
            throw new Error("Unable to access canvas.");
        }
        const ctx = this.canvasReference.current.getContext('2d');
        if (ctx === null) {
            throw new Error("Unable to create canvas drawing context.");
        }

        // First, let's clear the existing drawing so we can start fresh:
        ctx.clearRect(0, 0, this.props.width, this.props.height);

        // Once the image is done loading, it'll be saved inside our state, and we can draw it.
        // Otherwise, we can't draw the image, so skip it.
        if (this.state.backgroundImage !== null) {
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }

        // Draw all the dots.
        const coordinates = this.getCoordinates();
        for (let coordinate of coordinates) {
            this.drawCircle(ctx, coordinate);
        }

        //
        const edges = this.getEdges(this.props.edges);
        const x : number = this.props.width/(this.props.size + 1);
        const y : number = this.props.height/(this.props.size + 1);
        for (let edge of edges) {
            this.drawLine(ctx, edge, x, y);
        }
    };

    /**
     * Returns an array of coordinate pairs that represent all the points where grid dots should
     * be drawn.
     */
    getCoordinates = (): [number, number][] => {
        // A hardcoded 4x4 grid. Probably not going to work when we change the grid size...
        let coordinates: [number, number][] = [];
        const x : number = this.props.width/(this.props.size + 1);
        const y : number = this.props.height/(this.props.size + 1);
        for (let i = 0; i < this.props.size; i++){
            for (let j = 0; j < this.props.size; j++)
            coordinates[i*this.props.size+j] = [x*(j+1), y*(i+1)];
        }
        return coordinates;
    };

    getEdges = (Edges: Array<string>): [number, number, number, number, string][] => {
        let edges : [number, number, number, number, string][] = [];
        let validEdges : boolean = true;
        for (let i = 0; i < Edges.length; i++){
            if (Edges[i] === "") continue;
            let currentEdge : [number, number, number, number, string];
            let iEdge : Array<string> = Edges[i].split(' ');
            if( iEdge.length !== 3) {
                validEdges = false;
                alert("\"" + iEdge + "\" isn't a valid edge.\n" +
                    " Edge data should be this format: x1,y1 x2,y2 COLOR");
            } else {
                let x1 : number = parseInt(iEdge[0].split(',')[0]);
                let y1 : number = parseInt(iEdge[0].split(',')[1]);
                let x2 : number = parseInt(iEdge[1].split(',')[0]);
                let y2 : number = parseInt(iEdge[1].split(',')[1]);
                if (!isNaN(x1) && !isNaN(y1) && !isNaN(x2) && !isNaN(y2)) {
                    if (x1 >= 0 && x1 < this.props.size && x2 >= 0 && x2 < this.props.size &&
                        y1 >= 0 && y1 < this.props.size && y2 >= 0 && y2 < this.props.size) {
                        currentEdge = [x1, y1, x2, y2, iEdge[2]];
                        edges.push(currentEdge);
                    } else {
                        validEdges = false;
                        alert("\"" + iEdge + "\" isn't a valid edge.\n " +
                            "The current grid size is " + this.props.size + ", but this edge is outside the grid.");
                    }
                } else {
                    validEdges = false;
                    alert("\"" + iEdge + "\" isn't a valid edge.\n " +
                        "Edge data should be this format: x1,y1 x2,y2 COLOR, where x1,y1 x2,y2 must be numbers");
                }
            }
            if (!validEdges) {
                this.props.recoverEdge(this.currentEdges);
                return this.getEdges(this.currentEdges);
            }
        }
        this.currentEdges = this.props.edges;
        return edges;
    }

    drawCircle = (ctx: CanvasRenderingContext2D, coordinate: [number, number]) => {
        ctx.fillStyle = "white";
        // Generally use a radius of 4, but when there are lots of dots on the grid (> 50)
        // we slowly scale the radius down so they'll all fit next to each other.
        const radius = Math.min(4, 100 / this.props.size);
        ctx.beginPath();
        ctx.arc(coordinate[0], coordinate[1], radius, 0, 2 * Math.PI);
        ctx.fill();
    };

    drawLine = (ctx: CanvasRenderingContext2D, edge: [number, number, number, number, string], x: number, y: number) => {
        ctx.strokeStyle = edge[4];
        // Generally use a radius of 4, but when there are lots of dots on the grid (> 50)
        // we slowly scale the radius down so they'll all fit next to each other.
        ctx.beginPath();
        ctx.moveTo(edge[0]*x, edge[1]*y);
        ctx.lineTo(edge[2]*x, edge[3]*y);
        ctx.stroke();
    };

    render() {
        let size : number = this.props.size;
        if (isNaN(this.props.size)){
            size = 0;
        }
        return (
            <div id="grid">
                <canvas ref={this.canvasReference} width={this.props.width} height={this.props.height}/>
                <p>Current Grid Size: {size}</p>
            </div>
        );
    }
}

export default Grid;
