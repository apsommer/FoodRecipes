package com.sommerengineering.foodrecpies.reqeusts;

import com.sommerengineering.foodrecpies.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton pattern for api instance.
 */
public class ServiceGenerator {

    // build retrofit instance
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // associate it to endpoint interface
    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    // expose singleton
    public static RecipeApi getRecipeApi() {
        return recipeApi;
    }
}
