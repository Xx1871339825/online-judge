import {createContext, FC, ReactElement, useContext} from 'react';
import stores from "./stores";

interface IProps{

}


const StoreContext = createContext(stores)

const StoreContextProvider:FC<IProps> = (props):ReactElement => {
    return (
        <StoreContext.Provider value={stores}>
            {props.children}
        </StoreContext.Provider>
    );
};

export const useStore = () => {
    return useContext(StoreContext)
}

export default StoreContextProvider;
