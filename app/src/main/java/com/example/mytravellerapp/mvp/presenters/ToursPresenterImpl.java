package com.example.mytravellerapp.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.Service;
import com.example.mytravellerapp.domain.ToursService;
import com.example.mytravellerapp.model.entities.request.InquireRequest;
import com.example.mytravellerapp.model.entities.request.LikeRequest;
import com.example.mytravellerapp.model.entities.response.AddInquireResponse;
import com.example.mytravellerapp.model.entities.response.DoLikeResponse;
import com.example.mytravellerapp.model.entities.response.GetPackageResponse;
import com.example.mytravellerapp.model.entities.response.GetTourGalleryResponse;
import com.example.mytravellerapp.model.entities.response.HomeInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourMapDetailsResponse;
import com.example.mytravellerapp.model.entities.response.UploadTourImagesResponse;
import com.example.mytravellerapp.model.rest.exceptions.RetrofitException;
import com.example.mytravellerapp.mvp.views.ToursView;
import com.example.mytravellerapp.mvp.views.View;
import com.example.mytravellerapp.utils.IScheduler;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import okhttp3.MultipartBody;


public class ToursPresenterImpl extends BasePresenter implements ToursPresenter {

    private final static String TAG = "ToursPresenterImpl";

    private ToursView mToursView;

    public ToursPresenterImpl(Activity activityContext, Service userAccountService, IScheduler scheduler) {
        super(activityContext, userAccountService, scheduler);
    }

    @Override
    public void getHomeInfo(int count, int page) {

        disposable.add(getHomeInfoObservable(count, page).subscribeWith(getHomeInfoSubscriber()));
    }

    @Override
    public void doLike(LikeRequest likeRequest) {
        disposable.add(doLikeObservable(likeRequest).subscribeWith(doLikeSubscriber()));
    }

    @Override
    public void getTourInfo(String tourId) {
        disposable.add(getTourInfoObservable(tourId).subscribeWith(getTourInfoSubscriber()));
    }

    //delete this method , only for test the like functionality
    public void doLikeTest(LikeRequest likeRequest) {

    }

    @Override
    public String getId() {
        return getUserId();
    }

    @Override
    public void getTourGallery() {
        disposable.add(getTourGalleryObservable().subscribeWith(getTourGallerySubscriber()));
    }

    @Override
    public void getPackages() {
        disposable.add(getPackagesObservable().subscribeWith(getPackagesSubscriber()));
    }

    @Override
    public void uploadTourImages(MultipartBody.Part[] tourImages , String tourID) {
        disposable.add(uploadTourImagesObservable(tourImages, tourID).subscribeWith(uploadTourImagesSubscriber()));
    }

    @Override
    public void doInquire(InquireRequest inquireRequest) {
        disposable.add(doInquireObservable(inquireRequest).subscribeWith(doInquireSubscriber()));
    }

    @Override
    public void getTourMapDetails() {
        disposable.add(getTourMapDetailsObservable().subscribeWith(getTourMapDetailsSubscriber()));
    }
    private ToursService getService() {
        return (ToursService) mService;
    }

    @Override
    public void attachView(View v) {
        if (v instanceof ToursView) {
            mToursView = (ToursView) v;
            mView = mToursView;
        }
    }

    public Single<UploadTourImagesResponse> uploadTourImagesObservable(MultipartBody.Part[] tourImages, String tourID) {
        try {
            return getService(). uploadTourImagesService(getAccessToken(), tourImages, tourID)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public DisposableSingleObserver<UploadTourImagesResponse> uploadTourImagesSubscriber() {
        return new DefaultSubscriber<UploadTourImagesResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        UploadTourImagesResponse exceptionResponse = new UploadTourImagesResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mToursView.showUploadTourImagesResponse((exceptionResponse));
                    } else {
                        UploadTourImagesResponse response = error.getErrorBodyAs(UploadTourImagesResponse.class);
                        if (response == null) {
                            response = new UploadTourImagesResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mToursView.showUploadTourImagesResponse(response);
                    }
                } catch (IOException ex) {
                    UploadTourImagesResponse exceptionResponse = new UploadTourImagesResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mToursView.showUploadTourImagesResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(UploadTourImagesResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mToursView.showUploadTourImagesResponse(response);
                }
            }
        };
    }
    /*-------------------------------------------------------------*/

    public Single<GetPackageResponse> getPackagesObservable() {
        try {
            return getService(). getPackagesService(getAccessToken())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public DisposableSingleObserver<GetPackageResponse> getPackagesSubscriber() {
        return new DefaultSubscriber<GetPackageResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        GetPackageResponse exceptionResponse = new GetPackageResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mToursView.showGetPackageResponse((exceptionResponse));
                    } else {
                        GetPackageResponse response = error.getErrorBodyAs(GetPackageResponse.class);
                        if (response == null) {
                            response = new GetPackageResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mToursView.showGetPackageResponse(response);
                    }
                } catch (IOException ex) {
                    GetPackageResponse exceptionResponse = new GetPackageResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mToursView.showGetPackageResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(GetPackageResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mToursView.showGetPackageResponse(response);
                }
            }
        };
    }
/*----------------------------------------------------------------------------------*/
    public Single<HomeInfoResponse> getHomeInfoObservable(int count, int page) {
        try {
            return getService().getHomeInfoService(getAccessToken(), count, page)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
    public DisposableSingleObserver<HomeInfoResponse> getHomeInfoSubscriber() {
        return new DefaultSubscriber<HomeInfoResponse>((View) this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        HomeInfoResponse exceptionResponse = new HomeInfoResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mToursView.showHomeInfoResponse((exceptionResponse));
                    } else {
                        HomeInfoResponse response = error.getErrorBodyAs(HomeInfoResponse.class);
                        if (response == null) {
                            response = new HomeInfoResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mToursView.showHomeInfoResponse(response);
                    }
                } catch (IOException ex) {
                    HomeInfoResponse exceptionResponse = new HomeInfoResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mToursView.showHomeInfoResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(HomeInfoResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mToursView.showHomeInfoResponse(response);
                }
            }
        };
    }
    /*-----------------------------------------------------------------------------*/

    public Single<TourInfoResponse> getTourInfoObservable(String tourId) {
        try {
            return getService().getTourInfoService(getAccessToken(), tourId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public DisposableSingleObserver<TourInfoResponse> getTourInfoSubscriber() {
        return new DefaultSubscriber<TourInfoResponse>((View) this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        TourInfoResponse exceptionResponse = new TourInfoResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mToursView.showTourInfoResponse((exceptionResponse));
                    } else {
                        TourInfoResponse response = error.getErrorBodyAs(TourInfoResponse.class);
                        if (response == null) {
                            response = new TourInfoResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mToursView.showTourInfoResponse(response);
                    }
                } catch (IOException ex) {
                    TourInfoResponse exceptionResponse = new TourInfoResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mToursView.showTourInfoResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(TourInfoResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mToursView.showTourInfoResponse(response);
                }
            }
        };
    }
    /*--------------------------------------------------------------------------------------*/
    public Single<DoLikeResponse> doLikeObservable(LikeRequest likeRequest) {
        try {
            return getService().doLikeService(getAccessToken(), likeRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public DisposableSingleObserver<DoLikeResponse> doLikeSubscriber() {
        return new DefaultSubscriber<DoLikeResponse>((View) this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        DoLikeResponse exceptionResponse = new DoLikeResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
//                        mToursView.showDoLikeResponse(exceptionResponse);
                    } else {
                        DoLikeResponse response = error.getErrorBodyAs(DoLikeResponse.class);
                        if (response == null) {
                            response = new DoLikeResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
//                        mToursView.showDoLikeResponse(response);
                    }
                } catch (IOException ex) {
                    DoLikeResponse exceptionResponse = new DoLikeResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
//                    mToursView.showDoLikeResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(DoLikeResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mToursView.showDoLikeResponse(response);
                }
            }
        };
    }
    /*-------------------------------------------------------------------------------------*/

    public Single<GetTourGalleryResponse> getTourGalleryObservable() {
        try {
            return getService().getTourGalleryService(getAccessToken())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }





    public DisposableSingleObserver<GetTourGalleryResponse> getTourGallerySubscriber() {
        return new DefaultSubscriber<GetTourGalleryResponse>((View) this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        GetTourGalleryResponse exceptionResponse = new GetTourGalleryResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mToursView.showGetTourGalleryResponse((exceptionResponse));
                    } else {
                        GetTourGalleryResponse response = error.getErrorBodyAs(GetTourGalleryResponse.class);
                        if (response == null) {
                            response = new GetTourGalleryResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mToursView.showGetTourGalleryResponse(response);
                    }
                } catch (IOException ex) {
                    GetTourGalleryResponse exceptionResponse = new GetTourGalleryResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mToursView.showGetTourGalleryResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(GetTourGalleryResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mToursView.showGetTourGalleryResponse(response);
                }
            }
        };

    }

    /*-------------------------------------------------------------------*/
    public Single<AddInquireResponse> doInquireObservable(InquireRequest inquireRequest) {
        try {
            return getService(). doInquireService(getAccessToken(), inquireRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public DisposableSingleObserver<AddInquireResponse> doInquireSubscriber() {
        return new DefaultSubscriber<AddInquireResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        AddInquireResponse exceptionResponse = new AddInquireResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mToursView.showInquireResponse((exceptionResponse));
                    } else {
                        AddInquireResponse response = error.getErrorBodyAs(AddInquireResponse.class);
                        if (response == null) {
                            response = new AddInquireResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mToursView.showInquireResponse(response);
                    }
                } catch (IOException ex) {
                    AddInquireResponse exceptionResponse = new AddInquireResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mToursView.showInquireResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(AddInquireResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mToursView.showInquireResponse(response);
                }
            }
        };
    }
    /*--------------------------------------*/
    public Single<TourMapDetailsResponse> getTourMapDetailsObservable() {
        try {
            return getService(). getToursMapDetailsService(getAccessToken())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public DisposableSingleObserver<TourMapDetailsResponse> getTourMapDetailsSubscriber() {
        return new DefaultSubscriber<TourMapDetailsResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        TourMapDetailsResponse exceptionResponse = new TourMapDetailsResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mToursView.showGetToursMapDetailsResponse((exceptionResponse));
                    } else {
                        TourMapDetailsResponse response = error.getErrorBodyAs(TourMapDetailsResponse.class);
                        if (response == null) {
                            response = new TourMapDetailsResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mToursView.showGetToursMapDetailsResponse(response);
                    }
                } catch (IOException ex) {
                    TourMapDetailsResponse exceptionResponse = new TourMapDetailsResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mToursView.showGetToursMapDetailsResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(TourMapDetailsResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mToursView.showGetToursMapDetailsResponse(response);
                }
            }
        };
    }

}