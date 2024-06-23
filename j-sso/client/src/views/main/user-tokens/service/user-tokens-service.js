import axios from "axios";
import {GetMapper} from "@dlabs71/d-dto";
import {UserTokensModel} from "./user-tokens-model";

class UserTokensAPI {
    static __SEARCH = "/user-token/search";
    static __RECALL = "/user-token/recall";

    @GetMapper(UserTokensModel)
    search() {
        return axios.get(UserTokensAPI.__SEARCH);
    }

    recall(authorizationId) {
        return axios.post(UserTokensAPI.__RECALL, null, {
            params: {
                authorizationId: authorizationId
            }
        });
    }
}

export default new UserTokensAPI();