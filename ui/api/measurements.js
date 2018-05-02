import { fetchOptions, handleErrors, queryParameters, generateDeleteRequestFromData, parseIdFromResponse } from './common';

// Fetch measurements using given filters
export function fetchMeasurements(filters) {
    const params = queryParameters(filters);

    return fetch('/api/measurements' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}

// Delete measurement by id
export function deleteMeasurement(data) {
    const request = generateDeleteRequestFromData(data);

    return request()
        .then(handleErrors)
        .then(parseIdFromResponse)
}