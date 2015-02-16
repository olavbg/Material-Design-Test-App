package materialtest.vivz.slidenerd.materialtest;

import static android.text.TextUtils.isEmpty;

public class JsonPOSTResponse {
    private String err_msg = "";
    private String success = "";

    public JsonPOSTResponse(String err_msg, String success) {
        this.err_msg = err_msg;
        this.success = success;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
    
    public boolean hasErrors(){
        return !isEmpty(err_msg);
    }
    
    public boolean isSuccess(){
        return isEmpty(err_msg);
    }
}
