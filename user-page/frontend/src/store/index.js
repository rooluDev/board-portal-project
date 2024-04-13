import {createStore} from "vuex";
import createPersistedState from 'vuex-persistedstate';

export default createStore({
    state: {
        accessToken: null,
        previousRoute: null,
        nextRoute: null
    },
    getters: {
        getAccessToken(state) {
            return state.accessToken;
        },
        getPreviousRoute(state) {
            return state.previousRoute
        },
        getNextRoute(state) {
            return state.nextRoute
        }
    },
    mutations: {
        SET_TOKEN(state, accessToken) {
            state.accessToken = accessToken;
        },
        EXPIRE_TOKEN(state) {
            state.accessToken = null;
        },
        SET_PREVIOUS_ROUTE(state, route) {
            state.previousRoute = route;
        },
        DELETE_PREVIOUS_ROUTE(state) {
            state.previousRoute = null;
        },
        SET_NEXT_ROUTE(state, route) {
            state.nextRoute = route
        },
        DELETE_NEXT_ROUTE(state) {
            state.nextRoute = null;
        }
    },
    actions: {
        storeToken({commit}, token) {
            commit('SET_TOKEN', token);
        },
        deleteToken({commit}) {
            commit('EXPIRE_TOKEN');
        },
        storePreviousRoute({commit}, route) {
            commit('SET_PREVIOUS_ROUTE', route);
        },
        deletePreviousRoute({commit}) {
            commit('DELETE_PREVIOUS_ROUTE')
        },
        storeNextRoute({commit}, route) {
            commit('SET_NEXT_ROUTE', route);
        },
        deleteNextRoute({commit}) {
            commit('DELETE_NEXT_ROUTE')
        }
    },
    plugins: [createPersistedState()]
});