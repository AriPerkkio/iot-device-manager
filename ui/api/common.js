import _ from 'lodash';

const ERROR_SEPARATOR = "::";

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

function parseError(collection) {
    const { error } = collection;
    const { title, message } = error;

    return title + ERROR_SEPARATOR + message;
}

// Readable errors from API should have correct content type
function isErrorFromApi(response) {
    const { headers } = response;

    return headers &&
        headers.has("Content-Type") &&
        /application\/vnd.collection\+json/.test(response.headers.get("Content-Type"));
}

export const fetchOptions = {
    // By default fetch doesn't include cookies
    credentials: "same-origin"
}

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