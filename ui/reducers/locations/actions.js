import { fetchLocations } from "../../api/locations";

export const LOCATIONS_LOAD_START = 'LOCATIONS_LOAD_START';
export const LOCATIONS_LOAD_SUCCESS = 'LOCATIONS_LOAD_SUCCESS';
export const LOCATIONS_LOAD_FAILED = 'LOCATIONS_LOAD_FAILED';

export const LOCATIONS_DELETE_START = "LOCATIONS_DELETE_START";
export const LOCATIONS_DELETE_SUCCESS = "LOCATIONS_DELETE_SUCCESS";
export const LOCATIONS_DELETE_FAILED =  "LOCATIONS_DELETE_FAILED";

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

function locationsDeleteStart(data) {
    return {
        type: LOCATIONS_DELETE_START,
        data
    }
}

function locationsDeleteSuccess(json) {
    return {
        type: LOCATIONS_DELETE_SUCCESS,
        json
    }
}

function locationsDeleteFailed(error) {
    return {
        type: LOCATIONS_DELETE_FAILED,
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

export function generateDeleteLocation(dispatch) {
    return data => {
        dispatch(locationsDeleteStart(data));
        return deleteLocation(data)
            .then(id => dispatch(locationsDeleteSuccess(id)))
            .catch(error => dispatch(locationsDeleteFailed(error)))
    }
}
