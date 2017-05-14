package com.example.everyoneassist.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.everyoneassist.R;

public class EditTextActivity extends BaseActivity {

    private EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        String name = getIntent().getStringExtra("title");
        initHeader(name);

        String hint = getIntent().getStringExtra("hint");

        edittext = (EditText) this.findViewById(R.id.edittext);
        edittext.setHint("请输入");

        setRightText("确定");

        right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edittext.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("content", content);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
