package kz.nis.economykz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import kz.nis.economykz.ActivityParts;
import kz.nis.economykz.Main;
import kz.nis.economykz.R;
import kz.nis.economykz.extra.FontFactory;

public class AlbumsAdapter extends BaseAdapter implements View.OnTouchListener{


    private final Intent intent;
    private Context context;
    private String[] albumNames;
    private Typeface typeface;

    public AlbumsAdapter(Context _context, String[] albumNames, Intent intent) {
        this.context = _context;
        this.albumNames = albumNames;
        this.intent = intent;
        this.typeface = FontFactory.getFont1(context);
    }

    @Override
    public int getCount() {
        return albumNames.length;
    }

    @Override
    public String getItem(int position) {
        return albumNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_grid, parent, false);
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.imageItemBook);
Log.d(Main.LOG,"PART_CODE = "+intent.getComponent().getShortClassName());
//Log.d(Main.LOG,"intent.getComponent().getShortClassName() = "+intent.getComponent().getShortClassName());
//Log.d(Main.LOG,"intent.getComponent().getClassName() = "+intent.getComponent().getClassName());//.getShortClassName());

        if (position == getCount() - 1) {
            String partCode = intent.getStringExtra(ActivityParts.PART_CODE);
            if(partCode!=null)
            {
                if( partCode.equals(ActivityParts.PART_CODE_PLAY1) || partCode.equals(ActivityParts.PART_CODE_PLAY2)){
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
            imageView.setImageResource(R.drawable.icon_newset);
            imageView.setTag(0);

        } else {
            TextView textView = (TextView) view.findViewById(R.id.textItemAlbumName);
            textView.setText(getItem(position));
            textView.setTypeface(typeface);
            imageView.setTag(position + 1);
        }
        imageView.setOnTouchListener(this);
//       imageView.setOnTouchListener(new View.OnTouchListener() {
//           @Override
//           public boolean onTouch(View v, MotionEvent event) {
//
//               if(hasFocus){
//                   v.setScaleX(0.7f);
//                   v.setScaleY(0.7f);
//               }   else{
//                   v.setScaleX(1.0f);
//                   v.setScaleY(1.0f);
//               }
//
//               return false;
//           }
//       });
        return view;
    }

    private boolean click = false;

    @SuppressLint("NewApi")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                click = true;
                v.setScaleX(0.94f);
                v.setScaleY(0.94f);
                break;
            case MotionEvent.ACTION_MOVE:
                int left = -20;
                int right = v.getWidth() + 20;
                int top = -20;
                int bottom = v.getHeight() + 20;
                if (left >= event.getX() || event.getX() >= right || top >= event.getY() || event.getY() >= bottom) {
                    v.setScaleX(1f);
                    v.setScaleY(1f);
                    click = false;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (click) {
                        int partId = (int) v.getTag();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("partId", partId);
                        context.startActivity(intent);

                }
                v.setScaleX(1f);
                v.setScaleY(1f);
                click = false;
                break;
        }

        return true;
    }
}