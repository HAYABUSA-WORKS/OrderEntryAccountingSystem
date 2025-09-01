package to.msn.wings.restaurant6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ListItem> data;
    int resource;
    LayoutInflater inflater;

    public OrderListAdapter(Context context, ArrayList<ListItem> data, int resource){
        this.context = context;
        this.data = data;
        this.resource = resource;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ListItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getIndex();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem item = getItem(position);
        View sview = (convertView != null) ? convertView : inflater.inflate(resource, null);
        ((TextView)sview.findViewById(R.id.oTxtId)).setText(item.getLi_id());
        ((TextView)sview.findViewById(R.id.oTxtName)).setText(item.getLi_name());
        ((TextView)sview.findViewById(R.id.oTxtprice)).setText(item.getLi_price() + context.getString(R.string.txtEN));
        ((TextView)sview.findViewById(R.id.oTxtQuantity)).setText(item.getLi_quantity() + context.getString(R.string.txtKO));
        ((TextView)sview.findViewById(R.id.oTxtNote)).setText(item.getLi_note());

        return sview;
    }
}
