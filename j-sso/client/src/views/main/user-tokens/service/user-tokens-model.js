import {DATA_TYPE, JsonField, TypeArr, TypeString} from "@dlabs71/d-dto";

export class UserTokensModel {

    @JsonField("authorizationId") @TypeString authorizationId;
    @JsonField("startDate") @TypeString startDate;
    @JsonField("lastRefreshDate") @TypeString lastRefreshDate;
    @JsonField("clientId") @TypeString clientId;
    @JsonField("clientName") @TypeString clientName;
    @JsonField("scopeNames") @TypeArr(DATA_TYPE.STRING) scopes;
    @JsonField("grantTypeName") @TypeString grantType;
    @JsonField("clientRedirectUri") @TypeString clientRedirectUri;
}