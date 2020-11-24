package com.samuilolegovich.vedroidchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.auth.AuthUI;




public class MainActivity extends AppCompatActivity {
    private FirebaseListAdapter<Message> adapter;
    private RelativeLayout mainRelativeLayout;
    private FloatingActionButton sendButton;
    private static int SING_IN_CODE = 1;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SING_IN_CODE) {
            if (requestCode == RESULT_OK) {
                Snackbar.make(mainRelativeLayout, "Вы успешно зарегистрированы :)",
                        Snackbar.LENGTH_LONG).show();
                displayAllMessage();
            } else {
                Snackbar.make(mainRelativeLayout, "Вы не зарегистрированы :(",
                        Snackbar.LENGTH_LONG).show();
                finish();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainRelativeLayout = (RelativeLayout) findViewById(R.id.main_relative_layout);
        sendButton = (FloatingActionButton)findViewById(R.id.button_send_message);
        // привязываем к кнопке событие
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField = findViewById(R.id.input_text_field);
                if (textField.getText().toString().equals("")) return;
                FirebaseDatabase.getInstance().getReference().push().setValue(
                        new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                textField.getText().toString())
                );
                textField.setText("");
            }
        });


        /**
         * проверяем авторизован ли пользователь
         * FirebaseAuth - надо добавить иначе не сможем пользоваться, сам он не импортируется
         * в build.gradle вписываем --> maven {url 'https://maven.google.com'}
         * и в другой build.gradle вписываем -->
         *     implementation 'com.google.firebase:firebase-core:16.0.1'
         *     implementation 'com.google.firebase:firebase-auth:16.0.1'
         *     implementation 'com.google.firebase:firebase-database:16.0.1'
         */
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            /**
             * класс - AuthUI надо импортировать, для этого в build.gradle прописываем -->
             *     implementation 'com.firebaseui:firebase-ui:0.6.2'
             */
            // пользователь не авторизован, проводим авторизацию
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SING_IN_CODE);
        } else {
            Snackbar.make(mainRelativeLayout, "Добро пожаловать :)", Snackbar.LENGTH_LONG).show();
        }
        displayAllMessage();
    }



    private void displayAllMessage() {
        // готовим куда выводить список
        ListView listOfMessage = findViewById(R.id.list_of_message);
        // добавляем данные(лист) в адаптор
        // классв котором выводим и класс чего выводим
        adapter = new FirebaseListAdapter<Message>(this, Message.class,
                // стили вывода и подключение к базе данных
                R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                // list_item - тут у нас прописано три типа вывод, вот их мы и создаем и задаем данные
                // v. - говорит что мы работак=ем внутри окна
                TextView nameUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);
                TextView messageText = v.findViewById(R.id.message_text);

                nameUser.setText(model.getUserName());
                messageText.setText(model.getTextMessage());
                messageTime.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getMessageTime()));

            }
        };
        listOfMessage.setAdapter(adapter);
    }
}