import Vue from 'vue'
import Vuex from 'vuex'
import {
  store
} from '@/assets/js/vuex-store'

Vue.use(Vuex)

const storeFactory = () => {
  return store
}

export default storeFactory
