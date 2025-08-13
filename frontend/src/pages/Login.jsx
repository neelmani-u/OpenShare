import React, { useState } from 'react'
import axios from 'axios'

export default function Login({ onLogin }) {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState(null)

    const submit = async (e) => {
        e.preventDefault();

        if (username.includes(" ")) {
            setError("Username cannot contains spaces!");
            return;
        }

        if (!isNaN(username.trim().charAt(0))) {
            setError("Username cannot start with number!");
            return;
        }

        if (password.includes(" ")) {
            setError("Password cannot contains spaces!");
            return;
        }

        try {
            // await axios.post('http://localhost:8084/api/auth/login', { username, password })
            await axios.post("http://localhost:8084/api/auth/login",
                { username, password },
                { withCredentials: true }
            ).then(res => {
                // console.log(res.data);
                // navigate("/"); 
                onLogin && onLogin();
            }).catch(err => {
                setError("Invalid username or password!");
            });
        } catch (err) {
            setError(err.response?.data || 'Login failed')
        }
    }

    return (
        <div className="card p-4" style={{ maxWidth: 480, margin: '40px auto' }}>
            <h3>Login</h3>
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={submit}>
                <input className="form-control my-2" placeholder="username" value={username} onChange={e => setUsername(e.target.value)} required />
                <input className="form-control my-2" placeholder="password" type="password" value={password} onChange={e => setPassword(e.target.value)} required />
                <div className="d-flex gap-2">
                    <button className="btn btn-primary" type="submit">Login</button>
                    <a className="btn btn-link" href="/register">Register</a>
                </div>
            </form>
        </div>
    )
}

