import {FC, ReactElement} from 'react';
import './navbar.less'
import {Menu, Row, Col} from "antd";
import UserStatus from "../UserStatus";
import {useCurrentPathObj} from "../../../utils/hooks";
import {Link} from "react-router-dom";
const MenuItem = Menu.Item


interface IProps{
    pathArr:Array<{name:string,path:string}>
}

const NavBar:FC<IProps> = (props):ReactElement => {

    const currentPathObj = useCurrentPathObj(props.pathArr)
    const selectPathObj:Array<any> = [currentPathObj?.path]

    return (
        <div className={'navbar'}>
            <Row align={'middle'}>
                <Col offset={3} style={{minWidth:'90px'}} span={2}>
                    <div className={'title-font logo'}>CrowOj</div>
                </Col>
                <Col span={15}>
                    <Menu className={'menu'} theme={"light"} defaultSelectedKeys={selectPathObj}  mode='horizontal'>
                        {props.pathArr.map(value => <MenuItem key={value.path}><Link to={value.path}>{value.name}</Link></MenuItem>)}
                    </Menu>
                </Col>
                <Col span={1} >
                    <UserStatus/>
                </Col>
            </Row>
        </div>
    );
};

export default NavBar;
