import { fetchOptions, handleErrors, queryParameters } from './common';

// Fetch device types using given filters
export function fetchTypes(filters) {
    const params = queryParameters(filters);

    return fetch('/api/device-types' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}