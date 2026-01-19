"use client"

import { useEffect, useState } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button"; // Assuming you added shadcn button

export default function Home() {
    const [message, setMessage] = useState("Loading...");

    const TEST_PATH = "http://localhost:8080/api/test";

    useEffect(() => {
        fetch(`${TEST_PATH}/`)
            .then((res) => res.text())
            .then((data) => setMessage(data))
            .catch((err) => setMessage("Error connecting to backend. " + err.getMessage()));
    }, []);

    return (
        <main className="flex min-h-screen flex-col items-center justify-center p-24 bg-slate-50">
            <div className="z-10 max-w-5xl w-full items-center justify-center font-mono text-sm flex flex-col gap-8">
                <h1 className="text-6xl font-bold text-slate-900 text-center">
                    Final project: Film App
                </h1>

                <p className="text-xl text-slate-600 text-center max-w-2xl">
                    Welcome! Our backend says: <br />
                    <span className="font-bold text-blue-600">{message}</span>
                </p>

                <div className="flex gap-4 mt-8">
                    <Link href="/login">
                        <Button size="lg" className="w-32">Login</Button>
                    </Link>
                    <Link href="/register">
                        <Button variant="outline" size="lg" className="w-32">Register</Button>
                    </Link>
                </div>
            </div>
        </main>
    );
}