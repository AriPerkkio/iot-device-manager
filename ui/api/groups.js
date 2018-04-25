import { fetchOptions, handleErrors, queryParameters } from './common';

// Fetch device groups using given filters
export function fetchGroups(filters) {
    const params = queryParameters(filters);

    return fetch('/api/device-groups' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}