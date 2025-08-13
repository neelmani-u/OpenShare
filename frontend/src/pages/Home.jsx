import React, { useState, useEffect } from 'react'
import axios from 'axios'

export default function Home() {
    const [mine, setMine] = useState([])
    const [shared, setShared] = useState([])
    const [name, setName] = useState('')

    const load = async () => {
        const r = await axios.get('http://localhost:8084/api/folders', { withCredentials: true })
        setMine(r.data.mine || [])
        setShared(r.data.shared || [])
    }

    useEffect(() => { load() }, [])

    const checkDuplicates = (newName) => {
        let status = false;
        mine.map(f => {
            if (f.name == newName) {
                status = true;
                return;
            }
        });
        return status;
    }

    const create = async () => {
        console.log(shared);
        if (!name) return alert('Enter name');
        if (name.includes(" ")) return alert('Name cannot have spaces!');
        if (checkDuplicates(name)) return alert('Folder with same name already Exists!');
        await axios.post('http://localhost:8084/api/folders', { name, withCredentials: true })
        setName(''); load()
    }

    const remove = async (id) => {
        if (!confirm('Delete folder?')) return
        await axios.delete('http://localhost:8084/api/folders/' + id, { withCredentials: true }); load()
    }

    const rename = async (f) => {
        const n = prompt('New name', f.name); if (!n) return
        await axios.put('http://localhost:8084/api/folders/' + f.id + '/rename', { name: n, withCredentials: true }); load()
    }

    const logout = async () => {
        if (!confirm('Want to logout?')) return
        await axios.post('http://localhost:8084/api/auth/logout', { withCredentials: true });
        window.location.href = '/login';
    }

    return (
        <div>
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2>Folders</h2>
                <div>
                    <button className="btn btn-secondary me-2" onClick={logout}>Logout</button>
                </div>
            </div>

            <div className="card p-3 mb-3">
                <div className="d-flex gap-2">
                    <input className="form-control" placeholder="New folder name" value={name} onChange={e => setName(e.target.value)} required />
                    <button className="btn btn-primary" onClick={create}>Create</button>
                </div>
            </div>

            <h4>My Folders</h4>
            <div className="list-group mb-4">
                {mine.map(f => (
                    <div key={f.id} className="list-group-item d-flex justify-content-between align-items-center">
                        <div>
                            <a href={'/folders/' + f.id}>{f.name}</a>
                        </div>
                        <div>
                            <button className="btn btn-sm btn-outline-primary me-1" onClick={() => rename(f)}>Rename</button>
                            <button className="btn btn-sm btn-outline-danger" onClick={() => remove(f.id)}>Delete</button>
                        </div>
                    </div>
                ))}
            </div>

            <h4>Shared With Me</h4>
            <div className="list-group">
                {shared.map(f => (
                    <div key={f.id} className="list-group-item d-flex justify-content-between align-items-center">
                        <div>
                            <span><b>{f.ownerName}: </b></span>
                            <a href={'/folders/' + f.id}>{f.name}</a>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}
