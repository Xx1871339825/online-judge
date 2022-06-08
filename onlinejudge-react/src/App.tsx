import React from 'react'
import './app.less'
import {useRoutes} from "react-router-dom";
import {routes} from "./route/router";


function App() {

  return (
    <div className="App">
        {useRoutes(routes)}
    </div>
  );
}

export default App;
