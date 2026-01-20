"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {z} from "zod";
import { userSchema, UserRequestDto } from "@/types/userSchema";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react"
import {UserResponseDto} from "@/types/types";
import api from "@/lib/api";
import { Form, FormField, FormItem, FormControl, FormMessage } from "@/components/ui/form";
import {
    FieldDescription,
    FieldLabel,
    FieldLegend,
    FieldSet,
} from "@/components/ui/field"

import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";
import {AxiosError} from "axios";
import {RadioGroup, RadioGroupItem} from "@/components/ui/radio-group";


interface BackendErrorResponse {
    status: number;
    message: string;
}
// 1. Define the Role Schema
const roleSchema = z.object({
    role: z.string().min(1, "Please select a role"),
});

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
    // 2. Initialize the Role Form
    const roleForm = useForm<z.infer<typeof roleSchema>>({
        resolver: zodResolver(roleSchema),
        defaultValues: {
            role: user?.roles[0] || "USER",
        },
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
            .catch(error => {
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

    // 3. New Submit Handler for Roles
    async function onSubmitUpdateRole(values: z.infer<typeof roleSchema>) {
        try {
            // Endpoint: /api/user/updateUserToAdmin/{email}
            // Assuming you want to pass the role as a plain string or body
            await api.post(`/user/updateUserToAdmin/${user?.email}`, values.role);
            alert("Role updated successfully!");
        } catch (error) {
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

            <FieldSet className="bg-white p-6 pt-18 rounded-xl shadow-sm border relative">
                <FieldLegend className="absolute top-8 left-6 text-xl font-semibold">User Information</FieldLegend>
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

            {/* 4. Second Form: Role Management */}
            <FieldSet className="bg-white p-6 pt-18 rounded-xl shadow-sm border relative">
                <FieldLegend className="absolute top-8 left-6 text-xl font-semibold">Permissions</FieldLegend>
                <FieldDescription>Change the security authorities for: {user?.fullName}</FieldDescription>

                <Form {...roleForm}>
                    <form onSubmit={roleForm.handleSubmit(onSubmitUpdateRole)} className="mt-8 space-y-6">
                        <FormField
                            control={roleForm.control}
                            name="role"
                            render={({ field }) => (
                                <FormItem>
                                    <FieldLabel>System Role</FieldLabel>
                                    <FormControl>
                                        {/* Integration of Radio Group with Form */}
                                        <RadioGroup
                                            onValueChange={field.onChange}
                                            defaultValue={field.value}
                                            className="grid grid-cols-1 md:grid-cols-3 gap-4"
                                        >
                                            <div className="flex items-center gap-3 space-x-2 border p-4 rounded-lg hover:bg-slate-50">
                                                <RadioGroupItem value="USER" id="r-user" />
                                                <FieldLabel htmlFor="r-user" className="cursor-pointer font-medium">Standard User</FieldLabel>
                                            </div>
                                            <div className="flex items-center gap-3 space-x-2 border p-4 rounded-lg hover:bg-slate-50 border-blue-200">
                                                <RadioGroupItem value="ADMIN" id="r-admin" />
                                                <FieldLabel htmlFor="r-admin" className="cursor-pointer font-medium text-blue-700">Administrator</FieldLabel>
                                            </div>
                                        </RadioGroup>
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        <div className="flex justify-end mt-6">
                            <Button type="submit" className="w-full sm:w-auto">
                                Save Permissions
                            </Button>
                        </div>
                    </form>
                </Form>
            </FieldSet>
        </div>
    );
}