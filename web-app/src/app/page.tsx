"use client"

import {useEffect, useState} from "react";

export default function Home() {
    const [message, setMessage] = useState("Loading...");

    useEffect(() => {
        // Note: We call /api/... NOT http://localhost:8080/api/...
        fetch('/api/test/greeting')
            .then((res) => res.text())
            .then((data) => setMessage(data))
            .catch((err) => setMessage("Error connecting to backend. " + err.getMessage()));
    }, []);
    console.log(message);
    console.log(message);
    console.log(message);


    return (
        <main className="flex min-h-screen flex-col items-center justify-center p-24">
            <h1 className="text-4xl font-bold">Backend Message:</h1>
            <p className="mt-4 text-xl text-blue-500">{message}</p>
            <p className="mt-4 text-xl text-blue-500">{message}</p>
        </main>
    );
}
