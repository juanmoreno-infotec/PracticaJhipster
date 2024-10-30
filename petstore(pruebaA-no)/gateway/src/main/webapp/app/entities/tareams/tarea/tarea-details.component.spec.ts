/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TareaDetails from './tarea-details.vue';
import TareaService from './tarea.service';
import AlertService from '@/shared/alert/alert.service';

type TareaDetailsComponentType = InstanceType<typeof TareaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tareaSample = { id: 'ABC' };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Tarea Management Detail Component', () => {
    let tareaServiceStub: SinonStubbedInstance<TareaService>;
    let mountOptions: MountingOptions<TareaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      tareaServiceStub = sinon.createStubInstance<TareaService>(TareaService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          tareaService: () => tareaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tareaServiceStub.find.resolves(tareaSample);
        route = {
          params: {
            tareaId: '' + 'ABC',
          },
        };
        const wrapper = shallowMount(TareaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.tarea).toMatchObject(tareaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tareaServiceStub.find.resolves(tareaSample);
        const wrapper = shallowMount(TareaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
