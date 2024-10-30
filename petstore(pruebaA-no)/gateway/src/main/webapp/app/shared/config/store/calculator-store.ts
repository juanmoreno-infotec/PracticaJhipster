import type { Calculator } from '@/shared/model/calculator.model';
import { defineStore } from 'pinia';

export interface CalculatorStateStorable {
  calculator: Calculator[] | null;
}

export const defaultCalculatorState: CalculatorStateStorable = {
  calculator: [],
};

export const useCalculatorStore = defineStore('calculator', {
  state: (): CalculatorStateStorable => ({ ...defaultCalculatorState }),
  getters: {
/*    listaDeTareas: state => state.tareas,*/
  },
  actions: {
/*    updateTareas(updatedTareas: Tarea[]) {
      this.tareas = updatedTareas;
      },*/
  },
});
