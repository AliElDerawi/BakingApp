package com.nanodegree.bakingapp.ui.Fragments;


import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.request.RecipeStepsRequest;
import com.nanodegree.bakingapp.util.GlideApp;
import com.nanodegree.bakingapp.util.RecipeImages;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPES_STEP;
import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPE_NAME;
import static com.nanodegree.bakingapp.model.Contracts.EXTRA_STEP_POSITION;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemRecipeStepFragment extends Fragment implements ExoPlayer.EventListener {


    @BindView(R.id.recipe_step_short_description_text_view)
    TextView mRecipeStepShortDescriptionTextView;
    @BindView(R.id.recipe_step_full_description_text_view)
    TextView mRecipeStepFullDescriptionTextView;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.recipe_step_no_video_text_view)
    TextView mRecipeNoVideoTextView;
    @BindView(R.id.recipe_step_thumbnail_image_view)
    ImageView mRecipeStepThumbnail;

    private ArrayList<RecipeStepsRequest> mRecipeStepsArrayList;
    private String mRecipeName;
    private int mRecipeStepPosition;


    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private boolean mPlayWhenReady ;
    private long mCurrentPlayPosition;
    private int mPlayStartWindow;

    private static final String RECIPE_STEP_VIDEO_STATE = "video_state";
    private static final String RECIPE_STEP_VIDEO_POSITION = "video_position";
    private static final String RECIPE_STEP_VIDEO_WINDOW = "video_window";
    private static final String RECIPE_STEP_VIDEO_TRACK_SELECTOR = "video_track_selector";
    private final String TAG = ItemRecipeStepFragment.class.getSimpleName();

    public ItemRecipeStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_recipe_step, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            mRecipeStepsArrayList = Parcels.unwrap(savedInstanceState.getParcelable(EXTRA_RECIPES_STEP));
            mRecipeName = savedInstanceState.getString(EXTRA_RECIPE_NAME);
            mRecipeStepPosition = savedInstanceState.getInt(EXTRA_STEP_POSITION);
            mPlayWhenReady = savedInstanceState.getBoolean(RECIPE_STEP_VIDEO_STATE);
            mCurrentPlayPosition = savedInstanceState.getLong(RECIPE_STEP_VIDEO_POSITION);
            mPlayStartWindow = savedInstanceState.getInt(RECIPE_STEP_VIDEO_WINDOW);

        } else {
            clearStartPosition();
        }
        return view;

    }

    private void updateStartPosition() {
        if (mExoPlayer != null) {
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mPlayStartWindow = mExoPlayer.getCurrentWindowIndex();
            mCurrentPlayPosition = Math.max(0, mExoPlayer.getContentPosition());
        }
    }

    private void clearStartPosition() {
        mPlayWhenReady = true;
        mPlayStartWindow = C.INDEX_UNSET;
        mCurrentPlayPosition = C.TIME_UNSET;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecipeStepShortDescriptionTextView.setText(mRecipeStepsArrayList.get(mRecipeStepPosition).getStepShortDescription());
        mRecipeStepFullDescriptionTextView.setText(mRecipeStepsArrayList.get(mRecipeStepPosition).getStepDescription());


        if (mRecipeStepsArrayList.get(mRecipeStepPosition).getStepVideoUrl().isEmpty()) {
            mRecipeNoVideoTextView.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
            mRecipeStepThumbnail.setVisibility(View.VISIBLE);
            GlideApp.with(this).load(mRecipeStepsArrayList.get(mRecipeStepPosition).getStepThumbnailURL()).error(RecipeImages.getImageDrawable(mRecipeName)).into(mRecipeStepThumbnail);

        } else {
//            initializeMediaSession();
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), RecipeImages.getImageDrawable(mRecipeName)));
            mRecipeStepThumbnail.setVisibility(View.GONE);
            mRecipeNoVideoTextView.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(mRecipeStepsArrayList.get(mRecipeStepPosition).getStepVideoUrl()));
        }
    }

    public void setRecipeStepsArrayList(ArrayList<RecipeStepsRequest> mRecipeStepsArrayList) {
        this.mRecipeStepsArrayList = mRecipeStepsArrayList;
        Log.d(TAG, "Size>" + mRecipeStepsArrayList.size());
    }

    public void setRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;

    }

    public void setRecipeStepPosition(int mRecipeStepPosition) {
        this.mRecipeStepPosition = mRecipeStepPosition;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecipeStepsArrayList != null) {
            outState.putParcelable(EXTRA_RECIPES_STEP, Parcels.wrap(mRecipeStepsArrayList));
            outState.putString(EXTRA_RECIPE_NAME, mRecipeName);
            outState.putInt(EXTRA_STEP_POSITION, mRecipeStepPosition);
        }
        updateStartPosition();

        outState.putBoolean(RECIPE_STEP_VIDEO_STATE, mPlayWhenReady);
        outState.putLong(RECIPE_STEP_VIDEO_POSITION, mCurrentPlayPosition);
        outState.putInt(RECIPE_STEP_VIDEO_WINDOW,mPlayStartWindow);

    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

            // Set the ExoPlayer.EventListener to this activity.
//            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            boolean haveStartPosition = mPlayStartWindow != C.INDEX_UNSET;
            if (haveStartPosition) {
                mExoPlayer.seekTo(mPlayStartWindow, mCurrentPlayPosition);
            }
            mPlayerView.setPlayer(mExoPlayer);
        }
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new ItemRecipeStepFragment.MySessionCallback());

        mMediaSession.setActive(true);

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
//        if (mMediaSession != null)
//        mMediaSession.setActive(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {

        if (mExoPlayer != null) {
            updateStartPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

}
