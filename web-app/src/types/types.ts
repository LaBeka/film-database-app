export interface UserResponseDto {
    id: number;
    userName: string;
    fullName: string;
    email: string;
    currentlyActive: boolean;
    age: number;
    roles: string[];
}