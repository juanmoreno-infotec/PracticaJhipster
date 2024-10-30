/*import { Component, Vue } from 'vue';*/
import { Calculator } from '@/shared/model/calculator.model';
import { defineComponent, ref, type Ref } from 'vue';
import { getCurrentInstance } from 'vue';
import { useI18n } from 'vue-i18n';
import { useCalculatorStore } from '@/shared/config/store/calculator-store';

export default defineComponent ({
  name: 'Calculator',
  props: {
    msg: String
  },

  data() {
    return {
      calcVal: '',
      calcBtns: ['C', '%', '=', '+', '√',7, 8, 9,'-', 4, 5, 6,'*', 1, 2, 3, '/', 0, '.'],
      operators: null,
      prevCalcVal: '',
      isCalculatorVisible: false
    }
  }, 
  methods: {
    openModalCalculator(): void {
        this.isCalculatorVisible = true;
    },
    closeModalCalculator(): void {
        this.isCalculatorVisible = false;
    },
    action(btn){

      if(!isNaN(btn) || btn === '.'){
        this.calcVal += btn +'';
      }

      if (btn === 'C') {
        this.calcVal = '';
      }

      if (btn === '%') {
        this.calcVal = this.calcVal / 100 + '';
      }

      if (['/', '+', '-', '*', '√'].includes(btn)) {
        if (btn === '√') {
            this.calcVal = Math.sqrt(this.calcVal) + '';
        } else {     
            this.operators = btn;
            this.prevCalcVal = this.calcVal;
            this.calcVal = '';
        }
      }
      if (btn === "=") {
          this.calcVal = eval(
            this.prevCalcVal + this.operators + this.calcVal
            );
          this.prevCalcVal = '';
          this.operators = null;
      }
    }
  },
});
