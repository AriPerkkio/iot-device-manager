import { fetchOptions, handleErrors, queryParameters, requestBodyAndUrl } from './common';

// Fetch devices using given filters
export function fetchDevices(filters) {
    const params = queryParameters(filters);

    return fetch('/api/devices' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}

export function modifyDevice(data) {
    const { body, url } = requestBodyAndUrl(data);
    const options = {
        ... fetchOptions,
        method: "PUT",
        body
    };

    return fetch(url, options)
        .then(handleErrors)
        .then(response => response.json())
}