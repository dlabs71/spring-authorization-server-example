import ComponentsLib from './components/components-lib-plugin';
import MixinsLib from './mixins/mixins-lib-plugin';
import PluginsLib from './plugins/plugins-lib-plugin';

export default {
    install(Vue, opts) {
        Vue.use(ComponentsLib, opts);
        Vue.use(MixinsLib, opts);
        Vue.use(PluginsLib, opts);
    }
}
