package to.msn.wings.restaurant6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AlreadyOrderListAdapter extends BaseAdapter {
    Context context;
    ArrayList<AlreadyListItem> data;
    int resource;
    LayoutInflater inflater;

    public AlreadyOrderListAdapter(Context context, ArrayList<AlreadyListItem> data, int resource){
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
    public AlreadyListItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getIndex();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlreadyListItem item = getItem(position);
        View sview = (convertView != null) ? convertView : inflater.inflate(resource, null);
        ((TextView)sview.findViewById(R.id.aoTxtId)).setText(item.getAli_id() == null ? "" : item.getAli_id());
        ((TextView)sview.findViewById(R.id.aoTxtName)).setText(item.getAli_name());
        ((TextView)sview.findViewById(R.id.aoTxtprice)).setText(item.getAli_price() + context.getString(R.string.txtEN));
        ((TextView)sview.findViewById(R.id.aoTxtQuantity)).setText(item.getAli_quantity() == 0 ? "" : item.getAli_quantity() + context.getString(R.string.txtKO));
        ((TextView)sview.findViewById(R.id.aoTxtNote)).setText(item.getAli_note() == null ? "" : item.getAli_note());
        ((TextView)sview.findViewById(R.id.aoTxtTime)).setText(item.getAli_time() == null ? "" : item.getAli_time());

        String stateStr = "";
        if(item.getAli_state() == 1)      stateStr = context.getString(R.string.txtOdState1);
        else if(item.getAli_state() == 2) stateStr = context.getString(R.string.txtOdState2);
        else if(item.getAli_state() == 3) stateStr = context.getString(R.string.txtOdState3);

        ((TextView)sview.findViewById(R.id.aoTxtState)).setText(stateStr);

        ((TextView)sview.findViewById(R.id.aoTxtSeatNo)).setText(item.getAli_seatNo() == null ? "" : item.getAli_seatNo() + context.getString(R.string.txtSEKI));

        return sview;
    }
}
