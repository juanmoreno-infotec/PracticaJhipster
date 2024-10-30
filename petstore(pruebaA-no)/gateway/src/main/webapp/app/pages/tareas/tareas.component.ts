import { Tarea } from '@/shared/model/tarea.model';
import { defineComponent, ref, type Ref, inject, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { useTareaStore } from '@/store';
//Rutas de la tarea
import tareaEdit from '@/components/tarea-edit/tarea-edit.vue';
import TareaService from '@/pages/tareas/tareas.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  emits: ['confirmed'],
  name: 'Tareas',
  setup() {
    // Declaración de injeccion del servicio
    const tareaSerice = inject('tareaService', () => new TareaService());
    
    const textLabel: Ref<string> = ref('Hola mundo');
    const tareaStore = useTareaStore();
    const listaTareas: Ref<Tarea[] | null> = ref(tareaStore.listaDeTareas);
    const tareaToEdit: Ref<Tarea> = ref(new Tarea());
    const fields: Ref<string[]> = ref(['id', 'nombre', 'fechaLimite', 'acciones']);

    const createTareaModal = ref<any>(null);
    const deleteTareaModal = ref<any>(null);
    const editTareaModal = ref<any>(null);
    const isFetching: Ref<boolean> =ref(false);

    // Construccion de la tarea Listar
    const listarTareas = () => {
      isFetching.value = true;
        tareaService()
          .listar()
          .then(res => {
            listaTareas.value = res.data;
              if (tareaEdit.value) {
                  listaTareas.value?.forEach(tarea => {
                    if (tarea.fehcaLimite) {
                      tarea.fechaLimite = new Date(tarea.fechaLimite);
                    }
                  });
              }
          })
          // Atrapa el error en consola
          .catcher(err => {
            console.log(err);
          })
          // Finalizar la tarea
          .finally(() => {
            isFetching.value = false;
          });  
    };

    // Monta el listado de la tarea
    onMounted(() => {
      listarTareas();
    });

    return {
      textLabel,
      listaTareas,
      createTareaModal,
      deleteTareaModal,
      editTareaModal,
      tareaToEdit,
      tareaStore,
      fields,
      t$: useI18n().t,
      tareaService, // Se agrega servicio de la tarea
      listarTareas, // Declaración de la función de la tarea
    };
  },
  methods: {
    openCreateModalHandler(): void {
      this.tareaToEdit = new Tarea();
      this.createTareaModal.show();
    },
    clickHandler(): void {
      console.log('Se ejecuto un click');
    },
    openEditModalHandler(tarea: any): void {
      this.tareaToEdit = JSON.parse(JSON.stringify(tarea));
      this.tareaToEdit.fechaLimite = tarea.fechaLimite;
      this.editTareaModal.show();
    },
    openDeleteModalHandler(tarea: Tarea): void {
      this.tareaToEdit = JSON.parse(JSON.stringify(tarea));
      this.deleteTareaModal.show();
    },
    createTareaHandler(): void {
//      if (this.listaTareas) {
//        this.tareaToEdit.id = this.keygenerator();
//        this.listaTareas.push(this.tareaToEdit);
//        this.tareaToEdit = new Tarea();
//      }
//      this.createTareaModal.hide(); 
      // Se cambia para la ejecución de fetching para la tarea
      this.isFetching = true;
      this.tareaService()
        .crear(this.tareaToEdit)
        .then(tarea => {
          this.listarTareas();
        })
        .catch(err => {
          console.log(err);
        })
        .finally(() => {
          this.isFetching = false;
        });
      this.createTareaModal.hide();
    },
    deleteTareaHandler(): void {
      if (this.listaTareas) {
        const index = this.listaTareas.findIndex(tarea => tarea.id === this.tareaToEdit.id);
        this.listaTareas.splice(index, 1);
        this.deleteTareaModal.hide();
      }
    },
    updateTareaHandler(): void {
      if (this.listaTareas) {
        const index = this.listaTareas.findIndex(tarea => tarea.id === this.tareaToEdit.id);
        this.listaTareas.splice(index, 1, this.tareaToEdit);
        this.editTareaModal.hide();
      }
    },
    cancelHandler(): void {
      this.createTareaModal.hide();
      this.deleteTareaModal.hide();
      this.editTareaModal.hide();
    },
    keygenerator(): string {
      return new Date().getTime().toString();
    },
    isNombreValid(): boolean {
      if (this.tareaToEdit?.nombre?.length) {
        return this.tareaToEdit.nombre.length >= 3 && this.tareaToEdit.nombre.length <= 50;
      }
      return false;
    },
    isDescripcionValid(): boolean {
      if (this.tareaToEdit?.descripcion?.length) {
        return this.tareaToEdit.descripcion.length >= 3 && this.tareaToEdit.descripcion.length <= 100;
      }
      return false;
    },
    isDateValid(): boolean {
      if (this.tareaToEdit?.fechaLimite) {
        const now = new Date();
        const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
        const current = new Date(
          this.tareaToEdit.fechaLimite.getFullYear(),
          this.tareaToEdit.fechaLimite.getMonth(),
          this.tareaToEdit.fechaLimite.getDate(),
        );
        return current >= today;
      }
      return false;
    },
    isFormValid(): boolean {
      return this.isNombreValid() && this.isDescripcionValid() && this.isDateValid();
    },
  },
});
