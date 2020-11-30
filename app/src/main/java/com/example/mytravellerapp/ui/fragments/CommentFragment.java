package com.example.mytravellerapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.mytravellerapp.BaseApplication;
import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.domain.CommentService;
import com.example.mytravellerapp.domain.CommentServiceImpl;
import com.example.mytravellerapp.dto.Comment;
import com.example.mytravellerapp.model.entities.request.CommentRequest;
import com.example.mytravellerapp.model.entities.response.AddCommentResponse;
import com.example.mytravellerapp.model.entities.response.AddReplyResponse;
import com.example.mytravellerapp.model.entities.response.GetCommentsResponse;
import com.example.mytravellerapp.model.rest.BMSService;
import com.example.mytravellerapp.mvp.presenters.CommentPresenter;
import com.example.mytravellerapp.mvp.presenters.CommentPresenterImpl;
import com.example.mytravellerapp.mvp.presenters.Presenter;
import com.example.mytravellerapp.mvp.views.CommentView;
import com.example.mytravellerapp.ui.adapters.CommentsItemRecycleAdapter;
import com.example.mytravellerapp.utils.AppScheduler;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;


public class CommentFragment extends BaseFragment implements CommentView {

    private static final String TAG = "CommentFragment";
    private static CommentFragment commentFragment;
    private String tourId;
    private String imageUrl;
    private Context context;
    private static Presenter presenter;
    private final int TAKE_CONSTANT = 10;
    private int page = 1;
    private CommentsItemRecycleAdapter commentsItemRecycleAdapter;
    //elements in the UI
    private ImageView tourImage;
    private RecyclerView mRecyclerView;
    private ImageView sendButton;
    private EditText mComment;
    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";
    private int commentCount;
    private CommentRequest request;
//    public CommentFragment(String tourId, String imageUrl, Context context) {
//        this.tourId = tourId;
//        this.imageUrl = imageUrl;
//
//    }


    public static String getTAG() {
        return TAG;
    }

    public static CommentFragment newInstance(String tourId, String imageUrl, int commentCount) {
        Bundle args = new Bundle();
        args.putString("tourId", tourId);
        args.putString("imageUrl", imageUrl);
        args.putInt("commentCount", commentCount);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //    public static CommentFragment getInstance(String tourId, String imageUrl, Context context){
//        CommentFragment commentFragment = new CommentFragment(tourId, imageUrl, context);
//        return  commentFragment;
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_comment, container, false);
        tourImage = rootView.findViewById(R.id.tour_image);
        mRecyclerView = rootView.findViewById(R.id.comments_list);

        //Elements required to send user comment
        mComment = rootView.findViewById(R.id.comment_input);
        sendButton = rootView.findViewById(R.id.btn_post_comment);

        commentFragment = this;

        imageUrl = (String) getArguments().get("imageUrl");
        tourId = (String) getArguments().get("tourId");
        commentCount = (Integer) getArguments().getInt("commentCount");
        setData();
        initRecyclerView();
        performGetCommentInfoRequest(tourId, commentCount);

        //Send comment functionality
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSendComment();
            }
        });
        mComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String comment = mComment.getText().toString().trim();

                if (comment.length() > 0) {
                    sendButton.setImageResource(R.drawable.ic_send_selected);
                } else {
                    sendButton.setImageResource(R.drawable.ic_send_comment_black);
                }
            }
        });

        return rootView;
    }


    protected void setData() {
        String finalImageUrl = imageUrl;
        //Picasso.with(getContext()).load(finalImageUrl).resize(500, 400).into(tourImage);
    }

    protected void initRecyclerView() {

        commentsItemRecycleAdapter = new CommentsItemRecycleAdapter(new ArrayList<Comment>(), imageUrl, getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(commentsItemRecycleAdapter);

    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_back, null);
        ImageView btnBack = (ImageView) mCustomView.findViewById(R.id.imgVbackAr);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        TextView mTitle = (TextView) mCustomView.findViewById(R.id.title_text_back);
        mToolBar.addView(mCustomView);
        mTitle.setText((R.string.title_comment));
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0, 0);
    }

    @Override
    protected void setUpUI() {

    }


    @Override
    public void initializePresenter() {
        CommentService mCommentService = new CommentServiceImpl(new BMSService());
        presenter = new CommentPresenterImpl(getActivity(), mCommentService, new AppScheduler());
        presenter.attachView(CommentFragment.this);
        presenter.onCreate();

    }

    @Override
    public void showAddCommentResponse(AddCommentResponse addCommentResponse) {
        setProgressDialog(false);
        if (addCommentResponse.isSuccess()) {
            showTopSnackBar(addCommentResponse.getMessage(), getResources().getColor(R.color.green));
            resetRecyclerView();
            reloadFragment();
        }else {
            if (addCommentResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());
                return;
            } else if (addCommentResponse.isAPIError() && addCommentResponse.getMessage() != null) {
                showTopSnackBar(addCommentResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(addCommentResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            }
        }
    }

    @Override
    public void showAddReplyResponse(AddReplyResponse addReplyResponse) {
        setProgressDialog(false);
    }

    @Override
    public void showGetCommentsResponse(GetCommentsResponse getCommentsResponse) {
        setProgressDialog(false);
        if ((!isAdded() || !isVisible())) return;
        if (getCommentsResponse.isSuccess()) {
            commentsItemRecycleAdapter.updateData(getCommentsResponse.getComments().getData(), 0);
        } else {
            if (getCommentsResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());

                return;
            } else if (getCommentsResponse.isAPIError() && getCommentsResponse.getMessage() != null) {
                showTopSnackBar(getCommentsResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(getCommentsResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            }
        }
    }

    @Override
    public void showMessage(String message) {

    }

    //get the comment list
    public void performGetCommentInfoRequest(String tourId, int commentCount) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((CommentPresenter) presenter).getComments(tourId, commentCount, page);
        } else {

        }
    }

    private void performSendComment() {
        String txtComment = mComment.getText().toString().trim();
        if (txtComment.isEmpty()) {
            mComment.requestFocus();
        } else {
            request = new CommentRequest();
            request.setTourId(tourId);
            request.setDescription(txtComment);

            performSendCommentsRequest(request);
        }
    }

    private void performSendCommentsRequest(CommentRequest commentRequest) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((CommentPresenter) presenter).doComment(commentRequest);
        } else {
//            showAlertDialog(false, ApplicationConstants.WARNING,
//                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    private void reloadFragment() {
        mComment.setText("");
        commentCount += 2;
        performGetCommentInfoRequest(tourId, commentCount);
    }

    private void resetRecyclerView() {
        commentsItemRecycleAdapter.updateData(null, 1);
        page = 1;
    }
}
