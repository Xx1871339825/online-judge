import {FC, ReactElement} from 'react';
import Editor from 'md-editor-rt';
import 'md-editor-rt/lib/style.css';
interface IProps{
    settingId:number,
    createTime:string,
    updateTime:string|null,
    nickname:string,
    status:number,
    title:string,
    userId:number,
    content:string
}

const Announcement:FC<IProps> = (props):ReactElement => {
    return (
        <div>
            <Editor
                modelValue={props.content}
                previewOnly
            />
        </div>
    );
};

export default Announcement;
