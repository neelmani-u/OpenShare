import React, { useEffect, useState, Suspense } from "react";
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { getSessionResource } from "./resource/SessionResource";
import Login from './pages/Login'
import Register from './pages/Register'
import Home from './pages/Home'
import FolderView from './pages/FolderView'
import axios from 'axios'
import 'bootstrap/dist/css/bootstrap.css';

axios.defaults.withCredentials = true
axios.defaults.baseURL = '/'

function App() {
    // const [user, setUser] = useState('689c3e268cf00a5d789835ac')
    // const [user, setUser] = useState('689c42708cf00a5d789835b1')

    const session = getSessionResource();

    // useEffect(() => {
    //     axios.get("http://localhost:8084/api/auth/check-session", { withCredentials: true })
    //         .then(res => {
    //             // console.log(res);
    //             // console.log(res.data.loggedIn);
    //             if (res.data.loggedIn) {
    //                 // console.log("logged in");
    //                 setUser(res.data.username);
    //                 console.log(user);
    //                 // window.location.replace("/");
    //             }
    //         });
    // }, [])

    // if (session.loggedIn) {
    //     return <div>Loading...</div>;
    // }


    return (
        <BrowserRouter>
            <nav className="navbar bg-body-tertiary">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">
                        OpenShare
                    </a>
                    
                    {session.loggedIn ? <p className="float-right">
                        <b>Username:</b> {session.username}
                    </p> : ''}
                </div>
            </nav>
 
            <div className="container py-4">
                <Routes>
                    <Route path="/login" element={session.loggedIn ? <Navigate to='/' /> : <Login onLogin={() => window.location.href = '/'} />} />
                    <Route path="/register" element={session.loggedIn ? <Navigate to='/' /> : <Register onRegister={() => window.location.href = '/login'} />} />
                    <Route path="/" element={session.loggedIn ? <Home /> : <Navigate to='/login' />} />
                    <Route path="/folders/:id" element={session.loggedIn ? <FolderView /> : <Navigate to='/login' />} />
                </Routes>
            </div>
        </BrowserRouter>
    )
}

createRoot(document.getElementById('root')).render(<App />)
