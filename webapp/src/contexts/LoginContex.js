import React from "react"

const LoginContext = React.createContext({
    token: "",
    setToken: () => {}
});

export default LoginContext;