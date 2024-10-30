import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TareaService from './tarea.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ITarea, Tarea } from '@/shared/model/tareams/tarea.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TareaUpdate',
  setup() {
    const tareaService = inject('tareaService', () => new TareaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tarea: Ref<ITarea> = ref(new Tarea());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTarea = async tareaId => {
      try {
        const res = await tareaService().find(tareaId);
        res.fechaLimite = new Date(res.fechaLimite);
        tarea.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tareaId) {
      retrieveTarea(route.params.tareaId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {},
      descripcion: {},
      fechaLimite: {},
    };
    const v$ = useVuelidate(validationRules, tarea as any);
    v$.value.$validate();

    return {
      tareaService,
      alertService,
      tarea,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      ...useDateFormat({ entityRef: tarea }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.tarea.id) {
        this.tareaService()
          .update(this.tarea)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('tareamsApp.tareamsTarea.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tareaService()
          .create(this.tarea)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('tareamsApp.tareamsTarea.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
