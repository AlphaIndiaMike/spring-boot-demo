import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import { ChakraProvider } from '@chakra-ui/react'
import { createStandaloneToast } from '@chakra-ui/react'
//react router
import {createBrowserRouter, RouterProvider} from "react-router-dom"
//page components
import SimpleSignIn from './components/login/SimpleSignIn.jsx'
import AuthProvider from "./components/context/AuthContext.jsx"
import ProtectedRoutes from './components/protected/ProtectedRoutes.jsx'
import Signup from './components/signup/signup.jsx'

const { ToastContainer } = createStandaloneToast();

const router = createBrowserRouter([
  {
    path: "/",
    element: <SimpleSignIn/>
  },
  {
    path: "/signup",
    element: <Signup></Signup>
  },
  {
    path: "dashboard",
    element: <ProtectedRoutes>
              <App />
            </ProtectedRoutes>
  }

])

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
     <ChakraProvider>
          <AuthProvider>
            <RouterProvider router={router} />
          </AuthProvider>
          <ToastContainer/>
    </ChakraProvider>
  </React.StrictMode>,
)
