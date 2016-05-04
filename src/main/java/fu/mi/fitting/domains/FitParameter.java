package fu.mi.fitting.domains;

/**
 * Created by shang on 5/1/2016.
 */

import javafx.beans.property.SimpleStringProperty;

public class FitParameter {

    private final SimpleStringProperty key = new SimpleStringProperty("");
    private final SimpleStringProperty value = new SimpleStringProperty("");

    public FitParameter() {
        this("", "");
    }

    public FitParameter(String key, String value) {
        setKey(key);
        setValue(value);
    }

    public String getKey() {
        return key.get();
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

}