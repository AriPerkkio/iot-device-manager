import _ from 'lodash';

const ERROR_SEPARATOR = "::";

/**
 * Check fetch response for errors. Thrown error contains detailed description of error.
 */
export function handleErrors(response) {
    if(!response.ok) {

        if(isErrorFromApi(response)) {
            return response.json().then(({collection}) => {
                throw new Error(parseError(collection))
            });
        } else {
            // Generic client side errors, e.g. connectivity issues
            throw new Error([
                "Failed to query API",
                ERROR_SEPARATOR,
                response.status,
                response.statusText
            ].join(" "));
        }
    }

    return response;
}

/**
 * Parse error from application/vnd.collection+json response. Returns title and message separated by ERROR_SEPARATOR
 */
function parseError(collection) {
    const { error } = collection;
    const { title, message } = error;

    return title + ERROR_SEPARATOR + message;
}

/**
 * Check if error response is from API. Readable errors from API should have correct content type
 */
function isErrorFromApi(response) {
    const { headers } = response;

    return headers &&
        headers.has("Content-Type") &&
        /application\/vnd.collection\+json/.test(response.headers.get("Content-Type"));
}

/**
 * Default options to be used in fetch calls.
 */
export const fetchOptions = {
    // By default fetch doesn't include cookies
    credentials: "same-origin",
    headers: {
        "Content-Type": "application/json",
        "Accept": 'application/vnd.collection+json'
    }
}

/**
 * Construct URL query string from key-value pair object
 */
export function queryParameters(params) {

    if(_.isEmpty(params)) {
        return "";
    }

    const all =  _.map(params, (val, key) => ({key,val}))
    const first = all.shift();

    return [
        firstQueryParameter(first),
        ... all.map(nextQueryParameter)
    ].join("");
}

const queryParameter = param => param.key + "=" + param.val;
const firstQueryParameter = param => "?" + queryParameter(param);
const nextQueryParameter = param => "&" + queryParameter(param);

/**
 * Convert application/vnd.collection+json responses collection.item.data array into application/json request body.
 * Also returns href from response object.
 */
export function requestBodyAndUrl(data) {
    const body = {};
    const url = data.concat().pop().href;

    data.forEach(({ name, value }) =>
        body[name] = value);

    return {
        url,
        body: JSON.stringify(body)
    };
}