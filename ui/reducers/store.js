import { createStore, combineReducers, applyMiddleware } from 'redux';
import createHistory from 'history/createBrowserHistory';
import { routerReducer, routerMiddleware } from 'react-router-redux';
import { createLogger } from 'redux-logger'

// Reducers
import DevicesReducer from './devices/reducer';
import GroupsReducer from './groups/reducer';
import TypesReducer from './types/reducer';
import ConfigurationsReducer from './configurations/reducer';
import MeasurementsReducer from './measurements/reducer';
import LocationsReducer from './locations/reducer';

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
        devices: DevicesReducer,
        groups: GroupsReducer,
        types: TypesReducer,
        configurations: ConfigurationsReducer,
        measurements: MeasurementsReducer,
        locations: LocationsReducer,
    }),
    applyMiddleware(... middlewares)
);

