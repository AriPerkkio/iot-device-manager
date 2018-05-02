import { fetchGroups, modifyGroup, addGroup, deleteGroup } from "../../api/groups";

export const GROUPS_LOAD_START = 'GROUPS_LOAD_START';
export const GROUPS_LOAD_SUCCESS = 'GROUPS_LOAD_SUCCESS';
export const GROUPS_LOAD_FAILED = 'GROUPS_LOAD_FAILED';

export const GROUPS_EDIT_START = 'GROUPS_EDIT_START';
export const GROUPS_EDIT_SUCCESS = 'GROUPS_EDIT_SUCCESS';
export const GROUPS_EDIT_FAILED = 'GROUPS_EDIT_FAILED';
export const RESET_GROUPS_EDIT_FAILED = 'RESET_GROUPS_EDIT_FAILED';

export const GROUPS_ADD_START = 'GROUPS_ADD_START';
export const GROUPS_ADD_SUCCESS = 'GROUPS_ADD_SUCCESS';
export const GROUPS_ADD_FAILED = 'GROUPS_ADD_FAILED';
export const RESET_GROUPS_ADD_FAILED = 'RESET_GROUPS_ADD_FAILED';

export const GROUPS_DELETE_START = "GROUPS_DELETE_START";
export const GROUPS_DELETE_SUCCESS = "GROUPS_DELETE_SUCCESS";
export const GROUPS_DELETE_FAILED =  "GROUPS_DELETE_FAILED";

function groupsLoadStart(filters) {
    return {
        type: GROUPS_LOAD_START,
        filters
    }
}

function groupsLoadSuccess(json) {
    return {
        type: GROUPS_LOAD_SUCCESS,
        json
    }
}

function groupsLoadFailed(error) {
    return {
        type: GROUPS_LOAD_FAILED,
        error
    }
}

function groupsEditStart(data) {
    return {
        type: GROUPS_EDIT_START,
        data
    }
}

function groupsEditSuccess(json) {
    return {
        type: GROUPS_EDIT_SUCCESS,
        json
    }
}

function groupsEditFailed(error) {
    return {
        type: GROUPS_EDIT_FAILED,
        error
    }
}

function resetGroupEditFailed() {
    return {
        type: RESET_GROUPS_EDIT_FAILED
    }
}

function groupsAddStart(data) {
    return {
        type: GROUPS_ADD_START,
        data
    }
}

function groupsAddSuccess(json) {
    return {
        type: GROUPS_ADD_SUCCESS,
        json
    }
}

function groupsAddFailed(error) {
    return {
        type: GROUPS_ADD_FAILED,
        error
    }
}

function resetGroupsAddFailed() {
    return {
        type: RESET_GROUPS_ADD_FAILED
    }
}

function groupsDeleteStart(data) {
    return {
        type: GROUPS_DELETE_START,
        data
    }
}

function groupsDeleteSuccess(json) {
    return {
        type: GROUPS_DELETE_SUCCESS,
        json
    }
}

function groupsDeleteFailed(error) {
    return {
        type: GROUPS_DELETE_FAILED,
        error
    }
}

export function generateGetGroups(dispatch) {
    return filters => {
        dispatch(groupsLoadStart(filters))
        return fetchGroups(filters)
            .then(json => dispatch(groupsLoadSuccess(json)))
            .catch(error => dispatch(groupsLoadFailed(error)))
    }
}

export function generateModifyGroups(dispatch) {
    return data => {
        dispatch(groupsEditStart(data));
        return modifyGroup(data)
            .then(json => dispatch(groupsEditSuccess(json)))
            .catch(error => dispatch(groupsEditFailed(error)))
    }
}

export function generateAddGroup(dispatch) {
    return (data, url) => {
        dispatch(groupsAddStart());
        return addGroup(data, url)
            .then(json => dispatch(groupsAddSuccess(json)))
            .catch(error => dispatch(groupsAddFailed(error)))
    }
}

export function generateDeleteGroup(dispatch) {
    return data => {
        dispatch(groupsDeleteStart(data));
        return deleteGroup(data)
            .then(id => dispatch(groupsDeleteSuccess(id)))
            .catch(error => dispatch(groupsDeleteFailed(error)))
    }
}

export function resetModifyErrors(dispatch) {
    dispatch(resetGroupEditFailed());
}

export function resetAddingErrors(dispatch) {
    dispatch(resetGroupsAddFailed());
}