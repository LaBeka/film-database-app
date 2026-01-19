import api from '@/lib/api';
import { UserResponseDto } from '@/types/types';

const USER_PATH = "/api/user";

export const createNewUser = () => api.get<UserResponseDto>(`${USER_PATH}/createUser`);
export const getUserByEmailTest = (email: string) => api.get<UserResponseDto>(`${USER_PATH}/getTest/email/${email}`);
export const getList = () => api.get<UserResponseDto[]>(`${USER_PATH}/get/list/no`);

console.log(getList);