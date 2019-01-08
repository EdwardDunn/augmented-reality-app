package edwarddunn.co.uk.augmented_reality_app;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.rendering.Color;

/**
 * Created by Edward Dunn
 *
 * Description: I have started this project to learn more about developing AR apps. This application
 * will display a simple custom object on any flat surface. I'm using the Android developer examples
 * as a reference - https://developers.google.com/ar/develop/c/quickstart.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;

    @Nullable
    private ModelRenderable videoRenderable;
    private MediaPlayer mediaPlayer;

    // color filter
    private static final Color KEY_COLOR = new Color(0.1843f, 1.0f, 0.098f);

    // height controls
    private static final float VIDEO_HEIGHT_METERS = 0.85f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        ExternalTexture texture = new ExternalTexture();

        //TODO - add image chroma here as R.raw.image_chroma
        mediaPlayer = MediaPlayer.create(this, [image chroma here]);
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);

        //create renderable
        ModelRenderable.builder()
                //TODO - add key video to res folder
                .setSource(this, R.raw.key_video)
                .build()
                .thenAccept(
                        renderable -> {
                            videoRenderable = renderable;
                            renderable.getMaterial().setExternalTexture("videoTexture", texture);
                            renderable.getMaterial().setFloat4("keyColor", KEY_COLOR);
                        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (videoRenderable == null) {
                        return;
                    }

                    // add anchor
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // create a node to render the video and add it to the anchor.
                    Node videoNode = new Node();
                    videoNode.setParent(anchorNode);

                    // set the scale of the node so that the aspect ratio of the video is correct
                    float videoWidth = mediaPlayer.getVideoWidth();
                    float videoHeight = mediaPlayer.getVideoHeight();
                    videoNode.setLocalScale(
                            new Vector3(
                                    VIDEO_HEIGHT_METERS * (videoWidth / videoHeight), VIDEO_HEIGHT_METERS, 1.0f));

                    // Start playing the video when the first node is placed
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();

                        // prevent renderable appearing as black quad before the video starts
                        texture
                                .getSurfaceTexture()
                                .setOnFrameAvailableListener(
                                        (SurfaceTexture surfaceTexture) -> {
                                            videoNode.setRenderable(videoRenderable);
                                            texture.getSurfaceTexture().setOnFrameAvailableListener(null);
                                        });
                    } else {
                        videoNode.setRenderable(videoRenderable);
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
