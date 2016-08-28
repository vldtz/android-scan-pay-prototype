package com.vladc.android.mobileerptool.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.activity.ProductDetailActivity;
import com.vladc.android.mobileerptool.activity.ProductListActivity;
import com.vladc.android.mobileerptool.dao.entity.Product;
import com.vladc.android.mobileerptool.dao.entity.ProductImage;
import com.vladc.android.mobileerptool.dao.impl.ProductDaoImpl;
import com.vladc.android.mobileerptool.dao.impl.ProductImageDaoImpl;

import java.util.List;


/**
 * A fragment representing a single Product detail screen.
 * This fragment is either contained in a {@link ProductListActivity}
 * in two-pane mode (on tablets) or a {@link ProductDetailActivity}
 * on handsets.
 */
public class ProductDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_BARCODE = "item_barcode";

    /**
     * The dummy content this fragment is presenting.
     */
    private Product mItem;
    private List<ProductImage> mImagesList;
    private Gallery mGallery;
    private ImageView mImageView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            ProductDaoImpl productDao = new ProductDaoImpl(getContext());
            productDao.open();

            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = productDao.findById(getArguments().getLong(ARG_ITEM_ID));
            productDao.close();

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.product_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.field_id)).setText(String.valueOf(mItem.getId()));
            ((TextView) rootView.findViewById(R.id.field_barcode)).setText(mItem.getBarcode());
            ((TextView) rootView.findViewById(R.id.field_name)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.field_quantity)).setText(String.valueOf(mItem.getQuantity()));


            ProductImageDaoImpl productImageDao = new ProductImageDaoImpl(getContext());
            productImageDao.open();
            mImagesList = productImageDao.findByProductId(mItem.getId());

            mImageView = (ImageView) rootView.findViewById(R.id.image_large);
            mGallery = (Gallery) rootView.findViewById(R.id.thumb_gallery);
            mGallery.setAdapter(new ImageAdapter(getContext(),mImagesList));
            mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position,long id)
                {
                    Toast.makeText(getContext(),"pic" + (position + 1) + " selected",
                            Toast.LENGTH_SHORT).show();
                    // display the images selected
                    setPic(mImagesList.get(position));
                }
            });
        }

        return rootView;
    }

    private void setPic(ProductImage image) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getImagePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(image.getImagePath(), bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;

        private List<ProductImage> mImagesList;
        private int itemBackground;
        public ImageAdapter(Context c, List<ProductImage> images)
        {
            context = c;
            mImagesList = images;
            // sets a grey background; wraps around the images
//            TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
//            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
//            a.recycle();
        }
        // returns the number of images
        public int getCount() {
            return mImagesList.size();
        }
        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }
        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }
        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(mImagesList.get(position).getImage());
            imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
    }
}
