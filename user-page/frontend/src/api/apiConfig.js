import axios from "axios";
import store from "@/store";
import {ErrorCommandFactory} from "@/error";

export const api = axios.create({
    baseURL: '/api',
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

