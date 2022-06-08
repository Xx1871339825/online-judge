import {FC, ReactElement, useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {getCompetitionDesc} from "../../../../axios/network/competition";
import Editor from 'md-editor-rt';
import 'md-editor-rt/lib/style.css';
interface IProps{

}

const Description:FC<IProps> = (props):ReactElement => {
    const {id} = useParams()
    const [text,setText] = useState('')
    useEffect(()=>{
        getCompetitionDesc(id).then(res => {
            setText(res.data.data)
        })
    },[])
    return (
        <>
            <Editor
                modelValue={text}
                previewOnly
            />
        </>
    );
};

export default Description;
