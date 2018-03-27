package kz.nis.economykz;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import kz.nis.economykz.adapter.ListAdapter;
import kz.nis.economykz.R;
import kz.nis.economykz.adapter.FileAdapter;
import kz.nis.economykz.db.DBAdapter;
import kz.nis.economykz.extra.FontFactory;

public class ActivityList extends ListActivity implements View.OnFocusChangeListener {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    public static DBAdapter dbAdapter;
    private static int partId;
    private ListAdapter adapter;
    private ImageView imageViewCover;
    private EditText editTextPartName;
    private EditText editTextAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_list);
        partId = getIntent().getIntExtra("partId", 1);
        openDB();

        Typeface typeface = FontFactory.getFont1(this);

        editTextPartName = (EditText) findViewById(R.id.editTextPartName);
        editTextPartName.setTypeface(typeface);
        editTextAuthor = (EditText) findViewById(R.id.editTextAuthor);
        editTextAuthor.setTypeface(typeface);
        imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
        editTextPartName.setOnFocusChangeListener(this);
        editTextAuthor.setOnFocusChangeListener(this);
        if (partId == 0) {
            dbAdapter.insertPart();
            partId = dbAdapter.getParts().getCount();
            dbAdapter.insertRecord(partId);
            Log.d(Main.LOG, "part: " + partId);
        }
        fillList();
        getPartDatas();

        TextView back = (TextView) findViewById(R.id.textBack);
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ActivityList.this, ActivityParts.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        ((TextView) findViewById(R.id.textTitle)).setTypeface(typeface);
        ((TextView) findViewById(R.id.textAuthor)).setTypeface(typeface);
        ((TextView) findViewById(R.id.textCover)).setTypeface(typeface);
    }

    private void fillList() {
        Cursor cursor = dbAdapter.getAllRecords(partId);
        ArrayList<String> questions = new ArrayList<String>();
        ArrayList<String> answers = new ArrayList<String>();
        ArrayList<Boolean> actives = new ArrayList<Boolean>();
        if (cursor.moveToLast()) {
            do {
                questions.add(cursor.getString(cursor.getColumnIndex(DBAdapter.ATTR_QUESTION)));
                answers.add(cursor.getString(cursor.getColumnIndex(DBAdapter.ATTR_ANSWER)));
                actives.add(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DBAdapter.ATTR_ACTIV))));
//                Log.d(Main.LOG, "part: " + cursor.getInt(cursor.getColumnIndex(DBAdapter.ATTR_PART_INDEX)));
            } while (cursor.moveToPrevious());
        }
        adapter = new ListAdapter(getApplicationContext(), questions, answers, actives);
        getListView().setAdapter(adapter);
    }

    private void openDB() {
        dbAdapter = new DBAdapter(getApplicationContext());
        try {
            dbAdapter.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickAdd(View v) {

        dbAdapter.insertRecord(partId);
        int size = adapter.getQuestions().size() - 1;
        adapter.getQuestions().subList(0, size);
        adapter.getQuestions().add(0, "");
        adapter.getAnswers().subList(0, size);
        adapter.getAnswers().add(0, "");
        adapter.getActivies().subList(0, size);
        adapter.getActivies().add(0, true);
        adapter.notifyDataSetChanged();
    }

    private void getPartDatas() {
        Cursor c = dbAdapter.getPart(partId);
        if (c.moveToFirst()) {
            String coverIconName = c.getString(c.getColumnIndex(DBAdapter.ATTR_PART_COVER));
            if (coverIconName != null) {
                imageViewCover.setImageBitmap(FileAdapter.getImageFromStorage(getApplicationContext(), "photo" + partId + ".png"));
            }
            editTextPartName.setText(c.getString(c.getColumnIndex(DBAdapter.ATTR_PART_NAME)));
            editTextAuthor.setText(c.getString(c.getColumnIndex(DBAdapter.ATTR_PART_AUTHOR)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        changeCoverIcon(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                    break;

                    case REQUEST_CODE_CAMERA:
                        if (resultCode == RESULT_OK) {
                            Bundle bndl = data.getExtras();
                            if (bndl != null) {
                                Object obj = data.getExtras().get("data");
                                if (obj instanceof Bitmap) {
                                    Bitmap selectedImage = (Bitmap) obj;
                                    changeCoverIcon(selectedImage);
                                }
                            }

                        }
                        break;
                }
        }

    private void changeCoverIcon(Bitmap selectedImage) {
        imageViewCover.setImageBitmap(selectedImage);
        FileAdapter.saveToInternalStorage(getApplicationContext(), partId, selectedImage);
        dbAdapter.updatePartCover(partId);
    }

    public void onClickPickImage(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    public void onClickCamera(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onDestroy() {
        if (dbAdapter != null) dbAdapter.close();
        super.onDestroy();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            String text = ((EditText) v).getText().toString();
            switch (v.getId()) {
                case R.id.editTextPartName:
                    dbAdapter.updatePartName(partId, text);
                    break;
                case R.id.editTextAuthor:
                    dbAdapter.updateAuthorName(partId, text);
                    break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityList.this, ActivityParts.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
