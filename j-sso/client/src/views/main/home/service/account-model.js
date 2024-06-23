import {DATA_TYPE, JsonField, ModelExtension, TypeArr, TypeBool, TypeDate, TypeString} from "@dlabs71/d-dto";

export class UserModel extends ModelExtension {
    @JsonField("id") @TypeString id;
    @JsonField("email") @TypeString email;
    @JsonField("firstName") @TypeString firstName;
    @JsonField("lastName") @TypeString lastName;
    @JsonField("middleName") @TypeString middleName;
    @JsonField("birthday") @TypeDate() birthday;
    @JsonField("avatarFileId") @TypeString avatarFileId;
    @JsonField("registrationDate") @TypeString registrationDate;
    @JsonField("authProviders") @TypeArr(DATA_TYPE.STRING) authProviders;
    @JsonField("admin") @TypeBool admin;
}