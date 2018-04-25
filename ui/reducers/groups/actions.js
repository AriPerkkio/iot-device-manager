import { fetchGroups } from "../../api/groups";

export const GROUPS_LOAD_START = 'GROUPS_LOAD_START';
export const GROUPS_LOAD_SUCCESS = 'GROUPS_LOAD_SUCCESS';
export const GROUPS_LOAD_FAILED = 'GROUPS_LOAD_FAILED';

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

export function generateGetGroups(dispatch) {
    return filters => {
        dispatch(groupsLoadStart(filters))
        return fetchGroups(filters)
            .then(json => dispatch(groupsLoadSuccess(json)))
            .catch(error => dispatch(groupsLoadFailed(error)))
    }
}
