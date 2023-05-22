<template>
  <v-dialog v-model="saveDialog" width="30%">
    <v-card>
      <v-card-title>
        <span class="text-h5">Export Diagram</span>
      </v-card-title>
      <v-card-text>
        <v-form ref="form">
          <v-select v-model="exportCategory" :rules="[(v: any) => !!v || 'Item is required']" :items="['PNG']" item-title="name" item-value="id" label="Kategorie*" required></v-select>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="_close"> Close </v-btn>
        <v-btn variant="text" @click="exportDiagram"> Export </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useToolManagementStore } from "@/stores/toolManagementStore";
import { useDiagramStore } from "@/stores/diagramStore";
import * as rasterize from "rasterizehtml";

const toolManagementStore = useToolManagementStore();
const diagramStore = useDiagramStore();

const saveDialog = ref<boolean>(false);
const exportCategory = ref<string>("PNG");

const exportDiagram = () => {
  let div = toolManagementStore.diagramDiv?.cloneNode(true);
  if (div == null) {
    return;
  }

  if (exportCategory.value === "PNG") {
    let div = toolManagementStore.diagramDiv;
    if (div == null) {
      return;
    }

    setInlineStyles(div);

    // var rasterize = require("rasterizehtml");

    // More on the font face below
    var fontFaceRule = '<style>\n@font-face {\n font-family: "Roboto";\n }\n</style>';

    // Get the html with inlined styles
    var htmlContent = fontFaceRule + div.innerHTML;

    // Create a canvas element
    var canvas = document.createElement("canvas");
    canvas.setAttribute("id", "canvas");
    // Set the width and height of the canvas to match the element's
    canvas.width = div.getBoundingClientRect().width * 6;
    canvas.height = div.getBoundingClientRect().height * 6;

    // Get the 2D drawing context
    var context = canvas.getContext("2d");

    if (context == null) {
      return;
    }

    context.setTransform(6, 0, 0, 6, 0, 0);

    // Set the background color to white
    context.fillStyle = "#ffffff"; // white color
    context.fillRect(0, 0, canvas.width, canvas.height);

    // Append the canvas to your page, this does not have to be done on the body
    document.body.appendChild(canvas);
    // Draw the HTML
    rasterize.drawHTML(htmlContent, canvas).then(
      (renderResult: any) => {
        // Download as png
        var link2 = document.createElement("a");
        link2.download = diagramStore.diagram.name + ".png";
        link2.href = canvas.toDataURL("image/png");
        link2.click();

        // Remove the canvas from the DOM
        document.body.removeChild(canvas);

        // reload diagram
        diagramStore.key++;
      },
      (e) => {
        console.log(e);
      }

      // remove the canvas from the DOM
    );
  }
  _close();
};

const setInlineStyles = (svg: any) => {
  function explicitlySetStyle(element: any) {
    var cSSStyleDeclarationComputed = getComputedStyle(element);
    var i, len, key, value;
    var svgExcludedValues = ["height", "width", "min-height", "min-width"];
    var computedStyleStr = "";
    for (i = 0, len = cSSStyleDeclarationComputed.length; i < len; i++) {
      key = cSSStyleDeclarationComputed[i];
      if (!(element instanceof SVGElement && svgExcludedValues.indexOf(key) >= 0)) {
        value = cSSStyleDeclarationComputed.getPropertyValue(key);
        computedStyleStr += key + ":" + value + ";";
      }
    }
    element.setAttribute("style", computedStyleStr);
  }
  function traverse(obj: any) {
    var tree = [];
    tree.push(obj);
    visit(obj);
    function visit(node: any) {
      if (node && node.hasChildNodes()) {
        var child = node.firstChild;
        while (child) {
          if (child.nodeType === 1 && child.nodeName != "SCRIPT") {
            tree.push(child);
            visit(child);
          }
          child = child.nextSibling;
        }
      }
    }
    return tree;
  }
  // hardcode computed css styles inside SVG
  var allElements = traverse(svg);
  // console.log(allElements);

  var i = allElements.length;
  while (i--) {
    explicitlySetStyle(allElements[i]);
  }
};

// Promis
const resolvePromise: any = ref(undefined);
const rejectPromise: any = ref(undefined);

const openDialog = () => {
  saveDialog.value = true;

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
};

const _close = () => {
  saveDialog.value = false;
  resolvePromise.value(false);
};

// define expose
defineExpose({
  openDialog,
});
</script>

<style scoped lang="scss">
.save-diagram-form {
  // grid in one row
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr;
  grid-template-areas: "name category category";
  grid-column-gap: 10px;
  grid-row-gap: 10px;
  // align items
  align-items: center;
  // set width
  width: 100%;
}
</style>
