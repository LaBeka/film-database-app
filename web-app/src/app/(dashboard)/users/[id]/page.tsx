"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { userSchema, UserRequestDto } from "@/types/userSchema";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react"
import {UserResponseDto} from "@/types/types";
import api from "@/lib/api";
import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from "@/components/ui/form";
import {
    Field,
    FieldDescription,
    FieldGroup,
    FieldLabel,
    FieldLegend,
    FieldSet,
} from "@/components/ui/field"

import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";
import {AxiosError} from "axios";


interface BackendErrorResponse {
    status: number;
    message: string;
}


export default function UserDetailPage() {
    const router = useRouter();
    const params = useParams()
    const id = params.id // Captures the '1' from /users/1
    const [user, setUser] = useState<UserResponseDto>()

    const form = useForm({
        resolver: zodResolver(userSchema),
        defaultValues: {
            userName: "",
            fullName: "",
            email: "",
            password: "",
            age: 0
        }
    });

    useEffect(() => {
        api.get(`/user/get/id/${id}`)
            .then(res => {
                setUser(res.data);
                form.reset({
                    userName: res.data.userName,
                    fullName: res.data.fullName,
                    email: res.data.email,
                    age: res.data.age,
                    password: ""
                });
            })
            .catch(err => {
                const status = err.response?.status;
                const message = err.response?.message;
                console.error("Access Denied with message: ", message, " and status: ", status);
            })
    }, [id])
    console.log(user?.roles)


    async function onSubmit(values: UserRequestDto) {
        try {
            // Calls your @PostMapping("/api/user/create")
            await api.post("/api/user/update/{email}", values);
            router.push("/login"); // Redirect to login after success
        } catch (error: unknown) {
            // 2. Check if this is an Axios Error
            const axiosError = error as AxiosError<BackendErrorResponse>;

            if (axiosError.response && axiosError.response.data) {
                // Now TypeScript knows 'data' has 'message' and 'status'
                console.error("Backend Status:", axiosError.response.data.status);
                console.error("Backend Message:", axiosError.response.data.message);

                alert(`Error: ${axiosError.response.data.message}`);
            } else if (error instanceof Error) {
                // 3. Fallback for generic JS errors (like network failure)
                console.error("Network/Generic Error:", error.message);
            }
        }
    }

    return (
        /* 1. Entire Page Filling: w-full and p-4/p-8 */
        <div className="w-full min-h-screen p-4 md:p-8 lg:p-12 bg-slate-50">
            <FieldSet className="bg-white p-6 rounded-xl shadow-sm border">
                <FieldLegend className="text-xl font-semibold">User Information</FieldLegend>
                <FieldDescription>Update account details for user ID: {user?.fullName}</FieldDescription>

                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)} className="mt-8">

                        {/* 2 & 3. Grid Logic: 1 col on mobile/md, 2 cols on lg */}
                        <div className="grid grid-cols-1 lg:grid-cols-2 gap-x-8 gap-y-6">

                            {/* Username */}
                            <FormField control={form.control} name="userName" render={({ field }) => (
                                <FormItem>
                                    <FieldLabel>Username</FieldLabel>
                                    <FormControl>
                                        {/* 4. Shrink on small screen: text-sm and h-9 */}
                                        <Input className="text-sm sm:text-base h-9 sm:h-10" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )} />

                            {/* Full Name */}
                            <FormField control={form.control} name="fullName" render={({ field }) => (
                                <FormItem>
                                    <FieldLabel>Full Name</FieldLabel>
                                    <FormControl>
                                        <Input className="text-sm sm:text-base h-9 sm:h-10" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )} />

                            {/* Email */}
                            <FormField control={form.control} name="email" render={({ field }) => (
                                <FormItem>
                                    <FieldLabel>Email</FieldLabel>
                                    <FormControl>
                                        <Input type="email" className="text-sm sm:text-base h-9 sm:h-10" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )} />

                            {/* Age */}
                            <FormField control={form.control} name="age" render={({ field }) => (
                                <FormItem>
                                    <FieldLabel>Age</FieldLabel>
                                    <FormControl>
                                        <Input type="number" className="text-sm sm:text-base h-9 sm:h-10" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )} />
                        </div>

                        {/* Button section */}
                        <div className="flex flex-col sm:flex-row sm:justify-end gap-3 mt-10 border-t pt-6">
                            <Button
                                type="button"
                                variant="outline"
                                onClick={() => router.back()}
                                className="w-full sm:w-32 h-9 sm:h-10 text-xs sm:text-sm"
                            >
                                Cancel
                            </Button>

                            <Button
                                type="submit"
                                className="w-full sm:w-32 h-9 sm:h-10 text-xs sm:text-sm"
                            >
                                Submit
                            </Button>
                        </div>
                    </form>
                </Form>
            </FieldSet>
        </div>
    );
}