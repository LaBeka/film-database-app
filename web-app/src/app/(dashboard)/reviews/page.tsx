"use client";

import { useEffect, useState } from "react"
import api from "@/lib/api"
import { FilmReviewResponseDto } from "@/types/types"
import { ReviewResponseDto } from "@/types/types"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

export default function ReviewsPage() {
    const [filmId, reviews] = useState<FilmReviewResponseDto[]>([])
    console.log('ReviewsPage()');

    useEffect(() => {
        api.get(`/review/public/getByFilm/1`)
            .then(res => filmId(res.filmId))
            .then(res => reviews(res.reviews))
            .then(res => console.log(res.reviews))
            .catch(err => {
                const status = err.response?.status;
                const message = err.response?.message;
                console.error("Access Denied with message: ", message, " and status: ", status);
            })
    }, [])

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Review Directory</h1>
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead>Index</TableHead>
                        <TableHead>userName</TableHead>
                        <TableHead>text</TableHead>
                        <TableHead>date</TableHead>
                        <TableHead>score</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {filmId.map((review) => (
                        <TableRow key={review.index}>
                            <TableCell>{review.userName}</TableCell>
                            <TableCell>{review.text}</TableCell>
                            <TableCell>{review.date}</TableCell>
                            <TableCell>{review.score}</TableCell>
                            <TableCell>
                                <a href={`/users/${user.id}`} className="accent-red-400 underline">View</a>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    )
}