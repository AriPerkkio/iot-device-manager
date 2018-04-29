import { fetchDevices, modifyDevice } from "../../api/devices";

export const DEVICES_LOAD_START = 'DEVICES_LOAD_START';
export const DEVICES_LOAD_SUCCESS = 'DEVICES_LOAD_SUCCESS';
export const DEVICES_LOAD_FAILED = 'DEVICES_LOAD_FAILED';

export const DEVICES_EDIT_START = 'DEVICES_EDIT_START';
export const DEVICES_EDIT_SUCCESS = 'DEVICES_EDIT_SUCCESS';
export const DEVICES_EDIT_FAILED = 'DEVICES_EDIT_FAILED';

export const RESET_DEVICES_EDIT_FAILED = "RESET_DEVICES_EDIT_FAILED";

function devicesLoadStart(filters) {
    return {
        type: DEVICES_LOAD_START,
        filters
    }
}

function devicesLoadSuccess(json) {
    return {
        type: DEVICES_LOAD_SUCCESS,
        json
    }
}

function devicesLoadFailed(error) {
    return {
        type: DEVICES_LOAD_FAILED,
        error
    }
}

function devicesEditStart(data) {
    return {
        type: DEVICES_EDIT_START,
        data
    }
}

function devicesEditSuccess(json) {
    return {
        type: DEVICES_EDIT_SUCCESS,
        json
    }
}

function devicesEditFailed(error) {
    return {
        type: DEVICES_EDIT_FAILED,
        error
    }
}

function resetDevicesEditFailed() {
    return {
        type: RESET_DEVICES_EDIT_FAILED
    }
}

export function generateGetDevices(dispatch) {
    return filters => {
        dispatch(devicesLoadStart(filters))
        return fetchDevices(filters)
            .then(json => dispatch(devicesLoadSuccess(json)))
            .catch(error => dispatch(devicesLoadFailed(error)))
    }
}

export function generateModifyDevice(dispatch) {
    return data => {
        dispatch(devicesEditStart(data))
        return modifyDevice(data)
            .then(json => dispatch(devicesEditSuccess(json)))
            .catch(error => dispatch(devicesEditFailed(error)))
    }
}

export function resetModifyErrors(dispatch) {
    dispatch(resetDevicesEditFailed());
}