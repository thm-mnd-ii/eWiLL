import axios, { AxiosResponse } from "axios";
import Diagram from "../model/diagram/Diagram";
import Category from "../model/diagram/Category";
import authHeader from "./auth-header";

class DiagramService {
  getDiagramsByUserId(userId: number): Promise<AxiosResponse<Diagram[]>> {
    return axios.get("/api/diagram/user/" + userId, { headers: authHeader() });
  }

  postDiagram(diagram: Diagram) {
    return axios.post("/api/diagram/", diagram);
  }

  putDiagram(diagram: Diagram) {
    return axios
      .put("/api/diagram/", diagram)
      .then((response) => {
        console.log(response);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  deleteDiagram(diagram: Diagram) {
    return axios.delete("/api/diagram/" + diagram.id, {
      headers: authHeader(),
    });
  }

  getCategories(userId: number): Promise<AxiosResponse<Category[]>> {
    return axios.get("/category/user/" + userId, { headers: authHeader() });
  }

  postCategory(name: string, userid: number) {
    return axios.post("/category", { name: name, userid: userid });
  }

  deleteCategory(category: Category) {
    return axios.delete("/category/" + category.id, { headers: authHeader() });
  }

  getDiagramsWithCategory(
    categories: Category[],
    diagrams: Diagram[]
  ): Map<Category, Diagram[]> {
    const map: Map<Category, Diagram[]> = new Map();

    // map diagram to category
    diagrams.forEach((diagram) => {
      categories.forEach((category) => {
        if (diagram.categoryId === category.id) {
          if (map.has(category)) {
            map.get(category)?.push(diagram);
          } else {
            map.set(category, [diagram]);
          }
        }
      });
    });

    // categories.forEach((category) => {
    //   const diagramsSameCategory: Diagram[] = [];
    //   diagrams.forEach((diagram) => {
    //     if (category.id == diagram.category.id) {
    //       diagram.category.name = category.name;
    //       diagramsSameCategory.push(diagram);
    //     }
    //   });
    //   map.set(category.name, diagramsSameCategory);
    // });
    return map;
  }
}

export default new DiagramService();
