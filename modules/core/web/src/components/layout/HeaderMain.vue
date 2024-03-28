<template>
  <v-app-bar v-if="$router.currentRoute.value.name != 'ViewLogin' && pageSettingsStore.showHeader == true" :elevation="1">
    <v-app-bar-nav-icon @click.stop="showSideBar = !showSideBar" />
    <v-toolbar-title>
      <router-link to="/">
        <IconFBS class="icon" />
      </router-link>
    </v-toolbar-title>
    <v-spacer />
    <div>
      <DropdownUserNav></DropdownUserNav>
    </div>
  </v-app-bar>

  <v-navigation-drawer v-if="$router.currentRoute.value.name != 'ViewLogin' && pageSettingsStore.showHeader == true" v-model="showSideBar" width="220" expand-on-hover rail>
    <v-list density="compact" active-class="active" nav>
      <v-list-item to="/" active-class="active" prepend-icon="mdi-human-greeting" title="Willkommen" value="introduction" />
      <v-list-item to="/home" active-class="active" prepend-icon="mdi-home-variant" title="Startseite" value="home" />
      <v-list-item to="/course" active-class="active" prepend-icon="mdi-file-multiple" title="Alle Kurse" value="course" />
      <v-list-item to="/modeling" active-class="active" prepend-icon="mdi-pencil-ruler" title="Modellierung" value="modeling" />
      <v-list-item to="/diagramConfig" active-class="active" prepend-icon="mdi-cog" title="Diagramm Konfiguration" value="diagramConfig" />

      <v-list-item to="/feedbackReport" active-class="active" prepend-icon="mdi-bug" title="Feedback" value="report" />
      <v-list-item v-if="admin" to="/feedbackOverview" active-class="active" prepend-icon="mdi-view-dashboard-variant-outline" title="Feedback Overview" value="report" />
    </v-list>
  </v-navigation-drawer>

  <v-app-bar v-if="$router.currentRoute.value.name != 'ViewLogin' && pageSettingsStore.showHeader == false" :elevation="1">
    <v-app-bar-title>
      <router-link to="/">
        <IconFBS class="icon" />
      </router-link>
    </v-app-bar-title>

    <template #append>
      <v-btn to="/course" active-class="active" prepend-icon="mdi-file-multiple">
        <span v-if="!smAndDown"> Alle Kurse </span>
        <v-tooltip v-if="smAndDown" activator="parent" location="bottom"> Alle Kurse </v-tooltip>
      </v-btn>
      <v-btn to="/modeling" active-class="active" prepend-icon="mdi-pencil-ruler">
        <span v-if="!smAndDown"> Modellierung </span>
        <v-tooltip v-if="smAndDown" activator="parent" location="bottom"> Modellierung </v-tooltip>
      </v-btn>
      <v-btn to="/feedbackReport" active-class="active" prepend-icon="mdi-bug">
        <span v-if="!smAndDown"> Feedback </span>
        <v-tooltip v-if="smAndDown" activator="parent" location="bottom"> Feedback </v-tooltip>
      </v-btn>
      <v-btn v-if="admin" to="/feedbackOverview" active-class="active" prepend-icon="mdi-view-dashboard-variant-outline">
        <span v-if="!smAndDown"> Feedback Overview </span>
        <v-tooltip v-if="smAndDown" activator="parent" location="bottom"> Feedback Overview </v-tooltip>
      </v-btn>
      <v-btn v-if="admin" icon @click="pageSettingsStore.showHeader = !pageSettingsStore.showHeader">
        <v-icon>{{ pageSettingsStore.showHeader ? 'mdi-eye-off' : 'mdi-eye' }}</v-icon>
        <v-tooltip activator="parent" location="bottom"> Toggle Bar </v-tooltip>
      </v-btn>
    </template>
  </v-app-bar>
</template>

<script setup lang="ts">
import IconEWiLL from '@/components/icons/IconEWiLL.vue'
import IconFBS from '@/components/icons/IconFBS.vue'
import DropdownUserNav from '@/components/modelingTool/DropdownUserNav.vue'

import { usePageSettingsStore } from '@/stores/pageSettingsStore'

import { RouterLink } from 'vue-router'
import { onMounted, ref } from 'vue'
import { useDisplay } from 'vuetify'

const { smAndDown } = useDisplay()

const showSideBar = ref(true)
const admin = ref(localStorage.getItem('user')?.includes('ADMIN'))

const pageSettingsStore = usePageSettingsStore()

onMounted(() => {
  console.log(pageSettingsStore.showHeader)
})
</script>

<style scoped lang="scss">
.icon {
  height: 35px;
  width: auto;
  margin-right: 20px;
}
</style>
