package com.example.android.warehouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

// TODO add cache
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    RadioGroup mStorageOptionsRadioGroup;
    Button mSaveButton;
    Button mLoadButton;
    TextView mOutputTextView;
    EditText mInputEditText;

    private FileIO mFileIO;

    private int mStorageOption;
    public static final int INTERNAL_STORAGE=1;
    public static final int EXTERNAL_STORAGE=2;

    // TODO spinner with available files check external and internal directories+show info about files (dir,create and etc dates)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSaveButton= (Button) findViewById(R.id.btnSave);
        mLoadButton= (Button) findViewById(R.id.btnLoad);
        mStorageOptionsRadioGroup= (RadioGroup) findViewById(R.id.rgrpStorageOptions);
        mOutputTextView= (TextView) findViewById(R.id.textOutput);
        mInputEditText = (EditText) findViewById(R.id.editText);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mInputEditText.getText().toString();
                mFileIO.writeInternal("data.txt",input);
            }
        });
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOutputTextView.setText(mFileIO.readInternal("data.txt"));
            }
        });
        // by default save and load from external
//        mStorageOption=INTERNAL_STORAGE;

        mFileIO=FileIO.getInstance(this);

        Log.d(TAG,"is external storage writable? "+mFileIO.isExternalStorageWritable());
        Log.d(TAG,"available external memory: "+mFileIO.getAvailableExternalMemory());

        for(String fileName : mFileIO.getInternalFiles()){
            Log.d(TAG,fileName);
        }
        for(String fileName : mFileIO.getAlbumFiles()){
            Log.d(TAG,fileName);
        }
    }
}
