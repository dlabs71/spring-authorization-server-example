import axios from "axios";
import {PostMapper} from "@dlabs71/d-dto";
import {RegistrationModel} from "./models";

export class RegistrationAPI {
    static REGISTRATION_URL = "/registration/init";
    static CONFIRM_URL = "/registration/confirm";

    @PostMapper(RegistrationModel)
    sendRegistrationData(data) {
        return axios.post(RegistrationAPI.REGISTRATION_URL, data);
    }

    sendConfirm(code) {
        return axios.post(RegistrationAPI.CONFIRM_URL, null, {
            params: {
                otp: code
            }
        });
    }
}

export default new RegistrationAPI();