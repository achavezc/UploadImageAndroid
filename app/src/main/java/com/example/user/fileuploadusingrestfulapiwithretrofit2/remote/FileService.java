package com.example.user.fileuploadusingrestfulapiwithretrofit2.remote;

import com.example.user.fileuploadusingrestfulapiwithretrofit2.model.FileInfo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileService {

    @Multipart
    @POST("products")
    Call<FileInfo> upload(@Part MultipartBody.Part file);
}
