<template>
  <v-app>
    <HeaderMain />

    <v-main>
      <BreadCrumb v-if="showBreadcrumb" :link="router.currentRoute.value.fullPath" />
      <RouterView />
    </v-main>

    <FooterMain />
  </v-app>
</template>

<script setup lang="ts">
import { RouterView, useRouter } from 'vue-router'
import { onMounted, ref, watch } from 'vue'

import BreadCrumb from '@/components/BreadCrumb.vue'
import FooterMain from '@/components/layout/FooterMain.vue'
import HeaderMain from '@/components/layout/HeaderMain.vue'

const router = useRouter()

const hideBreadcrumbIn = ['Home', 'Modeling', 'ViewLogin', 'ViewIntroduction', 'View404Page', 'ViewBugReport', 'ViewBugOverview']
const showBreadcrumb = ref(true)

watch(
  () => router.currentRoute.value.name,
  (newVal) => {
    showBreadcrumb.value = true

    hideBreadcrumbIn.forEach((route) => {
      //console.log(`if ${route} == ${newVal}`);
      if (route == newVal) {
        showBreadcrumb.value = false
      }
    })
  }
)

onMounted(() => {})
</script>

<style scoped lang="scss"></style>
