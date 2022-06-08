import {FC, ReactElement} from 'react';

interface IProps{

}

const Article:FC<IProps> = (props):ReactElement => {
    document.title = 'CrowOj - 文章'
    return (
        <div>
            这是文章页
        </div>
    );
};

export default Article;
