package com.definityfirst.jesusgonzalez.agendaapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jesus.gonzalez on 30/01/2017.
 */

public class contactAdapter extends ArrayAdapter<Contacto> {

    private MainActivity activity;
    private List<Contacto> friendList;
    private List<Contacto> searchList;

    public contactAdapter(MainActivity context, int resource, List<Contacto> contactos) {
        super(context, resource, contactos);
        this.activity = context;
        this.friendList = contactos;
        this.searchList = new ArrayList<Contacto>();
        this.searchList.addAll(friendList);
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public  Contacto getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.contactview, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.friendName.setText(getItem(position).getName());
        holder.friendNumber.setText(getItem(position).getNumber());

        //get first letter of each String item
        String firstLetter = String.valueOf(getItem(position).getName().charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(getItem(position).getName());

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px

        holder.imageView.setImageDrawable(drawable);


        return convertView;

    }

    // Filter method
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        friendList.clear();
        if (charText.length() == 0) {
            friendList.addAll(searchList);
        } else {
            for (Contacto s : searchList) {
                if (s.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    friendList.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView friendName;
        private TextView friendNumber;

        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.ContactImage);
            friendName = (TextView) v.findViewById(R.id.ContactName);
            friendNumber = (TextView) v.findViewById(R.id.ContactNumber);
        }
    }
}
