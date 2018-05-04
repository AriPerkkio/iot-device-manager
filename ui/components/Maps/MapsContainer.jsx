import React from 'react';

import Maps from './Maps';

export default class MapsContainer extends React.Component {
    render() {
        const markerProps = this.convertItemsToMarkersProps();

        return (
            <Maps {...{
                markerProps
            }}/>
        )
    }

    convertItemsToMarkersProps() {
        const { items } = this.props;

        const mergedItems = _.map(items, ({ data }) =>
            data.reduce((merged, {name, value}) => ({
                ...merged,
                [name]: value
            }), {})
        );

        return mergedItems.map(({ longitude, latitude }) => ({
            lng: longitude,
            lat: latitude,
         }))
    }
}
