<!--
<script setup lang="ts">
import tareaEditComponent from './tarea-edit.component';
</script>
--->
<template>
    <daic-tarea-edit 
        :tarea="tareaToEdit" 
        @update:tarea="updateTareaHandler"
        @cancel:tarea="cancelTareaHandler">
    </daic-tarea-edit>
</template>
<!--
<script lang="ts" src="./tarea-edit.component.ts></script>
-->