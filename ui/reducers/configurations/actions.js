import { fetchConfigurations, modifyConfiguration, addConfiguration, deleteConfiguration } from "../../api/configurations";

export const CONFIGURATIONS_LOAD_START = 'CONFIGURATIONS_LOAD_START';
export const CONFIGURATIONS_LOAD_SUCCESS = 'CONFIGURATIONS_LOAD_SUCCESS';
export const CONFIGURATIONS_LOAD_FAILED = 'CONFIGURATIONS_LOAD_FAILED';

export const CONFIGURATIONS_EDIT_START = 'CONFIGURATIONS_EDIT_START';
export const CONFIGURATIONS_EDIT_SUCCESS = 'CONFIGURATIONS_EDIT_SUCCESS';
export const CONFIGURATIONS_EDIT_FAILED = 'CONFIGURATIONS_EDIT_FAILED';
export const RESET_CONFIGURATIONS_EDIT_FAILED = "RESET_CONFIGURATIONS_EDIT_FAILED";

export const CONFIGURATIONS_ADD_START = 'CONFIGURATIONS_ADD_START';
export const CONFIGURATIONS_ADD_SUCCESS = 'CONFIGURATIONS_ADD_SUCCESS';
export const CONFIGURATIONS_ADD_FAILED = 'CONFIGURATIONS_ADD_FAILED';
export const RESET_CONFIGURATIONS_ADD_FAILED = "RESET_CONFIGURATIONS_ADD_FAILED";

export const CONFIGURATIONS_DELETE_START = "CONFIGURATIONS_DELETE_START";
export const CONFIGURATIONS_DELETE_SUCCESS = "CONFIGURATIONS_DELETE_SUCCESS";
export const CONFIGURATIONS_DELETE_FAILED =  "CONFIGURATIONS_DELETE_FAILED";

function configurationsLoadStart(filters) {
    return {
        type: CONFIGURATIONS_LOAD_START,
        filters
    }
}

function configurationsLoadSuccess(json) {
    return {
        type: CONFIGURATIONS_LOAD_SUCCESS,
        json
    }
}

function configurationsLoadFailed(error) {
    return {
        type: CONFIGURATIONS_LOAD_FAILED,
        error
    }
}

function configurationsEditStart(data) {
    return {
        type: CONFIGURATIONS_EDIT_START,
        data
    }
}

function configurationsEditSuccess(json) {
    return {
        type: CONFIGURATIONS_EDIT_SUCCESS,
        json
    }
}

function configurationsEditFailed(error) {
    return {
        type: CONFIGURATIONS_EDIT_FAILED,
        error
    }
}

function resetConfigurationsEditFailed() {
    return {
        type: RESET_CONFIGURATIONS_EDIT_FAILED
    }
}

function configurationsAddStart(data) {
    return {
        type: CONFIGURATIONS_ADD_START,
        data
    }
}

function configurationsAddSuccess(json) {
    return {
        type: CONFIGURATIONS_ADD_SUCCESS,
        json
    }
}

function configurationsAddFailed(error) {
    return {
        type: CONFIGURATIONS_ADD_FAILED,
        error
    }
}

function resetConfigurationsAddFailed() {
    return {
        type: RESET_CONFIGURATIONS_ADD_FAILED
    }
}

function configurationsDeleteStart(data) {
    return {
        type: CONFIGURATIONS_DELETE_START,
        data
    }
}

function configurationsDeleteSuccess(json) {
    return {
        type: CONFIGURATIONS_DELETE_SUCCESS,
        json
    }
}

function configurationsDeleteFailed(error) {
    return {
        type: CONFIGURATIONS_DELETE_FAILED,
        error
    }
}

export function generateGetConfigurations(dispatch) {
    return filters => {
        dispatch(configurationsLoadStart(filters))
        return fetchConfigurations(filters)
            .then(json => dispatch(configurationsLoadSuccess(json)))
            .catch(error => dispatch(configurationsLoadFailed(error)))
    }
}

export function generateModifyConfiguration(dispatch) {
    return data => {
        dispatch(configurationsEditStart(data))
        return modifyConfiguration(data)
            .then(json => dispatch(configurationsEditSuccess(json)))
            .catch(error => dispatch(configurationsEditFailed(error)))
    }
}

export function generateAddConfiguration(dispatch) {
    return (data, url) => {
        dispatch(configurationsAddStart(data));
        return addConfiguration(data, url)
            .then(json => dispatch(configurationsAddSuccess(json)))
            .catch(error => dispatch(configurationsAddFailed(error)))
    }
}

export function generateDeleteConfiguration(dispatch) {
    return data => {
        dispatch(configurationsDeleteStart(data));
        return deleteConfiguration(data)
            .then(id => dispatch(configurationsDeleteSuccess(id)))
            .catch(error => dispatch(configurationsDeleteFailed(error)))
    }
}

export function resetModifyErrors(dispatch) {
    dispatch(resetConfigurationsEditFailed());
}

export function resetAddingErrors(dispatch) {
    dispatch(resetConfigurationsAddFailed());
}