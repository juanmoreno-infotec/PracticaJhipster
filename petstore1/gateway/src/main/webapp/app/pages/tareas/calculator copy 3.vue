<template>
    <div> 
        <h1>Operaciones Matemáticas (Calculadora)</h1>
        <br>
        <div>
            En esta practica vamos a realizar operadores logicos que son: la suma, la resta, la multiplicación, división y la raíz cuadrada
        </div>
        <br>
        <b-button @click="openModalCalculator">Abrir Calculadora</b-button>
        <b-modal :visible="isCalculatorVisible" @hidden="closeModalCalculator">
            <div class="calculator p-3" style="max-width: 500px; margin: 15px auto; background-color: #234;">
                <div class="display w-full rounded m-1 p-3 text-end lead fw-bold text-white bg-vue-dark">
                    {{ calcVal || 0}}
                </div>
                <div class="buttons row g-0">
                    <button class="col-3 lead text-white text-center m-1 py-3 bg-vue-dark rounded btn-hover" 
                        :class="{'bg-vue-green': ['C', '*', '/', '+', '-', '=', '%'].includes(btn)}" 
                        v-for="btn in calcBtns" @click="action(btn)">
                        {{ btn }}
                    </button>
                </div>
            </div>
        <template #modal-footer>
            <b-button variant="outline-danger" @click="closeModalCalculator()">{{ $t('entity.action.cancel') }}</b-button>
        </template>
        </b-modal>
    </div>

    <div class="p-3" style="max-width: 400px; margin: 50px auto; background-color: #234;">

    <!-- Calculator Result -->
    <div class="w-full rounded m-1 p-3 text-end lead fw-bold text-white bg-vue-dark">
      {{ calcVal || 0}}
    </div>  

    <!-- Calculator Buttons -->
    <div class="row g-0">
      <div class="col-3" v-for="btn in calcBtns">
        <div 
        class="lead text-white text-center m-1 py-3 bg-vue-dark rounded btn-hover"
        :class="{'bg-vue-green': ['C', '*', '/', '+', '-', '=', '%'].includes(btn)}"
        @click="action(btn)"
          >
          {{ btn }}
        </div>
      </div>
    </div>
  </div>    
</template>

<script lang="ts" src="./calculator.component.ts"></script>

<style scoped>
.bg-vue-dark{
  background: #31475e;
}

.btn-hover:hover{
  cursor: pointer;
  background: #3D5875;
}

.bg-vue-green{
  background: #3fb984 ;
}
</style>