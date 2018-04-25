import { fetchLocations } from "../../api/locations";

export const LOCATIONS_LOAD_START = 'LOCATIONS_LOAD_START';
export const LOCATIONS_LOAD_SUCCESS = 'LOCATIONS_LOAD_SUCCESS';
export const LOCATIONS_LOAD_FAILED = 'LOCATIONS_LOAD_FAILED';

function locationsLoadStart(filters) {
    return {
        type: LOCATIONS_LOAD_START,
        filters
    }
}

function locationsLoadSuccess(json) {
    return {
        type: LOCATIONS_LOAD_SUCCESS,
        json
    }
}

function locationsLoadFailed(error) {
    return {
        type: LOCATIONS_LOAD_FAILED,
        error
    }
}

export function generateGetLocations(dispatch) {
    return filters => {
        dispatch(locationsLoadStart(filters))
        return fetchLocations(filters)
            .then(json => dispatch(locationsLoadSuccess(json)))
            .catch(error => dispatch(locationsLoadFailed(error)))
    }
}
