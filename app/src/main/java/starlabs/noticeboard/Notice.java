package starlabs.noticeboard;

/**
 * Created by Rishabh on 18-03-2018.
 */

class Notice {
    String data ;

    public Notice() {
    }

    public Notice(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
