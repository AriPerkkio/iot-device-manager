import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

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
import Groups from './views/groups/Groups';
import Types from './views/types/Types';
import Configurations from './views/configurations/Configurations';
import Locations from './views/locations/Locations';
import Measurements from './views/measurements/Measurements';

// Components
import Header from './components/Header';

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
                        <Header />
                        <Route exact path="/" component={Home}/>
                        <Route path="/devices" component={Devices}/>
                        <Route path="/groups" component={Groups}/>
                        <Route path="/types" component={Types}/>
                        <Route path="/configurations" component={Configurations}/>
                        <Route path="/location-updates" component={Locations}/>
                        <Route path="/measurements" component={Measurements}/>
                    </div>
                </ConnectedRouter>
            </Provider>
        );
    }
}
