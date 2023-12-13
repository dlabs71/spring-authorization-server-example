import axios from "axios";

export class ResetPasswordAPI {
    static INIT_URL = "/reset-password/init";
    static CONFIRM_URL = "/reset-password/confirm";
    static SET_URL = "/reset-password/set";

    initReset(email) {
        let data = new FormData();
        data.append("email", email);
        return axios.post(ResetPasswordAPI.INIT_URL, data);
    }

    confirmReset(otp) {
        let data = new FormData();
        data.append("otp", otp);
        return axios.post(ResetPasswordAPI.CONFIRM_URL, data);
    }

    setNewPassword(newPassword, resetSessionId) {
        let data = new FormData();
        data.append("password", newPassword);
        return axios.post(ResetPasswordAPI.SET_URL, data, {
            headers: {
                'reset-password-session': resetSessionId
            }
        });
    }
}

export default new ResetPasswordAPI();