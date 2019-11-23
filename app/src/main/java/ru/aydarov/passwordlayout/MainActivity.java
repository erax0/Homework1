package ru.aydarov.passwordlayout;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TextView mResultTextView;
    private EditText mSourceTextView;
    private View mCopyButtonResult;
    private View mCopyButtonGenerate;
    private ImageView mQualityImageView;
    private TextView mQualityTextView;
    private CompoundButton mUseUppercase;
    private CompoundButton mUseDigits;
    private CompoundButton mUseSymbols;
    private TextView mGenerateTextView;
    private View mButtonGenerate;
    private SeekBar mSeekBarLenght;
    private TextView mLenghtTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTextView = findViewById(R.id.text_result);
        mQualityTextView = findViewById(R.id.password_quality_text);
        mSourceTextView = findViewById(R.id.source_text);
        mGenerateTextView = findViewById(R.id.text_generate);
        mLenghtTextView = findViewById(R.id.text_length);
        mCopyButtonResult = findViewById(R.id.btn_copy_result);
        mCopyButtonGenerate = findViewById(R.id.btn_copy_generate);
        mButtonGenerate = findViewById(R.id.btn_generate);
        mQualityImageView = findViewById(R.id.image_quality_indicator);
        mUseUppercase = findViewById(R.id.check_caps);
        mUseDigits = findViewById(R.id.check_digits);
        mUseSymbols = findViewById(R.id.check_symbols);
        mSeekBarLenght = findViewById(R.id.seek_length);
        mCopyButtonGenerate.setEnabled(false);
        mCopyButtonResult.setEnabled(false);

        final PasswordsHelper mPasswordsHelper = new PasswordsHelper(getResources().getStringArray(R.array.russians),
                getResources().getStringArray(R.array.english));

        mSeekBarLenght.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int sum = progress + 8;
                String param = getResources().getQuantityString(R.plurals.symbols, progress);
                String result = getResources().getQuantityString(R.plurals.symbols, sum);
                mLenghtTextView.setText(getString(R.string.password_length_format, progress, param, sum, result));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSourceTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mResultTextView.setText(mPasswordsHelper.convert(charSequence));
                mCopyButtonResult.setEnabled(charSequence.length() > 0);
                int quality = mPasswordsHelper.getQuality(charSequence);
                mQualityImageView.setImageLevel(quality * 1000);
                mQualityTextView.setText(getResources().getStringArray(R.array.quantities)[quality]);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCopyButtonResult.setOnClickListener(view -> {
            ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            assert manager != null;
            manager.setPrimaryClip(ClipData.newPlainText(MainActivity.this.getString(R.string.clipboard_title), mResultTextView.getText()));
            Toast.makeText(MainActivity.this, R.string.message_copied, Toast.LENGTH_SHORT).show();
        });
        mCopyButtonGenerate.setOnClickListener(view -> {
            ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            assert manager != null;
            manager.setPrimaryClip(ClipData.newPlainText(MainActivity.this.getString(R.string.clipboard_title), mGenerateTextView.getText()));
            Toast.makeText(MainActivity.this, R.string.message_copied, Toast.LENGTH_SHORT).show();
        });
        mButtonGenerate.setOnClickListener(view -> {
            mGenerateTextView.setText(mPasswordsHelper.generate(mSeekBarLenght.getProgress() + 8, mUseUppercase.isChecked(), mUseDigits.isChecked(), mUseSymbols.isChecked()));
            mCopyButtonGenerate.setEnabled(mGenerateTextView.getText().length() > 0);
        });
        mUseUppercase.setOnCheckedChangeListener((buttonView, isChecked) -> Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show());
    }


}
