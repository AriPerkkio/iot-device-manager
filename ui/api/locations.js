import { fetchOptions, handleErrors, queryParameters, generateDeleteRequestFromData, parseIdFromResponse } from './common';

// Fetch locations using given filters
export function fetchLocations(filters) {
    const params = queryParameters(filters);

    return fetch('/api/locations' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}

// Delete location by id
export function deleteLocation(data) {
    const request = generateDeleteRequestFromData(data);

    return request()
        .then(handleErrors)
        .then(parseIdFromResponse)
}