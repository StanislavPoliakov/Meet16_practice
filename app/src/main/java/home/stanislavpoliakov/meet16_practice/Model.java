package home.stanislavpoliakov.meet16_practice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import home.stanislavpoliakov.meet16_practice.response_data.ResponseObject;

/**
 * Класс модели. Точка пересечения нашей MVVM и MVP. Singleton
 */
public class Model {
    private static Model instance;
    private static final String TAG = "meet16_logs";

    private String urlString = "https://api.unsplash.com/search/photos/?Accept-Version=v1"
            + "&client_id=51051db5a916274b12dabcf8a93fe32bee6fba6c2678bb8bd7cd9a6bceb90cf8"
            + "&per_page=9";

    private MainContract.MVPPresenter presenter;
    private List<DownloadedPicture> pictures;


    private Model() {
    }

    static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    /**
     * Метод получение данных из интернета. Обработан стимом, в рабочем потоке (который запустил
     * ViewModel :) ). Логика такая: Посылаем GET в сеть, получаем JSON-ответ, парсим в объект, получаем
     * с объекта поток, конкурентно собираем объект бизнес логики, попутно загружая необходимые картинки
     * из json-ответа, все компонуем в список и возвращаем.
     * @param keyword ключевое слово для поиска коллекций картинок
     * @return список объектов, описывающих загруженные картинки
     */
    public List<DownloadedPicture> fetchData(String keyword) {

        pictures = Stream.of(search(keyword).results).parallel()
                .map(r -> {
                    DownloadedPicture picture = new DownloadedPicture();
                    picture.description = r.description;
                    picture.bitmap = getBitmap(r.urls.small);
                    return picture;
                })
                .collect(Collectors.toList());
        return pictures;
    }

    /**
     * "Прикручиваем" к модели презентер, чтобы модель его не инициализировала
     * @param presenter
     */
    public void attachPresenter(MainContract.MVPPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Ответ презентору на просьбу дать картинку. Картинку не грузим через сеть. Чтобы не нагружать
     * и без того медленную логику и пользовательский интернет (может там EDGE :) ), мы все данные
     * скачали в самые первый раз (fetchData). А сейчас просто достаем.
     * Работу запускаем через new Thread.
     * @param position позиция выбранной картинки в RecyclerView, фактически, позиция в списке
     */
    public void setMVPData(int position) {
        Thread t = new Thread(() -> presenter.showData(getLargeImage(position)));
        t.start();
    }

    /**
     * Наделал вспомогательных методов
     * @param position
     * @return
     */
    private Bitmap getLargeImage(int position) {
        return pictures.get(position).bitmap;
    }

    /**
     * Метод получения JSON в ответ на GET-запрос. Надо было бы все методы в Model.Utils собрать
     * @param keyword
     * @return
     */
    @NonNull
    private ResponseObject search(@NonNull String keyword) {
        final String searchQuery = "&query=";
        HttpURLConnection connection = null;
        BufferedReader reader;
        URL url = stringToUrl(urlString + searchQuery + keyword);

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10 * 1000);

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            reader.close();

            ResponseObject request = (ResponseObject) convertJSON(builder.toString());
            return request;

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    /**
     * Метод загрузки картинок по готовым ссылкам (ссылки конвертируем String -> URL) внутри.
     * @param urlString ссылка в формате String
     * @return картинка в формате Bitmap
     */
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

    /**
     * Метод конвертации String в URL
     * @param value
     * @return
     */
    private URL stringToUrl(String value) {
        URL url = null;
        try {
            url = new URL(value);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return url;
    }

    /**
     * Метод конвертации JSON-ответа в POJO
     * @param source JSON-строка
     * @return POJO (ResponseObject)
     */
    private Object convertJSON(String source) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(source, ResponseObject.class);
    }
}
