/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import TareaUpdate from './tarea-update.vue';
import TareaService from './tarea.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

type TareaUpdateComponentType = InstanceType<typeof TareaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tareaSample = { id: 'ABC' };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TareaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Tarea Management Update Component', () => {
    let comp: TareaUpdateComponentType;
    let tareaServiceStub: SinonStubbedInstance<TareaService>;

    beforeEach(() => {
      route = {};
      tareaServiceStub = sinon.createStubInstance<TareaService>(TareaService);
      tareaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          tareaService: () => tareaServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(TareaUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TareaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tarea = tareaSample;
        tareaServiceStub.update.resolves(tareaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tareaServiceStub.update.calledWith(tareaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        tareaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TareaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tarea = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tareaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        tareaServiceStub.find.resolves(tareaSample);
        tareaServiceStub.retrieve.resolves([tareaSample]);

        // WHEN
        route = {
          params: {
            tareaId: `${tareaSample.id}`,
          },
        };
        const wrapper = shallowMount(TareaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.tarea).toMatchObject(tareaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tareaServiceStub.find.resolves(tareaSample);
        const wrapper = shallowMount(TareaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
