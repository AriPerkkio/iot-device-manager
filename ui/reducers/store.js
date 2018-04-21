import { createStore, combineReducers, applyMiddleware } from 'redux';
import createHistory from 'history/createBrowserHistory';
import { routerReducer, routerMiddleware } from 'react-router-redux';
import logger from 'redux-logger';

// Reducers
import DevicesReducer from './devices/reducer';

export const history = createHistory();

export const store = createStore(
    combineReducers({
        router: routerReducer,
        devices: DevicesReducer
    }),
    applyMiddleware(routerMiddleware(history), logger)
);

