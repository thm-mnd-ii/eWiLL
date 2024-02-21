export default interface Member{
    user: {
        id: number,
        username: string,
        firstName: string,
        lastName: string,
        roles: [
            {
                id: number,
                name: string
            }
        ]
    }

    role: string;

}
