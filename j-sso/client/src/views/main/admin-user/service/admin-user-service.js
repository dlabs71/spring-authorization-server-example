import axios from "axios";
import {GetMapper} from "@dlabs71/d-dto";
import {PageableResponse} from "./admin-user-model";

class AdminUserAPI {

    static __SEARCH = "/admin-user/search";
    static __ASSIGN = "/admin-user/assign-admin";
    static __DISMISS = "/admin-user/dismiss";
    static __AVATAR_URL = "/admin-user/avatar/{avatarFileId}"

    getAvatarUrl(fileId) {
        return AdminUserAPI.__AVATAR_URL.replace('{avatarFileId}', fileId);
    }

    @GetMapper(PageableResponse)
    search(page, pageSize, email) {
        return axios.get(AdminUserAPI.__SEARCH, {
            params: {
                page: page,
                pageSize: pageSize,
                email: email
            }
        });
    }

    assign(email) {
        return axios.post(AdminUserAPI.__ASSIGN, null, {
            params: {
                email: email
            }
        });
    }

    dismiss(userId) {
        return axios.post(AdminUserAPI.__DISMISS + `/${userId}`);
    }
}

export default new AdminUserAPI();