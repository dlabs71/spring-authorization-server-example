import axios from 'axios';
import {GetMapper} from "@dlabs71/d-dto";
import {UserModel} from "@/views/home/service/home-model";

export class HomeAPI {

    static __USER_DATA_URL = "/security-session/user";

    @GetMapper(UserModel)
    getCurrentUser() {
        return axios.get(HomeAPI.__USER_DATA_URL);
    }
}


export default new HomeAPI();