export function handleErrors(response) {
    if(!response.ok) {
        const message = response.statusText + " " + response.status;
        throw new Error(message);
    }

    return response;
}