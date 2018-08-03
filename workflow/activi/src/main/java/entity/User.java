package entity;

import java.io.Serializable;

/**
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/8/1
 * @since : 1.5
 */
public class User implements Serializable {
    private static final long serialVersionUID = -3363120264501521428L;
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
