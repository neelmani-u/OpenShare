import React, { useState, useEffect } from 'react'
import axios from 'axios'
import { useParams } from 'react-router-dom'

export default function FolderView() {
    const { id } = useParams()
    const [files, setFiles] = useState([])
    const [file, setFile] = useState(null)
    const [uploading, setUploading] = useState(false)
    const [shareWith, setShareWith] = useState('')

    const load = async () => {
        try {
            const r = await axios.get('http://localhost:8084/api/files/by-folder/' + id); setFiles(r.data);
            // console.log(r);
        } catch(err) {
            // console.log(err);
            alert(err.response.data);
            window.location.replace("/");
        }
    }
    useEffect(() => { load() }, [id])

    const upload = async (e) => {
        e.preventDefault(); if (!file) return
        setUploading(true)
        const form = new FormData(); form.append('file', file)
        await axios.post('http://localhost:8084/api/files/' + id, form, { headers: { 'Content-Type': 'multipart/form-data' } })
        setFile(null); setUploading(false); load()
    }

    const remove = async (fid) => { if (!confirm('Delete file?')) return; await axios.delete('http://localhost:8084/api/files/' + fid); load() }
    const rename = async (f) => { const n = prompt('New name', f.filename); if (!n) return; await axios.put('http://localhost:8084/api/files/' + f.id + '/rename', { name: n }); load() }

    const share = async () => {
        if (!shareWith) return alert('Enter user name to share with (for demo)');
        if (shareWith.includes(" ")) return alert('Please Enter valid username!');

        try {
            await axios.post('http://localhost:8084/api/folders/' + id + '/share', { username: shareWith }); alert('Shared'); setShareWith('')
        } catch(err) {
            // console.log(err);
            alert(err.response.data);
        }
    }

    return (
        <div>
            <a href="/" className="btn btn-link">&larr; Back</a>
            <h3>Files</h3>
            <div className="card p-3 mb-3">
                <form onSubmit={upload} style={{ display: 'flex', gap: 12, alignItems: 'center' }}>
                    <input type="file" className="form-control" onChange={e => setFile(e.target.files[0])} />
                    <button className="btn btn-primary" type="submit">{uploading ? 'Uploading...' : 'Upload'}</button>
                </form>
                <div className="mt-2 d-flex gap-2">
                    <input className="form-control" placeholder="User name to share with" value={shareWith} onChange={e => setShareWith(e.target.value)} />
                    <button className="btn btn-outline-secondary" onClick={share}>Share Folder</button>
                </div>
            </div>

            <ul className="list-group">
                {files.map(f => (
                    <li key={f.id} className="list-group-item d-flex justify-content-between align-items-center">
                        <div><a href={'/api/files/' + f.id + '/download'}>{f.filename}</a></div>
                        <div>
                            <button className="btn btn-sm btn-outline-primary me-1" onClick={() => rename(f)}>Rename</button>
                            <button className="btn btn-sm btn-outline-danger" onClick={() => remove(f.id)}>Delete</button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    )
}
