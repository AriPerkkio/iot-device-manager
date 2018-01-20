import React from 'react';

// State
import { createStore, combineReducers, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';

// Router
import createHistory from 'history/createBrowserHistory';
import { ConnectedRouter, routerReducer, routerMiddleware } from 'react-router-redux';
import { Route } from 'react-router';

// Views
import Home from './views/home/Home';
import Devices from './views/devices/Devices';

const history = createHistory();
const middleware = routerMiddleware(history);

const store = createStore(
  combineReducers({
    router: routerReducer
  }),
  applyMiddleware(middleware)
);

export default class App extends React.Component {

    render() {
        return (
            <Provider store={store}>
                <ConnectedRouter history={history}>
                    <div>
                        <Route exact path="/" component={Home}/>
                        <Route path="/devices" component={Devices}/>
                    </div>
                </ConnectedRouter>
            </Provider>
        );
    }
}
