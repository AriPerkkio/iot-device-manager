import { fetchDevices, modifyDevice, addDevice } from "../../api/devices";

export const DEVICES_LOAD_START = 'DEVICES_LOAD_START';
export const DEVICES_LOAD_SUCCESS = 'DEVICES_LOAD_SUCCESS';
export const DEVICES_LOAD_FAILED = 'DEVICES_LOAD_FAILED';

export const DEVICES_EDIT_START = 'DEVICES_EDIT_START';
export const DEVICES_EDIT_SUCCESS = 'DEVICES_EDIT_SUCCESS';
export const DEVICES_EDIT_FAILED = 'DEVICES_EDIT_FAILED';
export const RESET_DEVICES_EDIT_FAILED = "RESET_DEVICES_EDIT_FAILED";

export const DEVICES_ADD_START = 'DEVICES_ADD_START';
export const DEVICES_ADD_SUCCESS = 'DEVICES_ADD_SUCCESS';
export const DEVICES_ADD_FAILED = 'DEVICES_ADD_FAILED';
export const RESET_DEVICES_ADD_FAILED = "RESET_DEVICES_ADD_FAILED";

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

function devicesAddStart(data) {
    return {
        type: DEVICES_ADD_START,
        data
    }
}

function devicesAddSuccess(json) {
    return {
        type: DEVICES_ADD_SUCCESS,
        json
    }
}

function devicesAddFailed(error) {
    return {
        type: DEVICES_ADD_FAILED,
        error
    }
}

function resetDevicesAddFailed() {
    return {
        type: RESET_DEVICES_ADD_FAILED
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

export function generateAddDevice(dispatch) {
    return (data, url) => {
        dispatch(devicesAddStart(data));
        return addDevice(data, url)
            .then(json => dispatch(devicesAddSuccess(json)))
            .catch(error => dispatch(devicesAddFailed(error)))
    }
}

export function resetModifyErrors(dispatch) {
    dispatch(resetDevicesEditFailed());
}

export function resetAddingErrors(dispatch) {
    dispatch(resetDevicesAddFailed());
}