import _ from 'lodash';

export function handleErrors(response) {
    if(!response.ok) {
        return response.json().then(({collection}) => {
            throw new Error(parseError(collection));
        })
    }

    return response;
}

function parseError(collection) {
    const { error } = collection;
    const { title, message } = error;

    return title + ": " + message;
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