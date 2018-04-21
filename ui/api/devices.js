import { handleErrors } from './common';

// Fetch devices using given filters
export function fetchDevices(dispatcher){
    return function(filters) {
        return fetch('/api/devices')
            .then(handleErrors)
            .then(response => response.json())
    }
}