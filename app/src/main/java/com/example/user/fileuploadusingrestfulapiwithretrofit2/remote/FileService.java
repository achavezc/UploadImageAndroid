package com.example.user.fileuploadusingrestfulapiwithretrofit2.remote;

import com.example.user.fileuploadusingrestfulapiwithretrofit2.model.FileInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileService {
    /*
    @Multipart
    @POST("products")
    Call<FileInfo> products(@Part MultipartBody.Part file);

    @Multipart
    @POST("products")
    Call<FileInfo> products ( @Part MultipartBody.Part file );
 */
    @Multipart
    @POST("products")
    Call<ResponseBody> postFile (@Part("productImage\"; filename=\"pp.png\" ") RequestBody file , @Part("name") RequestBody fname);//(@Part MultipartBody.Part image, @Part("name") RequestBody name);


}
