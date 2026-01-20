export interface UserResponseDto {
    id: number;
    userName: string;
    fullName: string;
    email: string;
    currentlyActive: boolean;
    age: number;
    roles: string[];
}

export interface FilmResponseDto {
    id: number;
    title: string;
    genre: string;
    casta: string[];
    ageRestriction: number;
    awards: string[];
    langusges: string[];
    aspectRatio: number;
    colorStatus: string;
    cameraUsed: string;
}

export interface FilmReviewResponseDto {
    filmId : number;
    reviews : ReviewResponseDto[];
}

export interface ReviewResponseDto {
    index : number;
    userName : string;
    text : string;
    date : Date;
    score : number;
}