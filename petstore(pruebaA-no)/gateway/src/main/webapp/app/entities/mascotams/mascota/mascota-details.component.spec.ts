/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MascotaDetails from './mascota-details.vue';
import MascotaService from './mascota.service';
import AlertService from '@/shared/alert/alert.service';

type MascotaDetailsComponentType = InstanceType<typeof MascotaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mascotaSample = { id: 'ABC' };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Mascota Management Detail Component', () => {
    let mascotaServiceStub: SinonStubbedInstance<MascotaService>;
    let mountOptions: MountingOptions<MascotaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      mascotaServiceStub = sinon.createStubInstance<MascotaService>(MascotaService);

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
          mascotaService: () => mascotaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mascotaServiceStub.find.resolves(mascotaSample);
        route = {
          params: {
            mascotaId: '' + 'ABC',
          },
        };
        const wrapper = shallowMount(MascotaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.mascota).toMatchObject(mascotaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mascotaServiceStub.find.resolves(mascotaSample);
        const wrapper = shallowMount(MascotaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});