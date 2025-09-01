package to.msn.wings.restaurant6;

public class FoodItem {
    int index;
    String f_id;
    String f_name;
    int f_price;

    public int getIndex()                { return index; }
    public void setIndex(int index)      { this.index = index; }
    public String getF_id()              { return f_id; }
    public void setF_id(String f_id)     { this.f_id = f_id; }
    public String getF_name()            { return f_name; }
    public void setF_name(String f_name) { this.f_name = f_name; }
    public int getF_price()              { return f_price; }
    public void setF_price(int f_price)  { this.f_price = f_price; }

    @Override
    public String toString() {
        return f_id + " " + f_name + " " + f_price + "円";
    }
}
