package hoppers.com.tamir.hoopers.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bluetooth.BluetoothConnectionHandler;
import hoppers.com.tamir.hoopers.R;

/**
 * Created by Tamir on 26/02/2018.
 * A list adapter for bluetooth devices with hoppers app.
 */

public class HoppersDevicesAdapter extends ArrayAdapter<BluetoothDevice> implements View.OnClickListener {
    private LayoutInflater inflater;
    private IOnHopperClicked hopperClickedListener;

    void setHopperClickedListener(IOnHopperClicked hopperClickedListener){
        this.hopperClickedListener = hopperClickedListener;
    }

    HoppersDevicesAdapter(@NonNull Activity context) {
        super(context, R.layout.bluetooth_hopper_item);
        inflater = context.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return BluetoothConnectionHandler.getInstance().getDevicesDiscovered().size();
    }

    @Nullable
    @Override
    public BluetoothDevice getItem(int position) {
        return BluetoothConnectionHandler.getInstance().getDevicesDiscovered().get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // If the row did not exist
        if(convertView == null){
            // Inflating it
            convertView = inflater.inflate(R.layout.bluetooth_hopper_item, null, true);
        }

        BluetoothDevice device = getItem(position);

        if(device != null) {
            LinearLayout mainLayout = convertView.findViewById(R.id.hopper_item_main_layout);
            mainLayout.setTag(device);
            TextView txtName = convertView.findViewById(R.id.hopper_name_text);
            txtName.setText(device.getName());
            txtName.setTag(device);
            ImageView frogImage = convertView.findViewById(R.id.hopper_image);
            frogImage.setTag(device);

            mainLayout.setOnClickListener(this);
            txtName.setOnClickListener(this);
            frogImage.setOnClickListener(this);
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        if(hopperClickedListener != null){
            hopperClickedListener.onHopperClicked((BluetoothDevice) v.getTag());
        }
    }
}
