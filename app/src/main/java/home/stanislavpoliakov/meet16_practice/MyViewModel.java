package home.stanislavpoliakov.meet16_practice;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;
import java.util.List;

/**
 * Класс ViewModel
 */
public class MyViewModel extends ViewModel {
    private static final String TAG = "meet16_logs";
    private Model mModel;

    // Делаем переменную LiveData видимой, чтобы привязать Observer к ней напрямую
    public MutableLiveData<List<DownloadedPicture>> bitmapCollection = new MutableLiveData<>();

    public MyViewModel() {
        mModel = Model.getInstance();
    }

    /**
     * Для нужд программы (получение данных из сети) запускаем  AsynkTask
     */
    public class FetchDataTask extends AsyncTask<String, Void, List<DownloadedPicture>> {

        @Override
        protected List<DownloadedPicture> doInBackground(String... strings) {
            return mModel.fetchData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<DownloadedPicture> bitmapList) {
            super.onPostExecute(bitmapList);
            bitmapCollection.setValue(bitmapList);
        }
    }

    /**
     * Обрабатываем нажатие Enter в EditText
     * @param keyword пользовательский ввод - часть запроса данных
     */
    public void keywordSubmitted(String keyword) {
        FetchDataTask fetchDataTask = new FetchDataTask();
        fetchDataTask.execute(keyword);
    }

    /**
     * Обрабатываем нажатие на ViewHolder
     * @param itemPosition позиция элемента в списке
     */
    public void itemClicked(int itemPosition) {
        mModel.setMVPData(itemPosition);
    }
}
