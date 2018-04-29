import { fetchOptions, handleErrors, queryParameters, generatePutRequestFromData } from './common';

// Fetch device groups using given filters
export function fetchGroups(filters) {
    const params = queryParameters(filters);

    return fetch('/api/device-groups' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}

// Modify device group with new request body
export function modifyGroup(data) {
    const request = generatePutRequestFromData(data);

    return request()
        .then(handleErrors)
        .then(response => response.json())
}