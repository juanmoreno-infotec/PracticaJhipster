<template>
    <!-- Verificación para Usuario-->
    <div v-if="authenticated"> 
        <b-button variant="outline-primary float-right px-3 py-2" class="rounded-circle" size="md" @click="openModalCalculator">+/-
            <i class="fas da-calculator"></i> 
        </b-button>
        <b-modal id="calculator-modal" :title="$t('calculator.title')" :visible="isCalculatorVisible" @hidden="closeModalCalculator">
            <template #modal-header>
                <h3 class="modal-title">{{ $t(('calculator.title')) }}</h3>
            </template>
            <!-- Margin display calculator-->
            <div class="calculator p-4 ms-3" style="max-width: 500px; margin: 5px auto; background-color: #234;">
                <!-- Calculator Result -->
                <div class="display ms-2 w-full rounded m-1 p-3 text-end lead fw-bold text-white bg-vue-dark">
                    {{ calcVal || 0}}
                </div>
                <!-- Calculator Buttons -->
                <div class="buttons row g-0">
                    <!-- bootstrap design buttons and actions-->
                    <button class="col-2 lead text-white text-center m-3 py-2 bg-vue-dark rounded btn-hover" 
                        :class="{'bg-vue-green': ['C', '*', '/', '+', '-', '√', '=', '%', '←'].includes(btn)}" 
                        v-for="btn in calcBtns" @click="action(btn)">
                        {{ btn }}
                    </button>
                </div>
            </div>
        <!-- close button -->
        <template #modal-footer>
            <b-button variant="outline-danger" @click="closeModalCalculator()">{{ $t('entity.action.cancel') }}</b-button>
        </template>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./calculadora.component.ts"></script>

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