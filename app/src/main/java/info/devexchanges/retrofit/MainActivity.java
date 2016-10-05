package info.devexchanges.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<StackOverflowQuestions> {

    private ListView listView;
    private ArrayList<Question> questions;
    private ArrayAdapter<Question> arrayAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        questions = new ArrayList<>();

        //init list view adapter and attach
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, questions);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2.0
        StackOverflowAPI stackOverflowAPI = retrofit.create(StackOverflowAPI.class);

        Call<StackOverflowQuestions> call = stackOverflowAPI.loadQuestions("android");
        //asynchronous call
        call.enqueue(this);

        return true;
    }

    @Override
    public void onResponse(Call<StackOverflowQuestions> call, Response<StackOverflowQuestions> response) {
        questions.clear();

        //addind data to array list
        questions.addAll(response.body().items);

        //update list view
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<StackOverflowQuestions> call, Throwable throwable) {
        Toast.makeText(MainActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}