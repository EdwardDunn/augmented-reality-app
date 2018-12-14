package edwarddunn.co.uk.augmented_reality_app;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.rendering.Color;

/**
 * Created by Edward Dunn
 *
 * Description: I have started this project to learn more about developing AR apps. This application
 * will display a simple custom object on any flat surface.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;

    @Nullable
    private ModelRenderable videoRenderable;
    private MediaPlayer mediaPlayer;

    // Color filter
    private static final Color CHROMA_KEY_COLOR = new Color(0.1843f, 1.0f, 0.098f);

    // Height controls
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
