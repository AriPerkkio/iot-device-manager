import { fetchOptions, handleErrors, queryParameters } from './common';

// Fetch devices using given filters
export function fetchDevices(filters) {
    const params = queryParameters(filters);

    return fetch('/api/devices' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}