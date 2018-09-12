package com.example.user.fileuploadusingrestfulapiwithretrofit2;

import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.fileuploadusingrestfulapiwithretrofit2.model.FileInfo;
import com.example.user.fileuploadusingrestfulapiwithretrofit2.remote.APIUtils;
import com.example.user.fileuploadusingrestfulapiwithretrofit2.remote.FileService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
//import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    FileService fileService;
    Button btnChooseFile;
    Button btnUpload;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        */
        btnChooseFile = (Button) findViewById(R.id.btnChooseFile);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        fileService = APIUtils.getFileService();

        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new RetrieveFeedTask().execute("");
                        /*

                        File file = new File(imagePath);
                        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                        //MultipartBody.Part body = MultipartBody.Part.createFormData("productImage", file.getName(), reqFile);
                        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "productImage");

                        Call<ResponseBody> call = fileService.postFile(reqFile, name);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "ERROR:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    */
            }
        });
    }
    class RetrieveFeedTask extends AsyncTask<String, Void, Response> {

        protected Response doInBackground(String... urls) {

            OkHttpClient client = new OkHttpClient();
            File file = new File(imagePath);
            MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("productImage", imagePath, RequestBody.create(MEDIA_TYPE_JPEG, file))
                    .addFormDataPart("name", "productImage")
                    .addFormDataPart("parametro1", "demoAndroid1")
                    .addFormDataPart("parametro2", "demoAndroid2")
                    .addFormDataPart("parametro3", "demoAndroid3")
                    .build();


           // RequestBody body = RequestBody.create(mediaType, file);
            Request request = new Request.Builder()
                    .url("http://54.214.60.111:9000/products")
                    .header("Accept", "application/json")
                    .header("Content-Type", "multipart/form-data")
                    .post(body).build();

            try {
                Response response = client.newCall(request).execute();
                /*
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "ERROR:" , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                }
                */
                return response;

            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "ERROR:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            }

        }

        protected void onPostExecute(Response response) {
            // TODO: check this.exception
            if (!response.isSuccessful()){
                Toast.makeText(MainActivity.this, "ERROR:" , Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            }

            // TODO: do something with the feed
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
                if(data == null){
                    Toast.makeText(this,"Unable to choose image!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri imageUri = data.getData();
                imagePath = getRealPathFromUri(imageUri);

        }
    }

    private String getRealPathFromUri(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}
