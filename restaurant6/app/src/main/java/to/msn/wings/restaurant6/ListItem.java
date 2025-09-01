package to.msn.wings.restaurant6;

public class ListItem {
    int index;
    String li_id;
    String li_name;
    int li_price;
    int li_quantity;
    String li_note;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLi_id() {
        return li_id;
    }

    public void setLi_id(String li_id) {
        this.li_id = li_id;
    }

    public String getLi_name() {
        return li_name;
    }

    public void setLi_name(String li_name) {
        this.li_name = li_name;
    }

    public int getLi_quantity() {
        return li_quantity;
    }

    public void setLi_quantity(int li_quantity) {
        this.li_quantity = li_quantity;
    }

    public int getLi_price() {
        return li_price;
    }

    public void setLi_price(int li_price) {
        this.li_price = li_price;
    }

    public String getLi_note() {
        return li_note;
    }

    public void setLi_note(String li_note) {
        this.li_note = li_note;
    }
}
