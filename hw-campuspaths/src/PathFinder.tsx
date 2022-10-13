import React, {Component} from 'react';

interface PathFinderProps {
    path : Array<string>;
    start : string;
    end : string;
    onChange(path: Array<string>): void;
}

class  PathFinder extends Component<PathFinderProps> {

    getPath = async () => {
        try {
            let start = this.props.start.split(':')[0];
            let end = this.props.end.split(':')[0];
            start = start.slice(0, start.length-1);
            end = end.slice(0, end.length-1);
            let response = await fetch("http://localhost:4567/find-shortest-path?start=" +
                                        start + "&end=" + end);
            if (!response.ok) {
                alert("Please select the valid buildings");
                return; // Don't keep trying to execute if the response is bad.
            }
            let text = await response.text();
            text = text.slice(text.indexOf("[")+1, text.indexOf("]"));
            text = text.replaceAll("},{", "&").replaceAll("{", "")
                .replaceAll("}", "").replaceAll("\"", "");
            let path : Array<string> = text.split('&');
            for (let i = 0; i < path.length; i++){
                let x1 = Math.round(parseFloat(path[i].slice(path[i].indexOf("x:")+2, path[i].indexOf(","))));
                let y1 = Math.round(parseFloat(path[i].slice(path[i].indexOf("y:")+2, path[i].indexOf(",end"))));
                let x2 = Math.round(parseFloat(path[i].slice(path[i].indexOf("d:x:")+4, path[i].lastIndexOf(",y"))));
                let y2 = Math.round(parseFloat(path[i].slice(path[i].lastIndexOf("y:")+2, path[i].indexOf(",cost"))));
                path[i] = x1 + "," + y1 + "," + x2 + "," + y2;
            }
            this.props.onChange(path);
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };



    render() {
        return (
                <div id="PathFinder">
                    <button onClick={this.getPath}>Find Path!</button>
                </div>
        );
    }
}

export default PathFinder;