import { fetchOptions, handleErrors, queryParameters, generatePutRequestFromData, generatePostRequestFromData, generateDeleteRequestFromData, parseIdFromResponse } from './common';

// Fetch configurations using given filters
export function fetchConfigurations(filters) {
    const params = queryParameters(filters);

    return fetch('/api/configurations' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}

// Modify configuration with new request body
export function modifyConfiguration(data) {
    const request = generatePutRequestFromData(data);

    return request()
        .then(handleErrors)
        .then(response => response.json())
}

// Add configuration from request body
export function addConfiguration(data, href) {
    const request = generatePostRequestFromData(data, href);

    return request()
        .then(handleErrors)
        .then(response => response.json())
}

// Delete configuration by id
export function deleteConfiguration(data) {
    const request = generateDeleteRequestFromData(data);

    return request()
        .then(handleErrors)
        .then(parseIdFromResponse)
}