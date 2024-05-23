import axios from "axios";
import store from "@/store";
import {ErrorCommandFactory} from "@/error";

const baseUrl = process.env.VUE_APP_API_URL + '/api';

export const api = axios.create({
    baseURL: baseUrl,
});

api.interceptors.request.use(
    config => {
        const accessToken = store.getters.getAccessToken;

        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        return config;
    }
);

api.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response) {
            const command = ErrorCommandFactory.createdCommand(error);
            command.execute();
        }
        throw new Error();
    }
)

