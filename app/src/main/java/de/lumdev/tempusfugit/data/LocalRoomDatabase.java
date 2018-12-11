package de.lumdev.tempusfugit.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import de.lumdev.tempusfugit.R;

@Database(entities = {GroupEvent.class, Event.class}, version = 1, exportSchema = false)
@TypeConverters({DateTimeTypeConverter.class})
public abstract class LocalRoomDatabase extends RoomDatabase {

    public abstract GroupEventDao groupEventDao();
    public abstract EventDao eventDao();
    public Context context;

    //class is implemented as singleton here
    private static volatile LocalRoomDatabase INSTANCE;

    static LocalRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalRoomDatabase.class) {
                if (INSTANCE == null) {
                    //database is created here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalRoomDatabase.class, "local_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                    LocalRoomDatabase.INSTANCE.context = context;
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final GroupEventDao gEDao;
        private final EventDao eDao;
        private final Context context;

        PopulateDbAsync(LocalRoomDatabase db) {
            gEDao = db.groupEventDao();
            eDao = db.eventDao();
            context = db.context;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            gEDao.deleteAll();
//            GroupEvent groupEvent = new GroupEvent("Telematikprojekt", "Meine Aufgaben des Telematikprojektes");
            GroupEvent gE1 = new GroupEvent("Telematikprojekt", "Meine Aufgaben des Telematikprojektes", context.getResources().getColor(R.color.groupEvent_orange), R.drawable.ic_edit_black_24dp, -1);
            GroupEvent gE2 = new GroupEvent("Einkaufen", "Meine Einkaufsliste", context.getResources().getColor(R.color.groupEvent_blue), R.drawable.ic_shopping_cart_black_24dp, -1);
            GroupEvent gE3 = new GroupEvent("Sport", "geplante Trainingseinheiten", context.getResources().getColor(R.color.groupEvent_red), R.drawable.ic_fitness_center_black_24dp, -1);
            GroupEvent gE4 = new GroupEvent("Autoreparatur", "Muss ich noch vor nächstem TÜV überprüfen", context.getResources().getColor(R.color.groupEvent_green), R.drawable.ic_directions_car_black_24dp, -1);
            gEDao.insert(gE1);
            gEDao.insert(gE2);
            gEDao.insert(gE3);
            gEDao.insert(gE4);
            return null;
        }
    }
}


