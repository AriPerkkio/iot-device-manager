import React from 'react';
import { Navbar, NavItem, NavLink, Nav } from 'reactstrap';


export default class Header extends React.Component {

    render() {
        return (
            <Navbar color="light" light expand="md">
                <Nav className="ml-auto" navbar>
                    <NavItem>
                        <NavLink href="/devices">Devices</NavLink>
                    </NavItem>
                </Nav>
            </Navbar>
        );
    }
}
