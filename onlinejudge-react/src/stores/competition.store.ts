import {makeAutoObservable} from "mobx";

interface ICompetition{
    cid:number,
    auth:number,
    valid:boolean
}

export default class CompetitionStore{

    constructor() {
        makeAutoObservable(this)
    }

    get competitionList(): Array<ICompetition> {
        return this._competitionList;
    }

    set competitionList(value: Array<ICompetition>) {
        this._competitionList = value;
    }

    private _competitionList:Array<ICompetition> = []
}
