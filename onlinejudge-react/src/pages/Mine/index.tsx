import {FC, ReactElement} from 'react';
import RouterBeforeEach from "../../route/RouterBeforeEach";

interface IProps{

}

const Mine:FC<IProps> = (props):ReactElement => {
    document.title = 'CrowOj - 个人信息'
    return (
        <div>
            <RouterBeforeEach />
        </div>
    );
};

export default Mine;
