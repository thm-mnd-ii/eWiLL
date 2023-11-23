<template>
  <Transition>
    <v-card v-if="isVisible" class="mx-auto context-menu" width="170" :style="{ left: props.position?.x, top: props.position?.y }">
      <v-list v-model:opened="openGroups">
        <v-list-group value="elements">
          <template #activator="{ props: activatorProps }">
            <v-list-item v-bind="activatorProps" title="Elemente"></v-list-item>
          </template>

          <!-- Menu items for adding elements -->
          <v-list-item class="custom-list-item" @mousedown="addElement(EntityType.ENTITY)">
            <v-list-item-title>
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="52.9586 182.183 61.91 31.75">
                <path d="m53.058594 182.2832v0.70118 30.85156h61.705076v-31.55274h-61.705076zm1.40039 1.40039h58.904296v28.75196h-58.904296v-28.75196z" fill="#000000" />
              </svg>
            </v-list-item-title>
          </v-list-item>
          <v-list-item class="custom-list-item" @mousedown="addElement(EntityType.RELATIONSHIP)">
            <v-list-item-title>
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="87.3453 830.214 233.4 119.5">
                <path
                  d="m90.087891 830.31445-2.642579 2.65039v113.96094l2.642579 2.64453h227.923829l2.64258-2.64453v-113.96094l-2.64258-2.65039h-227.923829zm2.65039 5.29297h47.482419l-47.482419 48.14844v-48.14844zm54.919919 0h112.51758l53.83594 54.58985-54.08203 54.08007h-112.02539l-54.087894-54.08007 53.841794-54.58985zm119.94922 0h47.76172v48.43164l-47.76172-48.43164zm47.76172 60.7168v47.95312h-47.95312l47.95312-47.95312zm-222.630859 0.27344 47.679689 47.67968h-47.679689v-47.67968z"
                  fill="#000000"
                />
              </svg>
            </v-list-item-title>
          </v-list-item>
          <v-list-item class="custom-list-item" @mousedown="addElement(EntityType.ENTITYRELATIONSHIP)">
            <v-list-item-title>
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="141.89 718.091 233.4 119.5">
                <path d="m141.99023 718.19141v2.64257 116.61133h233.2168v-119.2539h-233.2168zm5.29297 5.29297h47.7461l-47.7461 48.4082v-48.4082zm55.17774 0h167.45312v108.66796h-167.72656l-53.81836-53.82031 54.0918-54.84765zm-55.17774 61.24804 47.41992 47.41992h-47.41992v-47.41992z" fill="#000000" />
              </svg>
            </v-list-item-title>
          </v-list-item>
        </v-list-group>
      </v-list>
    </v-card>
  </Transition>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
import { ref } from "vue";
import { useDiagramStore } from "../../stores/diagramStore";

import EntityType from "@/enums/EntityType";
const props = defineProps({
  isVisible: Boolean,
  position: Object,
  modelingContainer: {
    type: Object as () => HTMLElement | null,
    default: null,
  },
});

const emit = defineEmits(["closeMenu"]);

const diagramStore = useDiagramStore();

const openGroups = ref(["elements"]);

const addElement = (type: EntityType) => {
  const container = props.modelingContainer;

  if (!container) return;

  const containerRect = container.getBoundingClientRect();

  const parsedPositionX = parseInt(props.position?.x);
  const parsedPositionY = parseInt(props.position?.y);

  const positionX = parsedPositionX - containerRect.left + container.scrollLeft;
  const positionY = parsedPositionY - containerRect.top + container.scrollTop;

  if (diagramStore.diagram.entities.length === 0) {
    diagramStore.diagram.entities.push({
      id: 1,
      type: type,
      entityName: "New Entity",
      attributes: [],
      top: positionY,
      left: positionX,
      width: 100,
    });

    diagramStore.saveHistory();
    emit("closeMenu");
    return;
  }

  const ids = diagramStore.diagram.entities.map((entity: { id: any }) => {
    return entity.id;
  });
  const max = Math.max(...ids);
  const nextID = max + 1;

  diagramStore.diagram.entities.push({
    id: nextID,
    type: type,
    entityName: "New Entity",
    attributes: [],
    top: positionY,
    left: positionX,
    width: 100,
  });

  diagramStore.saveHistory();
  emit("closeMenu");
};
</script>

<style scoped lang="scss">
.context-menu {
  position: fixed;
  z-index: 1000;
  cursor: pointer;
}

.custom-list-item {
  &:hover {
    background-color: #f6f6f6;
    transition: background-color 0.2s ease-in-out;
  }

  svg {
    width: 100px;
  }
}

.v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>
