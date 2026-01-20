// "use client";
//
// import { useForm } from "react-hook-form";
// import { zodResolver } from "@hookform/resolvers/zod";
// import { userSchema, UserRequestDto } from "@/types/userSchema";
// import { useParams } from "next/navigation";
// import { useEffect, useState } from "react"
// import {UserResponseDto} from "@/types/types";
// import api from "@/lib/api";
// import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from "@/components/ui/form";
// import {
//     Field,
//     FieldDescription,
//     FieldGroup,
//     FieldLabel,
//     FieldLegend,
//     FieldSet,
// } from "@/components/ui/field"
//
// import { Input } from "@/components/ui/input"
// import { Button } from "@/components/ui/button";
// import { useRouter } from "next/navigation";
// import {AxiosError} from "axios";
//
//
// interface BackendErrorResponse {
//     status: number;
//     message: string;
// }
//
//
// export default function UserDetailPage() {
//     const router = useRouter();
//     const params = useParams()
//     const id = params.id // Captures the '1' from /users/1
//     const [user, setUser] = useState<UserResponseDto>()
//
//     useEffect(() => {
//         api.get(`/user/get/id/${id}`)
//             .then(res => setUser(res.data))
//             .catch(err => {
//                 const status = err.response?.status;
//                 const message = err.response?.message;
//                 console.error("Access Denied with message: ", message, " and status: ", status);
//             })
//     }, [])
//     console.log(user)
//
//     const form = useForm({
//         resolver: zodResolver(userSchema),
//         defaultValues: {
//             userName: user?.userName,
//             fullName: user?.fullName,
//             email: user?.email,
//             password: "",
//             age: user?.age // This matches the schema's number type
//         }
//     });
//
//     async function onSubmit(values: UserRequestDto) {
//         try {
//             // Calls your @PostMapping("/api/user/create")
//             await api.post("/api/user/createUser", values);
//             router.push("/login"); // Redirect to login after success
//         } catch (error: unknown) {
//             // 2. Check if this is an Axios Error
//             const axiosError = error as AxiosError<BackendErrorResponse>;
//
//             if (axiosError.response && axiosError.response.data) {
//                 // Now TypeScript knows 'data' has 'message' and 'status'
//                 console.error("Backend Status:", axiosError.response.data.status);
//                 console.error("Backend Message:", axiosError.response.data.message);
//
//                 alert(`Error: ${axiosError.response.data.message}`);
//             } else if (error instanceof Error) {
//                 // 3. Fallback for generic JS errors (like network failure)
//                 console.error("Network/Generic Error:", error.message);
//             }
//         }
//     }
//
//     return (
//         <div className="w-full max-w-md space-y-6">
//             <FieldSet>
//                 <FieldLegend>User Information</FieldLegend>
//                 <FieldDescription>
//                     Detailed information about selected user.
//                 </FieldDescription>
//
//                 <FieldGroup>
//                     <Form {...form}>
//                         <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4 w-full max-w-md p-6 border rounded-lg shadow-md bg-white">
//                             <h2 className="text-2xl font-bold mb-4 text-center">Update Account</h2>
//
//                             {/* 1. Username Field */}
//                             <FormField control={form.control} name="userName" render={({ field }) => (
//                                 <FormItem>
//                                     <FieldLabel htmlFor="street">Username</FieldLabel>
//                                     <FormLabel>Username</FormLabel>
//                                     <FormControl><Input placeholder="jdoe123" {...field} /></FormControl>
//                                     <FormMessage />
//                                 </FormItem>
//                             )} />
//
//                             {/* 2. Full Name Field */}
//                             <FormField control={form.control} name="fullName" render={({ field }) => (
//                                 <FormItem>
//                                     <FieldLabel htmlFor="street">Full Name</FieldLabel>
//                                     <FormLabel>Full Name</FormLabel>
//                                     <FormControl><Input placeholder="John Doe" {...field} /></FormControl>
//                                     <FormMessage />
//                                 </FormItem>
//                             )} />
//
//                             {/* 3. Email Field */}
//                             <FormField control={form.control} name="email" render={({ field }) => (
//                                 <FormItem>
//                                     <FieldLabel htmlFor="street">Email</FieldLabel>
//                                     <FormLabel>Email</FormLabel>
//                                     <FormControl><Input type="email" placeholder="john@example.com" {...field} /></FormControl>
//                                     <FormMessage />
//                                 </FormItem>
//                             )} />
//
//                             {/*/!* 4. Password Field *!/*/}
//                             {/*<FormField control={form.control} name="password" render={({ field }) => (*/}
//                             {/*    <FormItem>*/}
//                             {/*        <FieldLabel htmlFor="street">Email</FieldLabel>*/}
//                             {/*        <FormLabel>Password</FormLabel>*/}
//                             {/*        <FormControl><Input type="password" {...field} /></FormControl>*/}
//                             {/*        <FormMessage />*/}
//                             {/*    </FormItem>*/}
//                             {/*)} />*/}
//
//                             {/* 5. Age Field */}
//                             <FormField control={form.control} name="age" render={({ field }) => (
//                                 <FormItem>
//                                     <FieldLabel htmlFor="street">Age</FieldLabel>
//                                     <FormLabel>Age</FormLabel>
//                                     <FormControl><Input type="number" {...field} /></FormControl>
//                                     <FormMessage />
//                                 </FormItem>
//                             )} />
//
//                             <Button type="submit" className="w-full mt-6">Update</Button>
//                         </form>
//                     </Form>
//                 </FieldGroup>
//             </FieldSet>
//         </div>
//     )
//
//     // return (
//     //     <div className="p-8">
//     //         <h1 className="text-2xl font-bold">User Details for userName: {user?.userName}</h1>
//     //         <h1 className="text-2xl font-bold">User Details for fullName: {user?.fullName}</h1>
//     //         <h1 className="text-2xl font-bold">User Details for email: {user?.email}</h1>
//     //         <h1 className="text-2xl font-bold">User Details for age: {user?.age}</h1>
//     //         <h1 className="text-2xl font-bold">User Details for currentlyActive: {user?.currentlyActive ? 'true' : 'false'}</h1>
//     //         <h1 className="text-2xl font-bold">User Details for roles: {user?.roles.map((role) => (
//     //             <Badge key={role} variant="outline">
//     //                 {role}
//     //             </Badge>
//     //         ))}</h1>
//     //     </div>
//     // )
// }