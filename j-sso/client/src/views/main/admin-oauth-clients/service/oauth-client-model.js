import {BusinessDto, DATA_TYPE, JsonField, TypeArr, TypeBool, TypeDate, TypeNumber, TypeString} from "@dlabs71/d-dto";

export class OauthClientModel extends BusinessDto {
    @JsonField("clientId") @TypeString clientId;
    @JsonField("clientSecret") @TypeString clientSecret;
    @JsonField("clientSecretExpiresAt") @TypeDate() clientSecretExpiresAt;
    @JsonField("clientName") @TypeString clientName;
    @JsonField("clientAuthenticationMethods") @TypeArr(DATA_TYPE.STRING) clientAuthenticationMethods;
    @JsonField("authorizationGrantTypes") @TypeArr(DATA_TYPE.STRING) authorizationGrantTypes;
    @JsonField("redirectUris") @TypeArr(DATA_TYPE.STRING) redirectUris;
    @JsonField("scopes") @TypeArr(DATA_TYPE.STRING) scopes;
    @JsonField("deleteNotifyUris") @TypeArr(DATA_TYPE.STRING) deleteNotifyUris;
}

export class PageableResponse {
    @JsonField("data") @TypeArr(DATA_TYPE.CUSTOM, OauthClientModel) data;
    @JsonField("moreDataExists") @TypeBool moreDataExists;
    @JsonField("total") @TypeNumber total;
}