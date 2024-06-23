import axios from 'axios';
import {GetMapper, PostMapper} from "@dlabs71/d-dto";
import {UserModel} from "./account-model";

export class HomeAPI {

    static __USER_DATA_URL = "/account/current";

    @GetMapper(UserModel)
    getCurrentUser() {
        return axios.get(HomeAPI.__USER_DATA_URL);
    }

    @PostMapper(UserModel)
    saveCurrentUser(dto, file) {
        const form = new FormData();
        form.append('file', file);
        form.append('dto', new Blob([JSON.stringify(dto)], {
            type: "application/json"
        }));
        return axios.post(HomeAPI.__USER_DATA_URL, form);
    }

    deleteAccount() {
        return axios.delete(HomeAPI.__USER_DATA_URL);
    }
}


export default new HomeAPI();