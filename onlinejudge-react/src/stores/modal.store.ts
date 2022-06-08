import {makeAutoObservable} from "mobx";

export default class ModalStore{
    get msg(): string {
        return this._msg;
    }

    set msg(value: string) {
        this._msg = value;
    }
    constructor() {
        makeAutoObservable(this)
    }
    private _visible = false
    private _msg = ''
    get visible(): boolean {
        return this._visible;
    }

    set visible(value: boolean) {
        this._visible = value;
    }
}
