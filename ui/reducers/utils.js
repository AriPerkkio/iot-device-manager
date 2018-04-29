/**
 * Parse application/vnd.collection+json response into item - id map
*/
function parseItems(collection) {
    const items = {}

    collection.items.forEach(item => {
        const id = (item.data
            .filter(({name}) => name === "id")
            .shift() || {}).value;

        items[id] = item;
    })

    return items;
}

/**
 * Parse application/vnd.collection+json response into rel - link map
*/
function parseLinks(collection) {
    const links = {};

    collection.links.forEach(link => {
        links[link.rel] = link;
    });

    return links;
}

// Common initial state for reducers handling API queries. Includes only attributes supported by all reducers
export const initialState = {
    // API responses' payload. See documentation of application/vnd.collection+json
    items: {},
    links: {},
    queries: [],
    template: {},

    // GET methods
    isFetching: false,
    hasFetched: false,
    fetchingError: false,
    fetchingErrorMessage: "",

};

// Initial state for reducers supporting item modification. (All except location and measurements)
export const initialStateWithUpdate = {
    ...initialState,

    // PUT methods
    isUpdating: false,
    hasUpdated: false,
    updateError: false,
    updateErrorMessage: "",
}

export function setLoadStart(state) {
    return {
        ...state,
        isFetching: true,
        fetchingError: false
    }
}

export function setLoadSuccess(state, { collection }) {
    const { queries, template } = collection;

    const items = {
        ...state.items,
        ...parseItems(collection)
    };

    const links = {
        ...state.links,
        ...parseLinks(collection)
    };

    return {
        ...state,
        isFetching: false,
        hasFetched: true,
        fetchingError: false,
        queries,
        template,
        items,
        links
    }
}

export function setLoadFailed(state, { error }) {
    return {
        ...state,
        isFetching: false,
        fetchingError: true,
        fetchingErrorMessage: error.message
    }
}

export function setEditStart(state) {
    return {
        ...state,
        isUpdating: true,
        hasUpdated: false,
        updateError: false
    }
}

export function setEditSuccess(state, { collection }) {
    const items = {
        ...state.items,
        ...parseItems(collection)
    };

    const links = {
        ...state.links,
        ...parseLinks(collection)
    };

    return {
        ...state,
        isUpdating: false,
        hasUpdated: true,
        updateError: false,
        items,
        links
    }
}

export function setEditFailed(state, { error }) {
    return {
        ...state,
        isUpdating: false,
        updateError: true,
        updateErrorMessage: error.message
    }
}

export function resetEditErrors(state) {
    return {
        ...state,
        updateError: false,
        updateErrorMessage: ""
    }
}
