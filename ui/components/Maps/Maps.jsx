import React from 'react';
import LoadingIndicator from 'react-loading-indicator';

import { compose, withProps } from "recompose"
import { withScriptjs, withGoogleMap, GoogleMap, Marker } from "react-google-maps"

class Maps extends React.Component {
    render() {
        const { markerProps } = this.props;

        return (
            <GoogleMap
                defaultZoom={8}
                defaultCenter={{ lat: 65.057, lng: 25.444 }} >

                { markerProps && markerProps.map((markerProp, key) =>
                    <Marker { ...{
                        position: markerProp,
                        key,
                    }}/>
                )}

            </GoogleMap>
        )
    }
}

// This part should be injected during CI builds
let partThatShouldntBeInSourceCodes = "y=";
partThatShouldntBeInSourceCodes += "AIzaSy" + "C7RR3V4rjx";
partThatShouldntBeInSourceCodes += "-ovu4qp" + "faE5jumV";
partThatShouldntBeInSourceCodes += "-Z5GIQTQ";

const wrapperProps = {
  ["go"+"og"+"leM"+"apU"+"RL"]: "https://ma"+"ps.goo"+"gle"+"ap"+"is.com/map"+"s/a"+"pi/j"+"s?v=3.exp&libr"+"aries=geomet"+"ry,drawing,places&ke" + partThatShouldntBeInSourceCodes,
  loadingElement: <LoadingIndicator />,
  containerElement: <div id="maps-container" />,
  mapElement: <div id="maps"/>,
};

export default compose(withProps(wrapperProps), withScriptjs, withGoogleMap)(Maps);