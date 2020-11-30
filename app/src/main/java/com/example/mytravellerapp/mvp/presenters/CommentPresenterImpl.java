package com.example.mytravellerapp.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.CommentService;
import com.example.mytravellerapp.domain.Service;
import com.example.mytravellerapp.model.entities.request.CommentRequest;
import com.example.mytravellerapp.model.entities.response.AddCommentResponse;
import com.example.mytravellerapp.model.entities.response.GetCommentsResponse;
import com.example.mytravellerapp.model.rest.exceptions.RetrofitException;
import com.example.mytravellerapp.mvp.views.CommentView;
import com.example.mytravellerapp.mvp.views.View;
import com.example.mytravellerapp.utils.IScheduler;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;


public class CommentPresenterImpl extends BasePresenter implements CommentPresenter {

    private final static String TAG = "CommentPresenterImpl";

    private CommentView mCommentView;

    public CommentPresenterImpl(Activity activityContext, Service commentService, IScheduler scheduler) {
        super(activityContext, commentService, scheduler);
    }


    @Override
    public void getComments(String tourId, int count, int page) {
        disposable.add(getCommentsObservable(tourId, count, page).subscribeWith(getCommentsSubscriber()));
    }

    @Override
    public void doComment(CommentRequest commentRequest) {
        disposable.add(doCommentObservable(commentRequest).subscribeWith(doCommentSubscriber()));
    }


    @Override
    public void attachView(View v) {
        if (v instanceof CommentView) {
            mCommentView = (CommentView) v;
            mView = mCommentView;
        }
    }

    public Single<GetCommentsResponse> getCommentsObservable(String tourId, int count, int page) {
        try {
            return getService().getCommentsService(getAccessToken(), tourId, count, page)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Single<AddCommentResponse> doCommentObservable(CommentRequest commentRequest) {
        try {
            return getService().doCommentService(getAccessToken(), commentRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public DisposableSingleObserver<GetCommentsResponse> getCommentsSubscriber() {
        return new DefaultSubscriber<GetCommentsResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        GetCommentsResponse exceptionResponse = new GetCommentsResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mCommentView.showGetCommentsResponse(exceptionResponse);
                    } else {
                        GetCommentsResponse response = error.getErrorBodyAs(GetCommentsResponse.class);
                        if (response == null) {
                            response = new GetCommentsResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mCommentView.showGetCommentsResponse(response);
                    }
                } catch (IOException ex) {
                    GetCommentsResponse exceptionResponse = new GetCommentsResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mCommentView.showGetCommentsResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(GetCommentsResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mCommentView.showGetCommentsResponse(response);
                }
            }
        };
    }
    public DisposableSingleObserver<AddCommentResponse> doCommentSubscriber() {
        return new DefaultSubscriber<AddCommentResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        AddCommentResponse exceptionResponse = new AddCommentResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mCommentView.showAddCommentResponse(exceptionResponse);
                    } else {
                        AddCommentResponse response = error.getErrorBodyAs(AddCommentResponse.class);
                        if (response == null) {
                            response = new AddCommentResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mCommentView.showAddCommentResponse(response);
                    }
                } catch (IOException ex) {
                    AddCommentResponse exceptionResponse = new AddCommentResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mCommentView.showAddCommentResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(AddCommentResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mCommentView.showAddCommentResponse(response);
                }
            }
        };
    }

    private CommentService getService() {
        return (CommentService) mService;
    }

}