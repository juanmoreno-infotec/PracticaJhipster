/*import { Component, Vue } from 'vue';*/
import { Calculator } from '@/shared/model/calculator.model';
/*import { defineComponent, ref, inject, type ComputedRef, type Ref } from 'vue';*/
import { defineComponent, inject, computed } from 'vue';
import { getCurrentInstance } from 'vue';
import { useI18n } from 'vue-i18n';
import { useCalculatorStore } from '@/shared/config/store/calculator-store';
import type LoginService from '@/account/login.service';


export default defineComponent ({
  compatConfig: {MODE: 3},
  name: 'Calculator',
  setup(){
    // Logueo para la sesión desde el Login.servive para autentificación
    const LoginService = inject<LoginService>('loginService');
    const authenticated = inject<ComputedRef<boolean>>('authenticated');
    const username = inject<ComputedRef<string>>('currentUsername');

    // logica para cuando abres la autentificación
    const openLogin = () => {
      LoginService.openLogin();
    };

    return {
      authenticated,
      username,
      openLogin,
      t$: useI18n().t,
    };
  },

  props: {
    msg: String
  },

  data() {
    // Datos de la calculadora
    return {
      calcVal: '',
      calcBtns: ['C', '%', '√', '+',7, 8, 9,'-', 4, 5, 6,'*', 1, 2, 3, '/', 0, '.', '←', '='],
      operators: null,
      prevCalcVal: '',
      isCalculatorVisible: false,
    }
  }, 
  methods: {
    // Modal para hacer visible la calculadora
    openModalCalculator(): void {
        this.isCalculatorVisible = true;
    },
    // Modal para hacer no visible la calculadora
    closeModalCalculator(): void {
        this.isCalculatorVisible = false;
    },
    action(btn){
      // Boton para eliminar los numeros
      if (btn === '←') {
        this.calcVal = this.calcVal.slice(0, -1);
      } else {
      // Add error handling here
      if(!isNaN(btn) || btn === '.'){
        this.calcVal += btn +'';
      }
      }
      // Botón para limpiar los calculos
      if (btn === 'C') {
        this.calcVal = '';
      }
      // Botón para sacar los porcentajes
      if (btn === '%') {
        this.calcVal = this.calcVal / 100 + '';
      }

      // Logíca para las operaciones matematicas
      if (['/', '+', '-', '*', '√'].includes(btn)) {
        // Se agrega en el porcenate un diferencia para el igual y de el valor
        if (btn === '√') {
            this.calcVal = Math.sqrt(this.calcVal) + '';
        } else {     
            this.operators = btn;
            this.prevCalcVal = this.calcVal;
            this.calcVal = '';
        }
      }
      if (btn === "=") {
        try {
          this.calcVal = eval(this.prevCalcVal + this.operators + this.calcVal);
        } catch (error) {
          // Handle error (e.g., display an error message)
          console.error('Error:', error);
          this.calcVal = 'Error';
        } finally {
          this.prevCalcVal = '';
          this.operators = null;
        }
      }
    },
  },
});
