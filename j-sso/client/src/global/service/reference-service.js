import {JsonField, StorableGetMapper, TypeString} from "@dlabs71/d-dto";
import axios from "axios";
import store from "@/store";

export class ReferenceModel {
    @JsonField("value") @TypeString value;
    @JsonField("title") @TypeString title;
}

class ReferenceAPI {

    static __AUTH_METHODS = "/reference/auth-methods";
    static __GRANT_TYPES = "/reference/grant-types";
    static __SCOPES = "/reference/scopes";

    @StorableGetMapper(
        ReferenceModel,
        null,
        (data) => {
            store.dispatch("setAuthMethods", data);
        },
        () => {
            return store.getters.getAuthMethods;
        }
    )
    getAuthMethods() {
        return axios.get(ReferenceAPI.__AUTH_METHODS);
    }

    @StorableGetMapper(
        ReferenceModel,
        null,
        (data) => {
            store.dispatch("setGrantTypes", data);
        },
        () => {
            return store.getters.getGrantTypes;
        }
    )
    getGrantTypes() {
        return axios.get(ReferenceAPI.__GRANT_TYPES);
    }

    @StorableGetMapper(
        ReferenceModel,
        null,
        (data) => {
            store.dispatch("setScopes", data);
        },
        () => {
            return store.getters.getScopes;
        }
    )
    getScopes() {
        return axios.get(ReferenceAPI.__SCOPES);
    }
}

export default new ReferenceAPI();