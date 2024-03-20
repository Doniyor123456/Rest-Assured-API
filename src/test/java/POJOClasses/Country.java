package APITest.POJOClasses;

import java.util.List;

public class Country {
    private String name;
    private String shortName;
    private String id;
    private String code;
    private List<String> translateName;
    private boolean hasState;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getTranslateName() {
        return translateName;
    }

    public void setTranslateName(List<String> translateName) {
        this.translateName = translateName;
    }

    public boolean isHasState() {
        return hasState;
    }

    public void setHasState(boolean hasState) {
        this.hasState = hasState;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", translateName=" + translateName +
                ", hasState=" + hasState +
                '}';
    }
}
