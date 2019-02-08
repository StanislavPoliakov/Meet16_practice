package home.stanislavpoliakov.meet16_practice;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import home.stanislavpoliakov.meet16_practice.response_data.ResponseObject;

public class Model {
    private static Model instance;
    private static final String TAG = "meet16_logs";
    /*private final String LOCATION = "https://api.unsplash.com/";
    private final String ACCEPT_VERSION = "Accept-Version=v1";
    private final String SEARCH_BASE = "/search/photos";
    private final String ACCESS_KEY = "client_id=51051db5a916274b12dabcf8a93fe32bee6fba6c2678bb8bd7cd9a6bceb90cf8";*/

    private String urlString = "https://api.unsplash.com/search/photos/?Accept-Version=v1"
            + "&client_id=51051db5a916274b12dabcf8a93fe32bee6fba6c2678bb8bd7cd9a6bceb90cf8"
            + "&per_page=9";

    private List<String> imageUrls;
    private MainContract.MVPPresenter presenter;
    private MainContract.MVVMViewModel viewModel;
    //private String keyword;
    private BuisnessData buisnessData;
    private Map<String, Bitmap> data;
    private List<DownloadedPicture> pictures;
    //private WorkThread workThread;


    private Model() {
        /*workThread = new WorkThread();
        workThread.start();*/
    }

    static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    /*private class WorkThread extends HandlerThread {
        private final int FETCH_DATA = 1;
        private final int DONE = 2;
        private Handler mHandler;

        public WorkThread() {
            super("WorkThread");
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            mHandler = new Handler(getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case FETCH_DATA:
                            data = Arrays.stream(search((String) msg.obj).results).parallel()
                                    .collect(Collectors.toMap(r -> r.description, r -> getBitmap(r.urls.small)));
                            mHandler.sendEmptyMessage(DONE);
                            break;
                        case DONE:

                    }
                }
            };
        }

        public void fetchDataFromNetwork(String keyword) {
            Message message = mHandler.obtainMessage(FETCH_DATA, keyword);
            mHandler.sendMessage(message);
        }
    }*/

    public List<DownloadedPicture> fetchData(String keyword) {
        /*return Stream.of(search(keyword).results).parallel()
                .map(r -> new DownloadedPicture(r.description, getBitmap(r.urls.small)))
                .collect(Collectors.toList());*/

        pictures = Stream.of(search(keyword).results).parallel()
                .map(r -> {
                    DownloadedPicture picture = new DownloadedPicture();
                    picture.description = r.description;
                    picture.bitmap = getBitmap(r.urls.small);
                    return picture;
                })
                .collect(Collectors.toList());
        return pictures;

        /*return pictures.stream()
                .map(p -> p.getBitmap())
                .collect(Collectors.toList());*/


       /* buisnessData = new BuisnessData();
        List<String> descriptions = Collections.synchronizedList(new ArrayList<>());
        List<Bitmap> bitmapList = Collections.synchronizedList(new ArrayList<>());

        Arrays.stream(search(keyword).results).parallel()
                .forEach(r -> {
                    descriptions.add(r.description);
                    bitmapList.add(getBitmap(r.urls.small));
                });
        buisnessData.setBitmapDescriptions(descriptions);
        buisnessData.setBitmapCollection(bitmapList);

        return buisnessData.getBitmapCollection();*/
    }

    public void attachPresenter(MainContract.MVPPresenter presenter) {
        this.presenter = presenter;
    }

    public void attachViewModel(MainContract.MVVMViewModel viewModel) {
        this.viewModel = viewModel;
    }



    public void setMVPData(int position) {
        Thread t = new Thread(() -> presenter.showData(getLargeImage(position)));
        t.start();
    }

    private Bitmap getLargeImage(int position) {
        //return buisnessData.getBitmapCollection().get(position);
        return pictures.get(position).bitmap;
    }

   /* public List<Bitmap> getBitmapCollectionByKeyword() {
       return getSearch(keyword).parallelStream()
                    .map(this::getBitmap)
                    .collect(Collectors.toList());
    }*/

    /*public List<Bitmap> getCollection(String keyword) {
        this.keyword = keyword;
        Thread networkThread = new Thread(getBitmapCollection);
        networkThread.start();
    }*/

    @NonNull
    private ResponseObject search(@NonNull String keyword) {
        final String searchQuery = "&query=";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        URL url = stringToUrl(urlString + searchQuery + keyword);

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10 * 1000);

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            reader.close();

            ResponseObject request = (ResponseObject) convertJSON(builder.toString(), ResponseObject.class);
            imageUrls = Arrays.stream(request.results)
                    .map(r -> r.urls.small)
                    .collect(Collectors.toList());
            return request;

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    @NonNull
    private Bitmap getBitmap(String urlString) {
        HttpURLConnection connection = null;
        URL url = stringToUrl(urlString);
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return  BitmapFactory.decodeStream(connection.getInputStream());
            } else return null;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    private URL stringToUrl(String value) {
        URL url = null;
        try {
            //Log.d(TAG, "stringToUrl: Thread = " + Thread.currentThread());
            url = new URL(value);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return url;
    }

    private Object convertJSON(String source, Class reponseObject) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(source, ResponseObject.class);
    }
}
