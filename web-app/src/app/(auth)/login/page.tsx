"use client";


import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import api from "@/lib/api";
import { useRouter } from "next/navigation";
import { AxiosError } from "axios";

// const USER_PATH = "/api/user";

const LoginSchema = z.object({
    email: z.string().email(),
    password: z.string().min(3),
});

export default function LoginPage() {
    const router = useRouter();
    const form = useForm<z.infer<typeof LoginSchema>>({
        resolver: zodResolver(LoginSchema),
        defaultValues: { email: "", password: "" },
    });

    interface BackendErrorResponse {
        status: number;
        message: string;
    }

    async function onSubmit(values: z.infer<typeof LoginSchema>) {
        try {
            // Backend expects @RequestParam, so we use 'params' in axios
            const response = await api.post(`/user/login`, null, {
                params: {
                    email: values.email,
                    password: values.password,
                },
            });

            // Assuming backend returns a plain JWT string
            if (response.data) {
                localStorage.setItem("token", response.data);
                window.dispatchEvent(new Event("storage"));
                router.push("/");
            }
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
        <div className="flex justify-center items-center min-h-screen">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6 w-96 p-8 border rounded-xl shadow-lg">
                    <h1 className="text-2xl font-bold">Login</h1>
                    <FormField control={form.control} name="email" render={({ field }) => (
                        <FormItem>
                            <FormLabel>Email</FormLabel>
                            <FormControl><Input placeholder="john@example.com" {...field} /></FormControl>
                            <FormMessage />
                        </FormItem>
                    )} />
                    <FormField control={form.control} name="password" render={({ field }) => (
                        <FormItem>
                            <FormLabel>Password</FormLabel>
                            <FormControl><Input type="password" {...field} /></FormControl>
                            <FormMessage />
                        </FormItem>
                    )} />
                    <Button type="submit" className="w-full">Sign In</Button>
                </form>
            </Form>
        </div>
    );
}