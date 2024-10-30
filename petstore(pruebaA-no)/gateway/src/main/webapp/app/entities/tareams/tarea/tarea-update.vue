<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.tareamsTarea.home.createOrEditLabel"
          data-cy="TareaCreateUpdateHeading"
          v-text="t$('gatewayApp.tareamsTarea.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="tarea.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="tarea.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.tareamsTarea.nombre')" for="tarea-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="tarea-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.tareamsTarea.descripcion')" for="tarea-descripcion"></label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="tarea-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.tareamsTarea.fechaLimite')" for="tarea-fechaLimite"></label>
            <div class="d-flex">
              <input
                id="tarea-fechaLimite"
                data-cy="fechaLimite"
                type="datetime-local"
                class="form-control"
                name="fechaLimite"
                :class="{ valid: !v$.fechaLimite.$invalid, invalid: v$.fechaLimite.$invalid }"
                :value="convertDateTimeFromServer(v$.fechaLimite.$model)"
                @change="updateInstantField('fechaLimite', $event)"
              />
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./tarea-update.component.ts"></script>
