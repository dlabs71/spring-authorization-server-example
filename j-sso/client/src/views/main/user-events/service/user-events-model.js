import {DATA_TYPE, JsonField, TypeArr, TypeBool, TypeNumber, TypeString} from "@dlabs71/d-dto";

export class UserEventsModel {
    @JsonField("id") @TypeString id;
    @JsonField("eventType") @TypeString eventType;
    @JsonField("eventTypeName") @TypeString eventTypeName;
    @JsonField("ipAddress") @TypeString ipAddress;
    @JsonField("clientId") @TypeString clientId;
    @JsonField("browser") @TypeString browser;
    @JsonField("device") @TypeString device;
    @JsonField("os") @TypeString os;
    @JsonField("creationDate") @TypeString creationDate;
}

export class PageableResponse {
    @JsonField("data") @TypeArr(DATA_TYPE.CUSTOM, UserEventsModel) data;
    @JsonField("moreDataExists") @TypeBool moreDataExists;
    @JsonField("total") @TypeNumber total;
}