import { fetchOptions, handleErrors, queryParameters, generatePutRequestFromData } from './common';

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