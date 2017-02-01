package android.vm.puntiinteresse;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

/**
 * Created by acbes on 30/01/2017.
 */

abstract class PhotoTask extends AsyncTask<String, Void, PhotoTask.AttributedPhoto> {

    private int mHeight;

    private int mWidth;

    private GoogleApiClient mGoogleApiClient;
    public PhotoTask(int width, int height, GoogleApiClient mGoogleApiClient) {
        mHeight = height;
        mWidth = width;
        this.mGoogleApiClient = mGoogleApiClient;
    }

    /**
     * Loads the first photo for a place id from the Geo Data API.
     * The place id must be the first (and only) parameter.
     */
    @Override
    protected AttributedPhoto doInBackground(String... params) {
        if (params.length != 1) {
            return null;
        }
        final String placeId = params[0];
        AttributedPhoto attributedPhoto = null;


        PlacePhotoMetadataResult result = Places.GeoDataApi
                .getPlacePhotos(mGoogleApiClient, placeId).await();

        if (result.getStatus().isSuccess()) {
            PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
            PlacePhotoMetadata photo;
            attributedPhoto = new AttributedPhoto();
            ArrayList<BitmapDrawable> imageBit  = new ArrayList<>();
            if (photoMetadataBuffer.getCount() > 0 && !isCancelled()) {
                for (int i = 0; i < photoMetadataBuffer.getCount(); i++){
                    // Get the first bitmap and its attributions.
                    photo = photoMetadataBuffer.get(i);
                // Load a scaled bitmap for this photo.
                Bitmap image = photo.getScaledPhoto(mGoogleApiClient, mWidth, mHeight).await()
                        .getBitmap();

                imageBit.add(new BitmapDrawable(Resources.getSystem(), image));
                attributedPhoto.addBitmap(imageBit.get(i));
            }
            }
            // Release the PlacePhotoMetadataBuffer.
            photoMetadataBuffer.release();
        }
        return attributedPhoto;
    }

    /**
     * Holder for an image and its attribution.
     */
    class AttributedPhoto {

       // public final CharSequence attribution;

        public final ArrayList <BitmapDrawable> bitmap=new ArrayList<>();

        public AttributedPhoto() {
        }
        public void addBitmap (BitmapDrawable bitmap){
            this.bitmap.add(bitmap);
        }
    }
}

