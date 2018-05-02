import { fetchOptions, handleErrors, queryParameters, generatePutRequestFromData, generatePostRequestFromData, generateDeleteRequestFromData, parseIdFromResponse } from './common';

// Fetch type types using given filters
export function fetchTypes(filters) {
    const params = queryParameters(filters);

    return fetch('/api/device-types' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}

// Modify type with new request body
export function modifyType(data) {
    const request = generatePutRequestFromData(data);

    return request()
        .then(handleErrors)
        .then(response => response.json())
}

// Add type from request body
export function addType(data, href) {
    const request = generatePostRequestFromData(data, href);

    return request()
        .then(handleErrors)
        .then(response => response.json())
}

// Delete type by id
export function deleteType(data) {
    const request = generateDeleteRequestFromData(data);

    return request()
        .then(handleErrors)
        .then(parseIdFromResponse)
}