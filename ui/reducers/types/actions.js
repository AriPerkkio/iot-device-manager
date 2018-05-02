import { fetchTypes, modifyType, addType, deleteType } from "../../api/types";

export const TYPES_LOAD_START = 'TYPES_LOAD_START';
export const TYPES_LOAD_SUCCESS = 'TYPES_LOAD_SUCCESS';
export const TYPES_LOAD_FAILED = 'TYPES_LOAD_FAILED';

export const TYPES_EDIT_START = 'TYPES_EDIT_START';
export const TYPES_EDIT_SUCCESS = 'TYPES_EDIT_SUCCESS';
export const TYPES_EDIT_FAILED = 'TYPES_EDIT_FAILED';
export const RESET_TYPES_EDIT_FAILED = "RESET_TYPES_EDIT_FAILED";

export const TYPES_ADD_START = 'TYPES_ADD_START';
export const TYPES_ADD_SUCCESS = 'TYPES_ADD_SUCCESS';
export const TYPES_ADD_FAILED = 'TYPES_ADD_FAILED';
export const RESET_TYPES_ADD_FAILED = "RESET_TYPES_ADD_FAILED";

export const TYPES_DELETE_START = "TYPES_DELETE_START";
export const TYPES_DELETE_SUCCESS = "TYPES_DELETE_SUCCESS";
export const TYPES_DELETE_FAILED =  "TYPES_DELETE_FAILED";

function typesLoadStart(filters) {
    return {
        type: TYPES_LOAD_START,
        filters
    }
}

function typesLoadSuccess(json) {
    return {
        type: TYPES_LOAD_SUCCESS,
        json
    }
}

function typesLoadFailed(error) {
    return {
        type: TYPES_LOAD_FAILED,
        error
    }
}

function typesEditStart(data) {
    return {
        type: TYPES_EDIT_START,
        data
    }
}

function typesEditSuccess(json) {
    return {
        type: TYPES_EDIT_SUCCESS,
        json
    }
}

function typesEditFailed(error) {
    return {
        type: TYPES_EDIT_FAILED,
        error
    }
}

function resetTypesEditFailed() {
    return {
        type: RESET_TYPES_EDIT_FAILED
    }
}

function typesAddStart(data) {
    return {
        type: TYPES_ADD_START,
        data
    }
}

function typesAddSuccess(json) {
    return {
        type: TYPES_ADD_SUCCESS,
        json
    }
}

function typesAddFailed(error) {
    return {
        type: TYPES_ADD_FAILED,
        error
    }
}

function resetTypesAddFailed() {
    return {
        type: RESET_TYPES_ADD_FAILED
    }
}

function typesDeleteStart(data) {
    return {
        type: TYPES_DELETE_START,
        data
    }
}

function typesDeleteSuccess(json) {
    return {
        type: TYPES_DELETE_SUCCESS,
        json
    }
}

function typesDeleteFailed(error) {
    return {
        type: TYPES_DELETE_FAILED,
        error
    }
}

export function generateGetTypes(dispatch) {
    return filters => {
        dispatch(typesLoadStart(filters))
        return fetchTypes(filters)
            .then(json => dispatch(typesLoadSuccess(json)))
            .catch(error => dispatch(typesLoadFailed(error)))
    }
}

export function generateModifyType(dispatch) {
    return data => {
        dispatch(typesEditStart(data))
        return modifyType(data)
            .then(json => dispatch(typesEditSuccess(json)))
            .catch(error => dispatch(typesEditFailed(error)))
    }
}

export function generateAddType(dispatch) {
    return (data, url) => {
        dispatch(typesAddStart(data));
        return addType(data, url)
            .then(json => dispatch(typesAddSuccess(json)))
            .catch(error => dispatch(typesAddFailed(error)))
    }
}

export function generateDeleteType(dispatch) {
    return data => {
        dispatch(typesDeleteStart(data));
        return deleteType(data)
            .then(id => dispatch(typesDeleteSuccess(id)))
            .catch(error => dispatch(typesDeleteFailed(error)))
    }
}

export function resetModifyErrors(dispatch) {
    dispatch(resetTypesEditFailed());
}

export function resetAddingErrors(dispatch) {
    dispatch(resetTypesAddFailed());
}