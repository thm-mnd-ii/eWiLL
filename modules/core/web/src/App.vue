<template>
  <v-app>
    <v-app-bar v-if="$router.currentRoute.value.name != 'ViewLogin'" :elevation="1">
      <v-app-bar-nav-icon @click.stop="showSideBar = !showSideBar" />
      <v-toolbar-title>
        <router-link to="/">
          <IconEWiLL class="icon" />
          <IconFBS class="icon" />
        </router-link>
      </v-toolbar-title>
      <v-spacer />
      <div>
        <DropdownUserNav></DropdownUserNav>
      </div>
    </v-app-bar>

    <v-navigation-drawer v-if="$router.currentRoute.value.name != 'ViewLogin'" v-model="showSideBar" width="220"
      expand-on-hover rail>
      <v-list density="compact" active-class="active" nav>
        <v-list-item to="/" active-class="active" prepend-icon="mdi-human-greeting" title="Willkommen"
          value="introduction" />
        <v-list-item to="/home" active-class="active" prepend-icon="mdi-home-variant" title="Startseite" value="home" />
        <v-list-item to="/course" active-class="active" prepend-icon="mdi-file-multiple" title="Alle Kurse"
          value="course" />
        <v-list-item to="/oralExam" active-class="active" prepend-icon="mdi-bug" title="Oral Examinator" value="/" />
        <v-list-item to="/modeling" active-class="active" prepend-icon="mdi-pencil-ruler" title="Modellierung"
          value="modeling" />
        <v-list-item to="/feedbackReport" active-class="active" prepend-icon="mdi-bug" title="Feedback" value="report" />
        <v-list-item v-if="admin" to="/feedbackOverview" active-class="active"
          prepend-icon="mdi-view-dashboard-variant-outline" title="Feedback Overview" value="report" />
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <BreadCrumb v-if="showBreadcrumb" :link="router.currentRoute.value.fullPath" />
      <RouterView />
    </v-main>

    <v-footer v-if="$router.currentRoute.value.name != 'ViewLogin'" app absolute elevation="3">
      <v-row justify="center" no-gutters class="mt-2">
        <v-btn v-for="link in links" :key="link.name" :to="link.url" color="gray" variant="text" class="mx-2"
          rounded="xl">
          {{ link.name }}
        </v-btn>
        <v-col class="text-center mt-4" cols="12"><strong> &copy; {{ new Date().getFullYear() }} Technische Hochschule
            Mittelhessen</strong>
        </v-col>
      </v-row>
    </v-footer>
  </v-app>
</template>

<script setup lang="ts">
import { RouterLink, RouterView, useRouter } from "vue-router";
import { onMounted, ref } from "vue";
import IconEWiLL from "./components/icons/IconEWiLL.vue";
import IconFBS from "./components/icons/IconFBS.vue";
import DropdownUserNav from "./components/modelingTool/DropdownUserNav.vue";
import BreadCrumb from "@/components/BreadCrumb.vue";
import { watch } from "vue";

const router = useRouter();

const hideBreadcrumbIn = ["Home", "Modeling", "ViewLogin", "ViewIntroduction", "View404Page", "ViewBugReport", "ViewBugOverview"];

const showSideBar = ref(true);
const showBreadcrumb = ref(true);
const admin = ref(localStorage.getItem("user")?.includes("ADMIN"));

const links = [
  { name: "Impressum", url: "/impressum" },
  { name: "Datenschutz", url: "/datenschutz" },
];

watch(
  () => router.currentRoute.value.name,
  (newVal) => {
    showBreadcrumb.value = true;

    hideBreadcrumbIn.forEach((route) => {
      //console.log(`if ${route} == ${newVal}`);
      if (route == newVal) {
        showBreadcrumb.value = false;
      }
    });
  }
);

onMounted(() => {});
</script>

<style scoped lang="scss">
.icon {
  height: 35px;
  width: auto;
  margin-right: 20px;
}
</style>
