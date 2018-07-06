package com.nanodegree.bakingapp.model.request;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class RecipeStepsRequest {

    @SerializedName("id")
    int stepId;
    @SerializedName("shortDescription")
    String stepShortDescription;
    @SerializedName("description")
    String stepDescription;
    @SerializedName("videoURL")
    String stepVideoUrl;
    @SerializedName("thumbnailURL")
    String stepThumbnailURL;


    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getStepShortDescription() {
        return stepShortDescription;
    }

    public void setStepShortDescription(String stepShortDescription) {
        this.stepShortDescription = stepShortDescription;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public String getStepVideoUrl() {
        return stepVideoUrl;
    }

    public void setStepVideoUrl(String stepVideoUrl) {
        this.stepVideoUrl = stepVideoUrl;
    }

    public String getStepThumbnailURL() {
        return stepThumbnailURL;
    }

    public void setStepThumbnailURL(String stepThumbnailURL) {
        this.stepThumbnailURL = stepThumbnailURL;
    }
}
