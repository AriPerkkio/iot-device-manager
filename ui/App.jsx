import React from 'react';

// State
import { Provider } from 'react-redux';
import { store, history } from './reducers/store';

// Router
import { ConnectedRouter } from 'react-router-redux';
import { Route } from 'react-router';

// Views
import Home from './views/home/Home';
import Devices from './views/devices/DevicesContainer';
import Groups from './views/groups/Groups';
import Types from './views/types/Types';
import Configurations from './views/configurations/Configurations';
import Locations from './views/locations/Locations';
import Measurements from './views/measurements/Measurements';

// Components
import Header from './components/Header';

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
