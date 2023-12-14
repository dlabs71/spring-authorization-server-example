import {DATA_TYPE, JsonField, TypeArr, TypeNumber, TypeString} from "@dlabs71/d-dto";

export class UserModel {
    @JsonField("id") @TypeString id;
    @JsonField("firstName") @TypeString firstName;
    @JsonField("lastName") @TypeString lastName;
    @JsonField("middleName") @TypeString middleName;
    @JsonField("birthday") @TypeString birthday;
    @JsonField("username") @TypeString username;
    @JsonField("email") @TypeString email;
    @JsonField("authorities") @TypeArr(DATA_TYPE.STRING) authorities = [];
    @JsonField("avatarUrl") @TypeString avatarUrl;
}