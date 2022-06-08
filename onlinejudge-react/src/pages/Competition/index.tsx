import {FC, ReactElement} from 'react';
import RouterBeforeEach from "../../route/RouterBeforeEach";

interface IProps{

}

const Competition:FC<IProps> = (props):ReactElement => {
    document.title = 'CrowOj - 竞赛'
    return (
        <div className={'competition'}>
            <RouterBeforeEach />
        </div>
    );
};

export default Competition;
