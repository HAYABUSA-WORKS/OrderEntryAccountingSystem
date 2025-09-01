package to.msn.wings.restaurant6;

import java.io.Serializable;

public class AlreadyListItem implements Serializable {
    int index;
    String ali_id;
    String ali_name;
    int ali_price;
    int ali_quantity;
    String ali_note;
    int ali_state;
    String ali_time;
    String ali_seatNo;
    int ali_order_id;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAli_id() {
        return ali_id;
    }

    public void setAli_id(String ali_id) {
        this.ali_id = ali_id;
    }

    public String getAli_name() {
        return ali_name;
    }

    public void setAli_name(String ali_name) {
        this.ali_name = ali_name;
    }

    public int getAli_price() {
        return ali_price;
    }

    public void setAli_price(int ali_price) {
        this.ali_price = ali_price;
    }

    public int getAli_quantity() {
        return ali_quantity;
    }

    public void setAli_quantity(int ali_quantity) {
        this.ali_quantity = ali_quantity;
    }

    public String getAli_note() {
        return ali_note;
    }

    public void setAli_note(String ali_note) {
        this.ali_note = ali_note;
    }

    public int getAli_state() {
        return ali_state;
    }

    public void setAli_state(int ali_state) {
        this.ali_state = ali_state;
    }

    public String getAli_time() {
        return ali_time;
    }

    public void setAli_time(String ali_time) {
        this.ali_time = ali_time;
    }

    public String getAli_seatNo() { return ali_seatNo; }

    public void setAli_seatNo(String ali_seatNo) { this.ali_seatNo = ali_seatNo; }

    public int getAli_order_id() { return ali_order_id; }

    public void setAli_order_id(int ali_order_id) { this.ali_order_id = ali_order_id; }
}
