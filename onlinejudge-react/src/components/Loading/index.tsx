import {FC, ReactElement} from 'react';
import './index.less'
import {Spin} from "antd";

interface IProps{

}


const Loading:FC<IProps> = (props):ReactElement => {
    return (
        <div className={'my-loading'}>
            <div className={'spin-wrap'}>
                <Spin size={'large'} />
            </div>
        </div>
    );
};

export default Loading;
