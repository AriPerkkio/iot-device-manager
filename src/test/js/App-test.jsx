import React from 'react';
import { render } from 'enzyme';
import { expect } from 'chai';


import App from '../../main/js/App.jsx';

describe("App", function() {
    it("Initial test", function() {
        const wrapper = render(<App />);
        expect(wrapper.html()).to.equal("Initial app");
    });
});
