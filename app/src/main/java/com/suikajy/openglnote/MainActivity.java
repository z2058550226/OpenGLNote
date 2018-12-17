package com.suikajy.openglnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    enum Demo {
        AIR_HOCKEY("air hockey", com.suikajy.openglnote.chapter2_3.AirHockeyActivity.class),
        AIR_HOCKEY2("air hockey2", com.suikajy.openglnote.chapter4.AirHockeyActivity.class);

        private final String demoName;
        private final Class<? extends Activity> activityClz;

        Demo(String demoName, Class<? extends Activity> activityClz) {
            this.demoName = demoName;
            this.activityClz = activityClz;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = findViewById(R.id.lv_demo);
        lv.setAdapter(new Adapter());
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Demo.values().length;
        }

        @Override
        public Object getItem(int position) {
            return Demo.values()[position];
        }

        @Override
        public long getItemId(int position) {
            return ((Demo) getItem(position)).ordinal();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Demo item = (Demo) getItem(position);
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(item.demoName);
            textView.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, item.activityClz)));
            return convertView;
        }
    }
}
