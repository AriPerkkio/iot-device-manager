import { fetchOptions, handleErrors, queryParameters } from './common';

// Fetch locations using given filters
export function fetchLocations(filters) {
    const params = queryParameters(filters);

    return fetch('/api/locations' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}