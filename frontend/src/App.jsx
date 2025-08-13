import React from 'react';
import Upload from './Upload';
import FileList from './FileList';
import 'bootstrap/dist/css/bootstrap.css';

export default function App() {
    return (
        <>
            <nav className="navbar bg-body-tertiary">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">
                        OpenShare
                    </a>
                </div>
            </nav>
            <div className='container' style={{ maxWidth: 800, margin: '40px auto', fontFamily: 'Arial, sans-serif' }}>
                <h3 className='mb-4'>
                    <b>Simple File Share across multiple devices</b>
                </h3>
                <Upload />
                <hr />
                <FileList />
            </div>
        </>
    );
}
