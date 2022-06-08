import {FC, ReactElement} from 'react';
import RouterBeforeEach from "../../route/RouterBeforeEach";
interface IProps{

}
const ProblemSet:FC<IProps> = (props):ReactElement => {
    document.title = 'CrowOj - 题库'
    return (
        <RouterBeforeEach />
    );
};

export default ProblemSet;
