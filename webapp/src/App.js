import './App.css';
import React, {useState} from "react";
import Main from './components/Main';
import LoadingContext from './contexts/LoadingContext';
import SnackbarContext from './contexts/SnackbarContext';
import Loading from "./components/LoadingComponent";
import Login from "./components/LoginComponent";
import { Snackbar } from "@material-ui/core";
import Alert from "./components/AlertComponent";
import LoginContext from './contexts/LoginContex';


function App() {
  const [loading, setLoading] = useState(true);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    type: 'success'
  });
  const [token, setToken] = useState("");

  const valueLoading = { loading, setLoading };
  const valueSnackbar = { snackbar, setSnackbar };
  const valueLogin = {token, setToken};

  const snackbarClose = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    setSnackbar({
      ...snackbar,
      open: false
    });
  };

  return (
    <div className="App">
      <LoginContext.Provider value ={valueLogin}>
        <LoginContext.Consumer>
          {({token}) =>{
            if(token===""){
              return <Login/>
            } else {
              return(
                <LoadingContext.Provider value={valueLoading}>
                  <SnackbarContext.Provider value={valueSnackbar}>
                      <Main/>
                      <LoadingContext.Consumer>
                        {({loading}) => {
                          if(loading){
                            return <Loading />;
                          } else {
                            return <div> </div>;
                          }
                        }}
                      </LoadingContext.Consumer>
                      <SnackbarContext.Consumer>
                        {({snackbar}) => {
                          return (
                            <Snackbar open={snackbar.open} onClose={snackbarClose} autoHideDuration={5000}>
                              <Alert severity={snackbar.type === 'success' ? 'success': 'error'} 
                                onClose={snackbarClose}>
                                {snackbar.message}
                              </Alert>
                            </Snackbar>
                          );
                        }}
                  </SnackbarContext.Consumer>
                  </SnackbarContext.Provider>
                  </LoadingContext.Provider>
              )
            }
          }}
        </LoginContext.Consumer>
      </LoginContext.Provider>        
    </div>
  );
}

export default App;
