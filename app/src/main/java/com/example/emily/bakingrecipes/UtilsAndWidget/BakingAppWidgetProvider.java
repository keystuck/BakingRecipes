package com.example.emily.bakingrecipes.UtilsAndWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.emily.bakingrecipes.MainMenuActivity;
import com.example.emily.bakingrecipes.R;
import com.example.emily.bakingrecipes.RecipeDetails.Ingredient;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    public static void updateFromRecipe(Context context, String name, ArrayList<Ingredient> ingredientArrayList){
        StringBuilder stringBuilder = new StringBuilder();
        for (Ingredient ingredient : ingredientArrayList){
            stringBuilder.append(ingredient.toString());
            stringBuilder.append("\n");
        }
        String finalList = stringBuilder.toString();
        ComponentName componentName = new ComponentName(context, BakingAppWidgetProvider.class);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(componentName);
        for (int i = 0; i < ids.length; i++){
            updateAppWidget(context, AppWidgetManager.getInstance(context), ids[i], name, finalList);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, String ingredientsList) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);

        //open app
        Intent intent = new Intent(context, MainMenuActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.layout_widget, pendingIntent);

        if (!recipeName.isEmpty()){
            views.setTextViewText(R.id.appwidget_current_recipe_name, recipeName);
        }
        if (!ingredientsList.isEmpty()){
            views.setTextViewText(R.id.appwidget_ingredients_list, ingredientsList);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, "", "");
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

