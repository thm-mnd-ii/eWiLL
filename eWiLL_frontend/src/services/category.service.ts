import Category from "../model/diagram/Category"
import axios from "axios";

class CategoryService{
    getByUserId(userId: number): Promise<Category[]>{
        return new Promise<Category[]>((resolve, reject) => {
            axios.get("/category/user/" + userId)
            .then((response) => {
                resolve(response.data)
            }).catch((error) => {
                console.log(error)
            })
        })
    }
}

export default new CategoryService();