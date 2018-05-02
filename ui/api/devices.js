import { fetchOptions, handleErrors, queryParameters,
    generatePutRequestFromData, generatePostRequestFromData, generateDeleteRequestFromData, parseIdFromResponse } from './common';

// Fetch devices using given filters
export function fetchDevices(filters) {
    const params = queryParameters(filters);

    return fetch('/api/devices' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}

// Modify device with new request body
export function modifyDevice(data) {
    const request = generatePutRequestFromData(data);

    return request()
        .then(handleErrors)
        .then(response => response.json())
}

// Add device from request body
export function addDevice(data, href) {
    const request = generatePostRequestFromData(data, href);

    return request()
        .then(handleErrors)
        .then(response => response.json())
}

// Delete device by id
export function deleteDevice(data) {
    const request = generateDeleteRequestFromData(data);

    return request()
        .then(handleErrors)
        .then(parseIdFromResponse)
}