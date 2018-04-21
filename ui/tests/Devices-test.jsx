import React from 'react';
import { render } from 'enzyme';
import { expect } from 'chai';


import Devices from '../views/devices/Devices';

describe("Devices", function() {
    xit("Initial test", function() {
        const wrapper = render(<Devices />);
        expect(wrapper.text()).to.equal("Devices view");
    });
});
