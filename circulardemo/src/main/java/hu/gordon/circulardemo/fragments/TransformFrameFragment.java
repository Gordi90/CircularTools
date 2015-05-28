package hu.gordon.circulardemo.fragments;


import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.shamanland.fab.FloatingActionButton;

import hu.gordon.circulardemo.MainActivity;
import hu.gordon.circulardemo.R;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.TransformViewAnimationUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransformFrameFragment extends Fragment implements SupportAnimator.AnimatorListener {

    public static final String TAG = TransformFrameFragment.class.getSimpleName();

    private int screenWidth;
    private int screenHeight;
    private FloatingActionButton fab;
    private ImageView imageView1, imageView2;
    private boolean state = true;
    private boolean animationInProgress = false;

    private ImageView mySourceView;
    private ImageView myTargetView;

    public TransformFrameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_transform_frame, container, false);


        imageView1 = (ImageView) root.findViewById(R.id.iv1);
        imageView2 = (ImageView) root.findViewById(R.id.iv2);

        if(state) {
            imageView2.setVisibility(View.INVISIBLE);
        } else {
            imageView1.setVisibility(View.INVISIBLE);
        }
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Display display = getActivity().getWindowManager().getDefaultDisplay();

        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        } else {
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }


        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(animationInProgress){
                    return;
                }

                if (state) {
                    mySourceView = imageView1;
                    myTargetView = imageView2;
                } else {
                    mySourceView = imageView2;
                    myTargetView = imageView1;
                }

                myTargetView.setVisibility(View.INVISIBLE);

                //
                // Pre-calculations
                //
                // get the final radius for the clipping circle
                float finalRadius = TransformViewAnimationUtils.hypo(screenWidth, screenHeight);
                int[] center = TransformViewAnimationUtils.getCenter(fab,myTargetView);

                SupportAnimator animator =
                        TransformViewAnimationUtils.createCircularTransform(myTargetView, mySourceView, center[0], center[1], 0F, finalRadius);

                animator.addListener(TransformFrameFragment.this);

                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(1500);
                animator.start();
                state = !state;
            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(3);
    }

    @Override
    public void onAnimationStart() {
        Log.d(TAG, "animation start");
        myTargetView.setVisibility(View.VISIBLE);
        animationInProgress = true;
    }

    @Override
    public void onAnimationEnd() {
        Log.d(TAG, "animation end");
        mySourceView.setVisibility(View.INVISIBLE);
        animationInProgress = false;
    }

    @Override
    public void onAnimationCancel() {

    }

    @Override
    public void onAnimationRepeat() {

    }
}
