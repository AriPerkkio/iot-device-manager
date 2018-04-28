/**
 * Parse application/vnd.collection+json response into item - id map
*/
export function parseItems(collection) {
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
export function parseLinks(collection) {
    const links = {};

    collection.links.forEach(link => {
        links[link.rel] = link;
    });

    return links;
}
