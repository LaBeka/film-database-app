"use client";

import { useEffect, useState } from "react"
import api from "@/lib/api"
import { UserResponseDto } from "@/types/types"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"


export default function UsersPage() {
    const [users, setUsers] = useState<UserResponseDto[]>([])

    useEffect(() => {
        api.get("/user/get/list")
            .then(res => setUsers(res.data))
            .catch(err => {
                const status = err.response?.status;
                const message = err.response?.message;
                console.error("Access Denied with message: ", message, " and status: ", status);
            })
    }, [])

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">User Directory</h1>
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead>Username</TableHead>
                        <TableHead>Email</TableHead>
                        <TableHead>Action</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {users.map((user) => (
                        <TableRow key={user.id}>
                            <TableCell>{user.userName}</TableCell>
                            <TableCell>{user.email}</TableCell>
                            <TableCell>
                                <a href={`/users/${user.id}`} className="text-blue-500 underline">View</a>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    )
}