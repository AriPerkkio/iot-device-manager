import React from 'react';
import { Navbar, NavItem, Nav, NavLink, NavbarBrand, Input, Form } from 'reactstrap';
import { Link } from 'react-router-dom';

export default class Header extends React.Component {

    render() {
        return (
            <Navbar color="light">
                <Nav>
                    <NavItem>
                        <NavLink tag={Link} to="/devices">Devices</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink tag={Link} to="/groups">Groups</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink tag={Link} to="/types">Types</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink tag={Link} to="/configurations">Configurations</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink tag={Link} to="/location-updates">Location updates</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink tag={Link} to="/measurements">Measurements</NavLink>
                    </NavItem>
                    <NavbarBrand tag={Link} to="/">Iot Device Manager</NavbarBrand>
                    <NavItem>
                        <Form action="/logout" method="post">
                            <Input type="submit" value="Log Out" className="btn btn-sm btn-primary logout-btn" />
                        </Form>
                    </NavItem>
                </Nav>
            </Navbar>
        );
    }
}