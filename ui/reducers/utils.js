// Parse items into id - item map
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

// Parse links into rel - link map
export function parseLinks(collection) {
    const links = {};

    collection.links.forEach(link => {
        links[link.rel] = link;
    });

    return links;
}
