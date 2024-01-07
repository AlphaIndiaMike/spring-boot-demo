import { createContext,
useContext,
useEffect,
useState } from "react";
import {login as performLogin} from "../../services/client.js"
import {jwtDecode} from 'jwt-decode'

const AuthContext = createContext({});

const AuthProvider = ({ children }) => {
    const [customer, setCustomer] = useState(null);

    const setCustomerFromToken = () => {
        let token = localStorage.getItem("access_token");
        if (token) {
            token = jwtDecode(token);
            setCustomer({
                username: token.sub,
                roles: token.scopes,
                uid: token.uid
            })
        }
    }

    useEffect(() => {
        setCustomerFromToken();
    }, [])

    const login = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword).then(res => {
                const jwtToken = res.headers["authorization"];
                // save the token 
                localStorage.setItem("access_token", jwtToken);
                
                const decodedToken = jwtDecode(jwtToken);
                console.log('----- decoded Token -----');
                console.log(decodedToken);

                setCustomer({
                    username: decodedToken.sub,
                    roles: decodedToken.scopes,
                    uid: decodedToken.uid
                })
                resolve();
            }).catch(err => {
                reject(err);
            })
        })
    }

    const logout = () => {
        localStorage.removeItem("access_token");
        setCustomer(null);
    }

    const isCustomerAuthenticated = () => {
        const token = localStorage.getItem("access_token");
        if (!token ){
            return false;
        }
        
        const {exp: expiration} = jwtDecode(token);
        if (Date.now() > expiration * 1000) {
            logout();
            return false;
        }

        return true;
    }

    return (
        <AuthContext.Provider value = {{
            customer,
            login,
            logout,
            isCustomerAuthenticated,
            setCustomerFromToken
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;