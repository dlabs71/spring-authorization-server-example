import {GetMapper, PostMapper} from "@dlabs71/d-dto";
import {OauthClientModel, PageableResponse} from "./oauth-client-model";
import axios from "axios";

class OauthClientAPI {

    static __GET = "/oauth2-client/{clientId}";
    static __SEARCH = "/oauth2-client/search";
    static __SAVE = "/oauth2-client";
    static __DELETE = "/oauth2-client/{clientId}";
    static __GEN_SECRET = "/oauth2-client/gen-secret/{clientId}";

    static __create_url_get(clientId) {
        return OauthClientAPI.__GET.replace("{clientId}", clientId);
    }

    static __create_url_delete(clientId) {
        return OauthClientAPI.__DELETE.replace("{clientId}", clientId);
    }

    static __create_url_gen_secret(clientId) {
        return OauthClientAPI.__GEN_SECRET.replace("{clientId}", clientId);
    }

    @GetMapper(OauthClientModel)
    get(clientId) {
        let url = OauthClientAPI.__create_url_get(clientId);
        return axios.get(url, {
            params: {
                clientId: clientId
            }
        });
    }

    @GetMapper(PageableResponse)
    search(page, pageSize, clientId, clientName) {
        return axios.get(OauthClientAPI.__SEARCH, {
            params: {
                page: page,
                pageSize: pageSize,
                clientId: clientId,
                clientName: clientName
            }
        });
    }

    @PostMapper(OauthClientModel)
    save(dto) {
        return axios.post(OauthClientAPI.__SAVE, dto);
    }

    delete(clientId) {
        let url = OauthClientAPI.__create_url_delete(clientId);
        return axios.delete(url, {
            params: {
                clientId: clientId
            }
        });
    }

    genSecret(clientId) {
        let url = OauthClientAPI.__create_url_gen_secret(clientId);
        return axios.post(url, null);
    }
}

export default new OauthClientAPI();