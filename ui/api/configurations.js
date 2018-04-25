import { fetchOptions, handleErrors, queryParameters } from './common';

// Fetch configurations using given filters
export function fetchConfigurations(filters) {
    const params = queryParameters(filters);

    return fetch('/api/configurations' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}