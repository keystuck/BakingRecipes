
//Emily Stuckey
//Udacity Android Development
//Baking App
//August 8, 2018

package com.example.emily.bakingrecipes;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.emily.bakingrecipes.RecipeDetails.Recipe;
import com.example.emily.bakingrecipes.RecipeDetails.RecipeAdapter;
import com.example.emily.bakingrecipes.UtilsAndWidget.NetworkUtilities;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenuActivity extends AppCompatActivity {

    private static final String recipesURLString = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String NOINTERNET = "no internet";
    private static final String SUCCESS = "success";

    private static final Gson gson = new Gson();

    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private TextView mErrorMessageView;
    private RecipeAdapter mRecipeAdapter;

    private String recipesListJSON;
    private ArrayList<Recipe> mRecipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mRecyclerView = findViewById(R.id.main_menu_recycler_view);
        if (findViewById(R.id.cl_tablet) == null) {
            mGridLayoutManager = new GridLayoutManager(MainMenuActivity.this, 1);
        } else {
            mGridLayoutManager = new GridLayoutManager(MainMenuActivity.this, 3);
        }
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mErrorMessageView = findViewById(R.id.tv_error_message_display);

        mRecipeAdapter = new RecipeAdapter(MainMenuActivity.this, mRecipeList);
        mRecyclerView.setAdapter(mRecipeAdapter);

        new GetRecipeListTask().execute(recipesURLString);

    }

    public void showErrorMessage(){
        mErrorMessageView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    public class GetRecipeListTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0){
                return null;
            } else {
                try {
                    int timeout = 1500;
                    Socket socket = new Socket();
                    SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                    socket.connect(socketAddress, timeout);
                    socket.close();
                } catch (IOException e){
                    e.printStackTrace();
                    return NOINTERNET;
                }

                try {
                    recipesListJSON = NetworkUtilities.getResponseFromHttpUrl(strings[0]);
                    return SUCCESS;
                } catch (Exception e){
                    e.printStackTrace();
                    return NOINTERNET;
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals(SUCCESS)) {
                mRecipeList = new ArrayList<Recipe>(Arrays.asList(gson.fromJson(recipesListJSON, Recipe[].class)));

                mErrorMessageView.setVisibility(View.INVISIBLE);
                mRecipeAdapter.clear();
                mRecipeAdapter.addAll(mRecipeList);
                mRecyclerView.setAdapter(mRecipeAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);

            } else{
                showErrorMessage();
            }
        }
    }
}
