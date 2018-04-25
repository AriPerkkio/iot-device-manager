import { fetchOptions, handleErrors, queryParameters } from './common';

// Fetch measurements using given filters
export function fetchMeasurements(filters) {
    const params = queryParameters(filters);

    return fetch('/api/measurements' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}