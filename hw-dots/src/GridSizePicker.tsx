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

/* A simple TextField that only allows numerical input */

import React, {Component} from 'react';

interface GridSizePickerProps {
    value: string;                    // text to display in the text area
    currentValue: string;             //
    onChange(newSize: number): void;  // called when a new size is picked
}

class GridSizePicker extends Component<GridSizePickerProps> {

    onInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        // Every event handler with JS can optionally take a single parameter that
        // is an "event" object - contains information about an event. For mouse clicks,
        // it'll tell you thinks like what x/y coordinates the click was at. For text
        // box updates, it'll tell you the new contents of the text box, like we're using
        // below.
        //
        // TODO - Not currently doing any validation or error handling. Should probably add some...
        const newSize: number = parseInt(event.target.value);
        const currentSize: number = parseInt(this.props.currentValue);
        if (newSize > 100){
            console.log("User set invalid grid size: " + newSize);
            alert('The maximum grid size is 100');
            this.props.onChange(currentSize);
        } else {
            this.props.onChange(newSize); // Tell our parent component about the new size.
        }

    };

    render() {
        return (
            <div id="grid-size-picker">
                <label>
                    Grid Size:
                    <input
                        value={this.props.value}
                        onChange={this.onInputChange}
                        type="number"
                        min={1}
                        max={100}
                    />
                </label>
            </div>
        );
    }
}

export default GridSizePicker;
