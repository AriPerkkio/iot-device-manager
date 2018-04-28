import { fetchOptions, handleErrors, queryParameters, requestBodyWithId } from './common';

// Fetch devices using given filters
export function fetchDevices(filters) {
    const params = queryParameters(filters);

    return fetch('/api/devices' + params, fetchOptions)
        .then(handleErrors)
        .then(response => response.json())
}

export function modifyDevice(data) {
    const { body, id } = requestBodyWithId(data);
    const options = {
        ... fetchOptions,
        method: "PUT",
        body
    };

    return fetch(`/api/devices/${id}`, options)
        .then(handleErrors)
        .then(response => response.json())
}