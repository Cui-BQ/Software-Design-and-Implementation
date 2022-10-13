import React, {Component} from 'react';

interface BuildingPickerProps {
    start : string;
    end : string;
    onStartChange(newStart: string): void;
    onEndChange(newEnd: string): void;
}

interface BuildingPickerState {
    buildingNames : Array<string>;
}


class BuildingPicker extends Component<BuildingPickerProps, BuildingPickerState> {



    constructor(props: BuildingPickerProps) {
        super(props);
        this.state = ({
            buildingNames : [],
        });
    }

    componentDidMount() {
        this.getBuildingNames();
    }


    onStartInputChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const newStart = event.target.value;
        this.props.onStartChange(newStart);
    }

    onEndInputChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const newEnd = event.target.value;
        this.props.onEndChange(newEnd);
    }

    getBuildingNames = async () => {
        try {
            let response = await fetch("http://localhost:4567/get-all-building-name");
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }
            let text = await response.text();
            text = text.replaceAll("\",\"", "@%");
            text = text.replaceAll("\"", " ");
            text = text.replaceAll("\\u0026", "&");
            text = text.slice(2, text.length-2);
            let buildingNames: Array<string> = text.split('@%');
            buildingNames.sort();
            for (let i = buildingNames.length; i > 0; i--){
                buildingNames[i] = buildingNames[i-1];
            }
            buildingNames[0] = "N/A";
            this.setState({
                buildingNames : buildingNames,
            })
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };



    render() {
        return (
            <div id="BuildingPicker">
                <label>
                    From: &nbsp;
                    <select defaultValue={this.props.start}
                            onChange={this.onStartInputChange}
                    >
                        {this.state.buildingNames.map(buildingName => {
                            return (
                                <option value={buildingName}> {buildingName} </option>
                            )
                        })}
                    </select>
                </label>
                &nbsp; &nbsp; &nbsp;
                <label>
                    To: &nbsp;
                    <select defaultValue={this.props.end}
                            onChange={this.onEndInputChange}
                    >
                        {this.state.buildingNames.map(buildingName => {
                            return (
                                <option value={buildingName}> {buildingName} </option>
                            )
                        })}
                    </select>
                </label>
            </div>
        );
    }
}
export default BuildingPicker;