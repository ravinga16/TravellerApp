package com.example.mytravellerapp.model.rest;

import com.example.mytravellerapp.model.entities.request.CommentRequest;
import com.example.mytravellerapp.model.entities.request.ForgotPasswordRequest;
import com.example.mytravellerapp.model.entities.request.InquireRequest;
import com.example.mytravellerapp.model.entities.request.LikeRequest;
import com.example.mytravellerapp.model.entities.request.LoginRequest;
import com.example.mytravellerapp.model.entities.request.RegisterFcmTokenRequest;
import com.example.mytravellerapp.model.entities.request.RegistrationRequest;
import com.example.mytravellerapp.model.entities.request.ResetPasswordRequest;
import com.example.mytravellerapp.model.entities.request.UpdateMyDetailRequest;
import com.example.mytravellerapp.model.entities.request.UpdateReadStatusRequest;
import com.example.mytravellerapp.model.entities.response.AddCommentResponse;
import com.example.mytravellerapp.model.entities.response.AddInquireResponse;
import com.example.mytravellerapp.model.entities.response.BaseServerResponse;
import com.example.mytravellerapp.model.entities.response.DoLikeResponse;
import com.example.mytravellerapp.model.entities.response.GetCommentsResponse;
import com.example.mytravellerapp.model.entities.response.GetPackageResponse;
import com.example.mytravellerapp.model.entities.response.GetTourGalleryResponse;
import com.example.mytravellerapp.model.entities.response.HomeInfoResponse;
import com.example.mytravellerapp.model.entities.response.LogOutResponse;
import com.example.mytravellerapp.model.entities.response.LoginResponse;
import com.example.mytravellerapp.model.entities.response.NotificationCountResponse;
import com.example.mytravellerapp.model.entities.response.NotificationResponse;
import com.example.mytravellerapp.model.entities.response.ProfileResponse;
import com.example.mytravellerapp.model.entities.response.ProfileUpdateResponse;
import com.example.mytravellerapp.model.entities.response.RegisterFcmResponse;
import com.example.mytravellerapp.model.entities.response.RegisterResponse;
import com.example.mytravellerapp.model.entities.response.TourInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourMapDetailsResponse;
import com.example.mytravellerapp.model.entities.response.UpdateReadStatusResponse;
import com.example.mytravellerapp.model.entities.response.UploadProfileImageResponse;
import com.example.mytravellerapp.model.entities.response.UploadTourImagesResponse;
import com.example.mytravellerapp.model.entities.response.ValidateResetCodeResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface BMSAPI {
    @POST("/user/signup")
    Single<RegisterResponse> doRegisterAPI(
            @Body RegistrationRequest registrationRequest);

    @POST("/user/login")
    Single<LoginResponse> doLoginAPI(
            @Body LoginRequest loginRequest);

    @POST("/user/forgot")
    Single<BaseServerResponse> doForgotPasswordAPI(
            @Body ForgotPasswordRequest forgotPasswordRequest);

    @POST("/fcm/registerToken")
    Single<RegisterFcmResponse> doRegisterFcmAPI(
            @Header("Authorization") String accessToken,
            @Body RegisterFcmTokenRequest registerFcmTokenRequest);


    @POST("/user/resetPassword")
    Single<BaseServerResponse> doResetPasswordAPI(
            @Body ResetPasswordRequest resetPasswordRequest);

    @POST("/user/validateResetCode")
    Single<ValidateResetCodeResponse> doValidateResetCodeAPI(
            @Query("resetToken") String resetToken);


    @GET("/tours")
    Single<HomeInfoResponse> getHomeInfoAPI(
            @Header("Authorization") String accessToken,
            @Query("count") int count,
            @Query("page") int page);

    @POST("/tours/like")
    Single<DoLikeResponse> doLikeAPI(
            @Header("Content-Type") String contentType,
            @Header("Authorization") String accessToken,
            @Body LikeRequest likeRequest);

    @GET("/tours/getTourByID")
    Single<TourInfoResponse> getTourInfoAPI(
            @Header("Authorization") String accessToken,
            @Query("tourLocationID") String tourLocationID);

    @GET("/tours/tourGallery")
    Single<GetTourGalleryResponse> getTourGalleryAPI(
            @Header("Authorization") String accessToken);

    @GET("/tours/getPackageList")
    Single<GetPackageResponse> getPackagesAPI(
            @Header("Authorization") String accessToken);

    @Multipart
    @POST("/tours/upload_images")
    Single<UploadTourImagesResponse> uploadTourImagesAPI(
            @Header("Authorization") String accessToken,
            @Part MultipartBody.Part[] tourImages,
            @Query("tourID") String tourID);


    @GET("/comments/byTourId")
    Single<GetCommentsResponse> getCommentsAPI(
            @Header("Authorization") String accessToken,
            @Query("tourId") String tourId,
            @Query("count") int count,
            @Query("page") int page);

    @POST("/comments/add")
    Single<AddCommentResponse> doCommentAPI(
            @Header("Authorization") String accessToken,
            @Body CommentRequest commentRequest);

    @GET("/user/getUserById")
    Single<ProfileResponse> getProfileDetailsAPI(
            @Header("Authorization") String accessToken,
            @Query("userId") String userId);

    @PATCH("/user/updateUserById")
    Single<ProfileUpdateResponse> doUpdateProfileAPI(
            @Header("Authorization") String accessToken,
            @Body UpdateMyDetailRequest updateMyDetailRequest,
            @Query("userId") String userId);

    @POST("/user/logout")
    Single<LogOutResponse> doLogOutAPI(
            @Header("Authorization") String accessToken);


    @Multipart
    @POST("/user/uploadProfileImage")
    Single<UploadProfileImageResponse> doUploadProfileImageAPI(
            @Header("Authorization") String accessToken,
            @Part MultipartBody.Part[] profileImage,
            @Query("userID") String userId);

    /////////////////////////notification///////////////////////////

    @GET("/notifications/notificationCount")
    Single<NotificationCountResponse> getNotificationCountAPI(
            @Header("Authorization") String accessToken);

    @GET("/notifications")
    Single<NotificationResponse> getNotificationAPI(
            @Header("Authorization") String accessToken);

    @POST("/notifications/setReadStatus")
    Single<UpdateReadStatusResponse> updateReadStatusAPI(
            @Header("Authorization") String accessToken,
            @Body UpdateReadStatusRequest updateReadStatusRequest);

    @POST("/inquire")
    Single<AddInquireResponse> doInquireAPI(
            @Header("Authorization") String accessToken,
            @Body InquireRequest inquireRequest);

    @GET("/tours/getToursMapDetails")
    Single<TourMapDetailsResponse> getToursMapDetailsAPI(
            @Header("Authorization") String accessToken);

}
