import {j2cMapper, JsonField, TypeDate, TypeString} from "@dlabs71/d-dto";

export class RegistrationModel {
    @JsonField("email") @TypeString email;
    @JsonField("firstName") @TypeString firstName;
    @JsonField("secondName") @TypeString secondName;
    @JsonField("middleName") @TypeString middleName;
    @JsonField("birthday") @TypeDate() birthday;
    @JsonField("password") @TypeString password;

    static build(jsonData) {
        if (jsonData !== null) {
            return j2cMapper(jsonData, RegistrationModel, true);
        }
        return null;
    }
}