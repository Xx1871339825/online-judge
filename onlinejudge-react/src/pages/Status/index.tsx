import {FC, ReactElement} from 'react';
import RouterBeforeEach from "../../route/RouterBeforeEach";

interface IProps{

}

const Status:FC<IProps> = (props):ReactElement => {
    document.title = 'CrowOj - 提交状态'
    return (
        <div>
            <RouterBeforeEach />
        </div>
    );
};

export default Status;
