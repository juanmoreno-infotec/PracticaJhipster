/*import { Component, Vue } from 'vue';
import { Tarea } from '@/shared/model/tarea.model';
import { defineComponent, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useTareaStore } from '@/store';

export default defineComponent({
  compatConfig: { MODE: 3, COMPONENT_V_MODEL: false },
  name: 'TareaEdit',
  props: {
    tarea: {
      type: Tarea,
      required: true
    },
    readonly: {
      type: Boolean,
      default: false
    }
  },
//  methods: {
//    get tareaToEdit() {
//        return this.readonly ? { ...this.tarea } : this.tarea;
//    },
  data() {
    return {
      // Copia local de la tarea para realizar los cambios
      localTarea: {}
    }
  },
  watch: {
    tarea: {
      deep: true,
      handler(newValue) {
        this.localTarea = JSON.parse(JSON.stringify(newValue));
      }
    }
  },
  computed: {
    tareaToEdit() {
      return this.localTarea;
    },
    isFormValid() {
      // Lógica de validación de los campos
//      return this.tareaToEdit.nombre && this.tareaToEdit.nombrie.length >= 3;
//      return this.tareaToEdit.nombre && this.tareaToEdit.nombre.lenghth >= 3;
    }
  },
  methods: {
    updateTareaHandler() {
      this.$emit('update:tarea', this.tareaToEdit);
    },
    cancelHandler() {
      this.$emit('cancel:tarea');
    }
  }
});
*/