
package com.codebee.tradethrust.model.form_details.create;

import com.google.gson.annotations.SerializedName;

public class FormDetailsCreate {

    @SerializedName("record")
    private com.codebee.tradethrust.model.form_details.create.Record Record;

    public com.codebee.tradethrust.model.form_details.create.Record getRecord() {
        return Record;
    }

    public void setRecord(com.codebee.tradethrust.model.form_details.create.Record record) {
        Record = record;
    }

}
