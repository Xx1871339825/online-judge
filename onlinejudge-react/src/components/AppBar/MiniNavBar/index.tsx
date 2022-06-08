import {FC, ReactElement, useState} from 'react';
import {Button, Col, Drawer, Menu, Row} from "antd";
import './mini_navbar.less'
import {MenuUnfoldOutlined} from "@ant-design/icons";
import UserStatus from "../UserStatus";
import {Link} from "react-router-dom";
import {useCurrentPathObj} from "../../../utils/hooks";

const MenuItem = Menu.Item
interface IProps{
    pathArr:Array<{name:string,path:string}>
}



const MiniNavBar:FC<IProps> = (props):ReactElement => {
    const [visible, setVisible] = useState(false);

    const showDrawer = () => {
        setVisible(true);
    };
    const onClose = () => {
        setVisible(false);
    };
    let currentPathObj = useCurrentPathObj(props.pathArr)
    const selectPath:Array<any> = [currentPathObj?.path]
    const title = currentPathObj?.name
    return (
        <div className={'mini-navbar'} >
            <Row align={'middle'}>
                <Col span={3}>
                    <div style={{float:'left',width:'fit-content',height:'fit-content'}}>
                        <Button style={{padding:'0'}} type="text" onClick={showDrawer}>
                            <MenuUnfoldOutlined style={{color:'#fcaf17',fontSize:'1.5em'}} />
                        </Button>
                        <Drawer width={225} title={<div className={'drawer-header'}>CrowOj</div>} placement="left" onClose={onClose} visible={visible}>
                            <Menu
                                className={'menu'}
                                mode="inline"
                                defaultSelectedKeys={selectPath}
                            >
                                {props.pathArr.map(value => <MenuItem key={value.path}><Link to={value.path}>{value.name}</Link></MenuItem>)}
                            </Menu>
                        </Drawer>
                    </div>
                </Col>
                <Col span={18}>
                    <div className={'logo'}>{title}</div>
                </Col>
                <Col span={3} >
                    <div style={{float:'right'}}>
                        <UserStatus/>
                    </div>
                </Col>
            </Row>
        </div>
    );
};

export default MiniNavBar;
