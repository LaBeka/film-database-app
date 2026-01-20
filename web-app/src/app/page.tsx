"use client"

import { useEffect, useState } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";

export default function Home() {
    const [isLoggedIn, setIsLoggedIn] = useState(() => {
        if (typeof window !== "undefined") {
            return !!localStorage.getItem("token")
        }
        return false
    })

    useEffect(() => {
        const checkToken = () => setIsLoggedIn(!!localStorage.getItem("token"))
        window.addEventListener("storage", checkToken)

        return () => window.removeEventListener("storage", checkToken)
    }, [])

    const handleLogout = () => {
        localStorage.removeItem("token"); // Removes the JWT
        window.dispatchEvent(new Event("storage")); // Notifies other components like the Sidebar
        setIsLoggedIn(false);
        // Using window.location forces a fresh state, but router.push works too
        window.location.href = "/";
    }

    return (
        <main className="flex min-h-screen flex-col items-center justify-center p-24">
            <h1 className="text-4xl font-bold mb-8">Film Database App</h1>

            {isLoggedIn ? (
                <div className="text-center space-y-4">
                    <h2 className="text-2xl text-green-600 font-semibold">Welcome back!</h2>
                    <p className="text-gray-600">You are logged in.</p>
                    {/* ADDED LOGOUT BUTTON HERE */}
                    <Button variant="destructive" onClick={handleLogout}>
                        Logout from Session
                    </Button>
                </div>
            ) : (
                <div className="text-center space-y-6">
                    <p className="text-xl text-gray-600">Please login to continue.</p>
                    <div className="flex gap-4 justify-center">
                        <Link href="/login"><Button size="lg">Login</Button></Link>
                        <Link href="/register"><Button variant="outline" size="lg">Register</Button></Link>
                    </div>
                </div>
            )}
        </main>
    );
}