package to.msn.wings.restaurant6;

public class SeatItem {
    String s_id;
    int s_capacity;
    int o_id;

    public String getS_id()                   { return s_id; }
    public void setS_id(String s_id)          { this.s_id = s_id; }
    public int getS_capacity()                { return s_capacity; }
    public void setS_capacity(int s_capacity) { this.s_capacity = s_capacity; }
    public int getO_id()                      { return o_id; }
    public void setO_id(int o_id)             { this.o_id = o_id; }

    @Override
    public String toString() {
        String str;
        if(o_id == -1){
            str = "タップして席を選択";
        }
        else {
            str = s_id + "席（" + s_capacity + "人用）" + (o_id == 0 ? "空き" : "食事中");
        }
        return str;
    }


}
