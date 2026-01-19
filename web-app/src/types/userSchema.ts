import {z} from "zod";

export const userSchema = z.object({
    userName: z.string().min(1, "User name is required"),
    fullName: z.string().min(1, "Full name is required"),
    email: z.string().email("Invalid email format").min(1, "Email is required"),
    password: z.string().min(3, "Password must be at least 3 characters long"),
    age: z.number().min(1, "Age must be at least 1"),
});

export type UserRequestDto = z.infer<typeof userSchema>;