import React, {FC, ReactElement} from 'react';
import AppBar from "../AppBar";
import {Layout} from "antd";
import {observer} from "mobx-react-lite";
import StoreContextProvider from "../../stores/store.context.provider";
import AppFooter from "../AppFooter";
import "./index.less"
import RouterBeforeEach from "../../route/RouterBeforeEach";
import Particles from "reactparticles.js";
const {Header,Content,Footer} = Layout

interface IProps{
    children?:any
}

const Wrap:FC<IProps> = (props):ReactElement => {
    return (
        <StoreContextProvider >

            <div className={"wrap"}>
                <Layout style={{minWidth:'280px'}}>
                    <Header style={{backgroundColor:'#232423',position: 'fixed', zIndex: 999, width: '100%'}}>
                        <AppBar/>
                    </Header>
                    <Content style={{minHeight:'calc(100vh - 64px - 92px)',padding: '0 3vw', marginTop: 64,backgroundColor:'#EEEEEE' }}>

                        {props.children == null ? <RouterBeforeEach />:props.children}
                    </Content>
                    <Footer style={{backgroundColor:'#232423'}}><AppFooter /></Footer>
                </Layout>
            </div>
        </StoreContextProvider>
    )

};
export default observer(Wrap);
