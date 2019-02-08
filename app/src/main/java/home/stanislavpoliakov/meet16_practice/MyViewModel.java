package home.stanislavpoliakov.meet16_practice;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyViewModel extends ViewModel implements MainContract.MVVMViewModel{
    private static final String TAG = "meet16_logs";
    private Model mModel;
    private MainContract.MVVMView mView;
    //List<Bitmap> bitmapCollection;
    private String keyword;
    private ExecutorService pool = Executors.newSingleThreadExecutor();
    public MutableLiveData<List<DownloadedPicture>> bitmapCollection = new MutableLiveData<>();
    private FetchDataTask fetchDataTask;

    public MyViewModel() {
        mModel = Model.getInstance();
        mModel.attachViewModel(this);
    }

    public void attachView(MainContract.MVVMView view) {
        this.mView = view;
    }

    public class FetchDataTask extends AsyncTask<String, Void, List<DownloadedPicture>> {

        @Override
        protected List<DownloadedPicture> doInBackground(String... strings) {
            return mModel.fetchData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<DownloadedPicture> bitmapList) {
            super.onPostExecute(bitmapList);
            bitmapCollection.setValue(bitmapList);
            Log.d(TAG, "onPostExecute: ");
        }
    }

    @Override
    public void setData(List<Bitmap> bitmapCollection) {

    }

    @Override
    public void keywordSubmitted(String keyword) {
        fetchDataTask = new FetchDataTask();
        fetchDataTask.execute(keyword);
    }

    public void itemClicked(int itemPosition) {
        mModel.setMVPData(itemPosition);
    }
}
