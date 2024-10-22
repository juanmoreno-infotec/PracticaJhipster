import { defineComponent } from 'vue';
import { useI18n } from 'vue-i18n';
import { Calculator } from '@/shared/model/calculator.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'JhiFooter',
  setup() {
    return {
      t$: useI18n().t,
    };
  },
  components: {
    Calculator
  },
  data() {
    return {
      showCalculator: false
    }
  },
  methods: {
    toggleCalculator() {
      this.showCalculator = !this.showCalculator;
    }
  }
});
