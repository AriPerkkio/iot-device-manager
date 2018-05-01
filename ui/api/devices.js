import { fetchOptions, handleErrors, queryParameters, generatePutRequestFromData, generatePostRequestFromData } from './common';

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

export function addDevice(data, href) {
    const request = generatePostRequestFromData(data, href);

    return request()
        .then(handleErrors)
        .then(response => response.json())
}