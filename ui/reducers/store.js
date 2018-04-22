import { createStore, combineReducers, applyMiddleware } from 'redux';
import createHistory from 'history/createBrowserHistory';
import { routerReducer, routerMiddleware } from 'react-router-redux';
import { createLogger } from 'redux-logger'

// Reducers
import DevicesReducer from './devices/reducer';

export const history = createHistory();
const middlewares = [
    routerMiddleware(history)
];

if (process.env.NODE_ENV === `development`) {
    const logger = createLogger({
        predicate: (getState, action) => !/router/.test(action.type)
    });
    middlewares.push(logger);
}

export const store = createStore(
    combineReducers({
        router: routerReducer,
        devices: DevicesReducer
    }),
    applyMiddleware(... middlewares)
);

