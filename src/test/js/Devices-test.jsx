import React from 'react';
import { render } from 'enzyme';
import { expect } from 'chai';


import Devices from '../../main/js/views/devices/Devices';

describe("Devices", function() {
    it("Initial test", function() {
        const wrapper = render(<Devices />);
        expect(wrapper.html()).to.equal("Devices view");
    });
});
