"use client";
import { AxiosError } from "axios";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { userSchema, UserRequestDto } from "@/types/userSchema";
import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import api from "@/lib/api";
import { useRouter } from "next/navigation";

export default function RegisterPage() {
    const router = useRouter();
    const form = useForm({
        resolver: zodResolver(userSchema),
        defaultValues: {
            userName: "",
            fullName: "",
            email: "",
            password: "",
            age: 18 // This matches the schema's number type
        }
    });

    interface BackendErrorResponse {
        status: number;
        message: string;
    }

    async function onSubmit(values: UserRequestDto) {
        try {
            // Calls your @PostMapping("/api/user/create")
            await api.post("/user/createUser", values);
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
        <div className="flex justify-center items-center min-h-screen p-4">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4 w-full max-w-md p-6 border rounded-lg shadow-md bg-white">
                    <h2 className="text-2xl font-bold mb-4 text-center">Create Account</h2>

                    {/* 1. Username Field */}
                    <FormField control={form.control} name="userName" render={({ field }) => (
                        <FormItem>
                            <FormLabel>Username</FormLabel>
                            <FormControl><Input placeholder="jdoe123" {...field} /></FormControl>
                            <FormMessage />
                        </FormItem>
                    )} />

                    {/* 2. Full Name Field */}
                    <FormField control={form.control} name="fullName" render={({ field }) => (
                        <FormItem>
                            <FormLabel>Full Name</FormLabel>
                            <FormControl><Input placeholder="John Doe" {...field} /></FormControl>
                            <FormMessage />
                        </FormItem>
                    )} />

                    {/* 3. Email Field */}
                    <FormField control={form.control} name="email" render={({ field }) => (
                        <FormItem>
                            <FormLabel>Email</FormLabel>
                            <FormControl><Input type="email" placeholder="john@example.com" {...field} /></FormControl>
                            <FormMessage />
                        </FormItem>
                    )} />

                    {/* 4. Password Field */}
                    <FormField control={form.control} name="password" render={({ field }) => (
                        <FormItem>
                            <FormLabel>Password</FormLabel>
                            <FormControl><Input type="password" {...field} /></FormControl>
                            <FormMessage />
                        </FormItem>
                    )} />

                    {/* 5. Age Field */}
                    <FormField control={form.control} name="age" render={({ field }) => (
                        <FormItem>
                            <FormLabel>Age</FormLabel>
                            <FormControl><Input type="number" {...field} /></FormControl>
                            <FormMessage />
                        </FormItem>
                    )} />

                    <Button type="submit" className="w-full mt-6">Register</Button>
                </form>
            </Form>
        </div>
    );
}