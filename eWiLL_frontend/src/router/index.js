import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/ViewHome.vue";
import ViewModelling from "../views/ViewModelling.vue";
import ViewChecker from "../views/ViewChecker.vue";
import ViewImpressum from "../views/ViewImpressum.vue";
import ViewDatenschutz from "../views/ViewDatenschutz.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/modelling",
      name: "modelling",
      component: ViewModelling,
    },
    {
      path: "/checker",
      name: "ViewChecker",
      component: ViewChecker,
    },
    {
      path: "/impressum",
      name: "ViewImpressum",
      component: ViewImpressum,
    },
    {
      path: "/datenschutz",
      name: "ViewDatenschutz",
      component: ViewDatenschutz,
    },
  ],
});

router.beforeEach((to, from, next) => {
  const publicPages = ["/login", "/modelling", "/checker"];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem("user");

  // trying to access a restricted page + not logged in
  // redirect to login page
  if (authRequired && !loggedIn) {
    next("/login");
  } else {
    next();
  }
});

export default router;
