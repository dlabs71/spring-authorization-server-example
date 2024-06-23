import {DATA_TYPE, JsonField, TypeArr, TypeBool, TypeDate, TypeNumber, TypeString} from "@dlabs71/d-dto";

export class AdminUserModel {
    @JsonField("id") @TypeString id;
    @JsonField("email") @TypeString email;
    @JsonField("firstName") @TypeString firstName;
    @JsonField("lastName") @TypeString lastName;
    @JsonField("middleName") @TypeString middleName;
    @JsonField("birthday") @TypeDate() birthday;
    @JsonField("registrationDate") @TypeString registrationDate;
    @JsonField("authProviders") @TypeArr(DATA_TYPE.STRING) authProviders;
    @JsonField("superuser") @TypeBool superuser;

    afterJ2cMapping(jsonObj, dtoModel) {
        this.userFullName = `${this.lastName} ${this.firstName} ${this.middleName || ""}`.trim();
    }
}

export class PageableResponse {
    @JsonField("data") @TypeArr(DATA_TYPE.CUSTOM, AdminUserModel) data;
    @JsonField("moreDataExists") @TypeBool moreDataExists;
    @JsonField("total") @TypeNumber total;
}