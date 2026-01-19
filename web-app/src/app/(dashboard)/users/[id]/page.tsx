"use client"
import { useParams } from "next/navigation"

export default function UserDetailPage() {
    const params = useParams()
    const id = params.id // Captures the '1' from /users/1

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold">User Details for ID: {id}</h1>
            {/* Call api.get(`/user/get/id/${id}`) here */}
        </div>
    )
}