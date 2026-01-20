"use client";

import { useEffect, useState } from "react"
import api from "@/lib/api"
import {FilmResponseDto} from "@/types/types"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import Link from "next/link";


export default function UsersPage() {
    const [films, setFilms] = useState<FilmResponseDto[]>([])

    useEffect(() => {
        api.get("/test/films")
            .then(res => setFilms(res.data))
            .catch(err => {
                const status = err.response?.status;
                const message = err.response?.message;
                console.error("Access Denied with message: ", message, " and status: ", status);
            })
    }, [])

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Film Directory</h1>
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead>Title</TableHead>
                        <TableHead>genre</TableHead>
                        <TableHead>casta</TableHead>
                        <TableHead>ageRestriction</TableHead>
                        <TableHead>awards</TableHead>
                        <TableHead>languages</TableHead>
                        <TableHead>aspectRatio</TableHead>
                        <TableHead>colorStatus</TableHead>
                        <TableHead>cameraUsed</TableHead>
                        <TableHead>Reviews</TableHead>
                        <TableHead>Details</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {films.map((film) => (
                        <TableRow key={film.id}>
                            <TableCell>{film.title}</TableCell>
                            <TableCell>{film.genre}</TableCell>
                            <TableCell>{film.casta}</TableCell>
                            <TableCell>{film.ageRestriction}</TableCell>
                            <TableCell>{film.awards}</TableCell>
                            <TableCell>{film.langusges}</TableCell>
                            <TableCell>{film.aspectRatio}</TableCell>
                            <TableCell>{film.colorStatus}</TableCell>
                            <TableCell>{film.cameraUsed}</TableCell>
                            <TableCell>
                                {/* Use ?filmId=ID so the reviews page knows which films to filter */}
                                <Link
                                    href={`/reviews?filmId=${film.title}`}
                                    className="text-blue-600 hover:underline font-medium"
                                >
                                    View Reviews
                                </Link>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    )
}