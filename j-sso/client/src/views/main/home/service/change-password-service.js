import axios from "axios";

export class ChangePasswordAPI {

    static __INIT_URL = "/change-password/init";
    static __CONFIRM_URL = "/change-password/confirm";

    init(password) {
        const form = new FormData();
        form.append('password', new Blob([password], {
            type: "text/plain"
        }));
        return axios.post(ChangePasswordAPI.__INIT_URL, form);
    }

    confirm(otp) {
        const form = new FormData();
        form.append('otp', new Blob([otp], {
            type: "text/plain"
        }));
        return axios.post(ChangePasswordAPI.__CONFIRM_URL, form);
    }
}


export default new ChangePasswordAPI();