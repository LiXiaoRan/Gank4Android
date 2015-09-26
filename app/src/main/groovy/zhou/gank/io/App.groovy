package zhou.gank.io

import android.app.Activity
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import android.widget.Toast
import com.bettervectordrawable.VectorDrawableCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import groovy.transform.CompileStatic
import zhou.gank.io.comment.Config
import zhou.gank.io.net.NetworkManager

@CompileStatic
class App extends Application {

    public static final String SITE_URL = "http://gank.avosapps.com";
    public static final String TYPE_URL = SITE_URL + "/api/data";
    public static final String TIME_URL = SITE_URL + "/api/day";
    public static final String RANDOM_URL = SITE_URL + "/api/random/data";

    private static App app;

    static App getInstance() {
        return app;
    }

    Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        NetworkManager.getInstance().init(this, gson)
        NetworkManager.getInstance().setDefaultHandle { e ->
            switch (e) {
                case SocketTimeoutException:
                    return Config.Error.TIME_OUT
                default:
                    return Config.Error.UNKOWN
            }
        }
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(),
                R.drawable.ic_favorite_white_48px,
                R.drawable.wrong, R.drawable.ic_info_48px,
                R.drawable.ic_dashboard_48px, R.drawable.ic_event_48px,
                R.drawable.ic_extension_48px, R.drawable.ic_settings_black_48px)

        Config.Configurable.HANDLE_BY_ME = true
    }

    static void toast(int id) {
        Toast.makeText(app, id, Toast.LENGTH_SHORT).show();
    }

    static void toast(String msg) {
        Toast.makeText(app, msg, Toast.LENGTH_SHORT).show();
    }

    static String getStr(int res) {
        return app.getResources().getString(res);
    }

    static File cacheFile() {
        return app.getCacheDir();
    }

    static void copy(String text) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) app.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
    }

    static void copyUri(Uri uri) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) app.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newUri(app.getContentResolver(), "URI", uri);
        myClipboard.setPrimaryClip(myClip);
    }

    static void setTheme(Activity activity) {
        String theme = Config.getString(getStr(R.string.key_theme), "Light")
        switch (theme) {
            case "Light":
                activity.setTheme(R.style.AppTheme)
                break
            case "Dark":
                activity.setTheme(R.style.AppThemeDark)
                break
        }
    }
}