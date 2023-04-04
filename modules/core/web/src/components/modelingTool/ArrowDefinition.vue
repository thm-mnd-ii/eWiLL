<template>
  <svg>
    <defs>
      <marker id="arrowhead" markerWidth="20" markerHeight="10" refX="5" refY="2" orient="auto">
        <polygon points="0 0, 0 4, 6 2" />
      </marker>

      <filter id="double">
        <feMorphology in="SourceGraphic" result="a" operator="dilate" radius="1.2" />
        <feComposite in="SourceGraphic" in2="a" result="xx" operator="xor" />
      </filter>
    </defs>
  </svg>
</template>
