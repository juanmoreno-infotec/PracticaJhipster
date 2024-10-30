import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import TareaService from './tarea.service';
import { useDateFormat } from '@/shared/composables';
import { type ITarea } from '@/shared/model/tareams/tarea.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TareaDetails',
  setup() {
    const dateFormat = useDateFormat();
    const tareaService = inject('tareaService', () => new TareaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const tarea: Ref<ITarea> = ref({});

    const retrieveTarea = async tareaId => {
      try {
        const res = await tareaService().find(tareaId);
        tarea.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tareaId) {
      retrieveTarea(route.params.tareaId);
    }

    return {
      ...dateFormat,
      alertService,
      tarea,

      previousState,
      t$: useI18n().t,
    };
  },
});
