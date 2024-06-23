import axios from "axios";
import {GetMapper} from "@dlabs71/d-dto";
import {PageableResponse} from "./user-events-model";

class UserEventsAPI {
    static __SEARCH = "/user-event/search";

    @GetMapper(PageableResponse)
    search(page, pageSize) {
        return axios.get(UserEventsAPI.__SEARCH, {
            params: {
                page: page,
                pageSize: pageSize
            }
        })
    }
}

export default new UserEventsAPI();