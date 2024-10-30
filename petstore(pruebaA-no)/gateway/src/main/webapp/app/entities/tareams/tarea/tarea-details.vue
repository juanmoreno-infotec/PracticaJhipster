<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tarea">
        <h2 class="jh-entity-heading" data-cy="tareaDetailsHeading">
          <span v-text="t$('gatewayApp.tareamsTarea.detail.title')"></span> {{ tarea.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.tareamsTarea.nombre')"></span>
          </dt>
          <dd>
            <span>{{ tarea.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.tareamsTarea.descripcion')"></span>
          </dt>
          <dd>
            <span>{{ tarea.descripcion }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.tareamsTarea.fechaLimite')"></span>
          </dt>
          <dd>
            <span v-if="tarea.fechaLimite">{{ formatDateLong(tarea.fechaLimite) }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="tarea.id" :to="{ name: 'TareaEdit', params: { tareaId: tarea.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tarea-details.component.ts"></script>
