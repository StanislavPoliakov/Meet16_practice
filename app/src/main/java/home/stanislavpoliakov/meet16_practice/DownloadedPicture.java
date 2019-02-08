package home.stanislavpoliakov.meet16_practice;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.graphics.Bitmap;

/**
 * Класс данных, которые мы получаем из сети. Содержит описание картинки и саму картинку.
 * Описание не использую. Вся работа с картинкой.
 * Использую класс, как структуру данных, хотя знаю, что нужно закрыть переменные в private-поля,
 * создать для них getter/setter и переопределить Equals и HashCode. Но сейчас 4 утра, и я,
 * просидев "немало" времени с DataBinding, наконец разобрался как заставить работать app:srcCompat
 * для ImageView (почему он не находит сеттер для ImageView). Все работает, хочется спать и
 * браться уже за dagger2
 */
@BindingMethods({
        @BindingMethod(type = android.widget.ImageView.class,
                attribute = "app:srcCompat",
                method = "setImageBitmap") })
public class DownloadedPicture {
    public String description;
    public Bitmap bitmap;
}
