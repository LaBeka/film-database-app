"use client";

import { useEffect, useState } from "react"
import api from "@/lib/api"
import { FilmReviewResponseDto } from "@/types/types"
import { ReviewResponseDto } from "@/types/types"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { useSearchParams } from "next/navigation";

export default function ReviewsPage() {
    const [filmId, reviews] = useState<FilmReviewResponseDto[]>([])
    const params = useSearchParams()
    const id = params.get("filmId")

    useEffect(() => {
        api.get(`/review/public/getByFilm/${id}`)
            .then(res => reviews(res.data.reviews))
            .then(res => console.log(res.filmId))
            .catch(err => {
                console.log(err.response?.status);
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
                            <TableCell>{review.index}</TableCell>
                            <TableCell>{review.userName}</TableCell>
                            <TableCell>{review.text}</TableCell>
                            <TableCell>{review.date}</TableCell>
                            <TableCell>{review.score}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    )
}