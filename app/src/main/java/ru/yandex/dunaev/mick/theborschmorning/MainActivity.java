package ru.yandex.dunaev.mick.theborschmorning;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.util.VKJsonHelper;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VKSdk.login(this,VKScope.WALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался
                Log.v("VK","Пользователь авторизовался");

                VKRequest request = new VKRequest("wall.search",VKParameters.from(
                        "domain","publik_borsch",
                        "query","#borschутречко",
                        "count","100"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        Log.v("VKResponse","Данные получены");
                        JSONObject jsonObject = response.json;

                        final List<PhotoImage> photoImages = PhotoHelperGson.parseJSON(jsonObject);

                        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler);

                        recyclerView.setAdapter(new RecyclerView.Adapter() {
                            @NonNull
                            @Override
                            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                CardView cv = (CardView)LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_card,viewGroup,false);
                                return new RecyclerView.ViewHolder(cv) {};
                            }

                            @Override
                            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                                CardView cv = (CardView)viewHolder.itemView;
                                cv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this,PhotoViewActivity.class);
                                        intent.putExtra(PhotoViewActivity.EXTRA_PHOTO_URL,photoImages.get(i).getFullImage());
                                        startActivity(intent);
                                    }
                                });

                                ImageView imageView = (ImageView)cv.findViewById(R.id.photoImage);
                                Picasso.get().load(photoImages.get(i).getIconImage()).into(imageView);

                            }

                            @Override
                            public int getItemCount() {
                                return photoImages.size();
                            }
                        });

                        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                    }
                });

            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
