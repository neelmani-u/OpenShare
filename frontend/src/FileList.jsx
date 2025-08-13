import React, { useEffect, useState } from 'react';

export default function FileList() {
    const [files, setFiles] = useState([]);
    const load = async () => {
        try {
            const res = await fetch('http://localhost:8084/api/files');
            if (!res.ok) throw new Error('List failed');
            const json = await res.json();
            setFiles(json);
        } catch (e) {
            console.error(e);
        }
    };
    useEffect(() => { load(); }, []);
    return (
        <div>
            <h2>Files</h2>
            <button onClick={load}>Refresh</button>
            <ul>
                {files.map(f => (
                    <li key={f.id} style={{ display: 'flex', gap: 12, alignItems: 'center' }}>
                        <span>{f.filename} ({Math.round(f.length / 1024)} KB)</span>
                        <a href={`/api/files/${f.id}/download`} target="_self" rel="noreferrer">Download</a>
                    </li>
                ))}
            </ul>
        </div>
    );
}
