import React from 'react';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import {ProductLink} from '../../common';

const UserProducts = ({userProducts}) => (

    <table className="table table-striped table-hover">

        <thead>
            <tr>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.name'/>
                </th>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.remainingTime'/>
                </th>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.currentPrice'/>
                </th>
                <th scope="col">
                    <FormattedMessage id='project.global.fields.winnerEmail'/>
                </th>
            </tr>
        </thead>

        <tbody>
            {userProducts.map((userProduct, index) => 
                <tr key={index}>
                    <td><ProductLink id={userProduct.id} name={userProduct.name}/></td>
                    <td>{userProduct.remainingTime}</td>
                    <td>{userProduct.currentPrice}</td>
                    <td>{userProduct.winnerEmail}</td>
                </tr>
            )}
        </tbody>

    </table>

);

UserProducts.propTypes = {
    userProducts: PropTypes.array.isRequired,
};

export default UserProducts;