import React, { useState } from 'react';

export default function Upload() {
    const [file, setFile] = useState(null);
    const [status, setStatus] = useState('');

    const onSubmit = async (e) => {
        e.preventDefault();
        if (!file) return;
        setStatus('Uploading...');
        const form = new FormData();
        form.append('file', file);
        try {
            const res = await fetch('http://localhost:8084/api/files', { method: 'POST', body: form });
            if (!res.ok) throw new Error('Upload failed');
            setStatus('Uploaded successfully');
        } catch (err) {
            setStatus('Upload error');
            console.error(err);
        }
    };

    return (
        <form className='mb-3' onSubmit={onSubmit} style={{ display: 'flex', gap: 12, alignItems: 'center' }}>
            <input type="file" onChange={e => setFile(e.target.files[0])} />
            <button type="submit">Upload</button>
            <span>{status}</span>
        </form>
    );
}
