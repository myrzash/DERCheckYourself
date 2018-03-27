package kz.nis.economyru;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import kz.nis.economyru.adapter.AlbumsAdapter;
import kz.nis.economyru.db.DBAdapter;
import kz.nis.economyru.play.ActivityGameOne;
import kz.nis.economyru.play.ActivityGameTwo;

public class ActivityParts extends Activity {
    public static final String PART_CODE = "partcode";
    public static final String PART_CODE_PLAY1 = "play1";
    public static final String PART_CODE_PLAY2 = "play2";
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        openDB();
        String[] partNames = getPartNames(dbAdapter.getParts());
        String from = getIntent().getStringExtra("from");
        Intent intent;
        if (PART_CODE_PLAY1.equals(from)) {
            intent = new Intent(ActivityParts.this, ActivityGameOne.class);
            intent.putExtra(PART_CODE, PART_CODE_PLAY1);
        } else if (PART_CODE_PLAY2.equals(from)) {
            intent = new Intent(ActivityParts.this, ActivityGameTwo.class);
            intent.putExtra(PART_CODE, PART_CODE_PLAY2);
        } else {
            intent = new Intent(ActivityParts.this, ActivityList.class);
        }
        AlbumsAdapter gridAdapter = new AlbumsAdapter(getApplicationContext(), partNames, intent);
        gridView.setAdapter(gridAdapter);
    }


    private void openDB() {
        dbAdapter = new DBAdapter(getApplicationContext());
        try {
            dbAdapter.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getPartNames(Cursor cursor) {
        String[] partNames = new String[cursor.getCount() + 1];
        short i = 0;
        if (cursor.moveToFirst()) {
            do {
                partNames[i++] = cursor.getString(cursor.getColumnIndex(DBAdapter.ATTR_PART_NAME));
            } while (cursor.moveToNext());
        }
        return partNames;
    }

    public void onClickBack(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityParts.this, Main.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        if (dbAdapter != null) dbAdapter.close();
        super.onDestroy();
    }
}
