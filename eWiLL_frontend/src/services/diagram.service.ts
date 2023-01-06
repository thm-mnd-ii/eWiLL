import axios, { responseEncoding } from "axios";
import Diagram from "../model/diagram/Diagram";
import Category from "../model/diagram/Category";
import DiagramType from "../enums/DiagramType";
import authHeader from "./auth-header";
import { DiagnosticCategory } from "typescript";

class DiagramService {
  getDiagramsByUserId(userId: number) {
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

  getCategories(userId: number) {
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
  ): Map<string, Diagram[]> {
    const map: Map<string, Diagram[]> = new Map();

    // map diagram to category
    diagrams.forEach((diagram) => {
      categories.forEach((category) => {
        if (diagram.categoryId === category.id) {
          if (map.has(category.name)) {
            map.get(category.name)?.push(diagram);
          } else {
            map.set(category.name, [diagram]);
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
