import {FC, ReactElement} from 'react';
import './index.less'
import {CopyrightOutlined} from "@ant-design/icons";

interface IProps{

}

const AppFooter:FC<IProps> = (props):ReactElement => {
    return (
        <>
            <div className={'copy-right'}>
                Copyright <CopyrightOutlined />2021 - future Crow
            </div>
            <div className={"copy-right"}>
                桂ICP备xxxxxxxxxx号-1
            </div>
        </>
    );
};

export default AppFooter;
