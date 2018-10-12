package starlabs.noticeboard;

import org.w3c.dom.Text;

/**
 * Created by Rishabh on 07-05-2018.
 */

public class Upload {
    String Name,Url,textnotice;

    public Upload() {
    }

    public Upload(String txt) {
        textnotice=txt;
    }

    public String getTextnotice() {
        return textnotice;
    }

    public void setTextnotice(String textnotice) {
        this.textnotice = textnotice;
    }

    public Upload(String name, String url) {
        Name=name;
        Url=url;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
