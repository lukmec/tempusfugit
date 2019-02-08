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
                            .addCallback(onCreateRoomDatabaseCallback)
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
                    //delete all data and populate with fresh test data --- excuted everytime the app starts (== db is opened)
//                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static RoomDatabase.Callback onCreateRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onCreate (@NonNull SupportSQLiteDatabase db){
                    super.onCreate(db);
                    //populate when db is created (db is created with installation; reinstall app to recreate db)
                    new PopulateDbAsync(INSTANCE).execute();

                    //create Trigger in SQLite DB, which contain part of business logic
                    // 1) --> set color of event, when color of parent group_event changes
                    db.execSQL("CREATE TRIGGER group_event_color_change AFTER UPDATE OF color ON group_event BEGIN UPDATE event SET color = new.color WHERE event.parent_id = new.id; END;");
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
            eDao.deleteAll();

            //Create Test Data
            GroupEvent gE1 = new GroupEvent("Telematikprojekt", "Meine Aufgaben des Telematikprojektes", context.getResources().getColor(R.color.groupEvent_orange), R.drawable.ic_edit_black_24dp, -1);
            GroupEvent gE2 = new GroupEvent("Einkaufen", "Meine Einkaufsliste", context.getResources().getColor(R.color.groupEvent_blue), R.drawable.ic_shopping_cart_black_24dp, -1);
            GroupEvent gE3 = new GroupEvent("Sport", "geplante Trainingseinheiten", context.getResources().getColor(R.color.groupEvent_red), R.drawable.ic_fitness_center_black_24dp, -1);
            GroupEvent gE4 = new GroupEvent("Autoreparatur", "Muss ich noch vor nächstem TÜV überprüfen", context.getResources().getColor(R.color.groupEvent_green), R.drawable.ic_directions_car_black_24dp, -1);
            gEDao.insert(gE1);
            gEDao.insert(gE2);
            gEDao.insert(gE3);
            gEDao.insert(gE4);

            Event e1_1 = new Event("Einleitung schreiben", " ", 1);
            Event e1_2 = new Event("Literaturquellen raussuchen", " ", 1);
            Event e1_3 = new Event("Gedanken zur Entwickler- Dokumentation machen", " ", 1);
            Event e1_4 = new Event("mit Betreuern sprechen", " ", 1);
            e1_1.done = false;
            e1_1.priority = 3.5;
            e1_2.priority = 1.4;
            e1_3.priority = 12.8;
            e1_4.priority = 5.6;
            e1_1.color = context.getResources().getColor(R.color.groupEvent_orange);
            e1_2.color = context.getResources().getColor(R.color.groupEvent_orange);
            e1_3.color = context.getResources().getColor(R.color.groupEvent_orange);
            e1_4.color = context.getResources().getColor(R.color.groupEvent_orange);
            eDao.insert(e1_1);
            eDao.insert(e1_2);
            eDao.insert(e1_3);
            eDao.insert(e1_4);

            Event e2_1 = new Event("Brot, Käse, Salat, Tomaten einkaufen", "sonst hab ich genügen Nahrungsmittel da", 2);
            Event e2_2 = new Event("Geburtstagsgeschenk für Oma kaufen", "sie wünscht sich einen neuen Rollator", 2);
            Event e2_3 = new Event("Neue Taschenlampe für Keller kaufen", "da ist es immer so dunkel drin", 2);
            Event e2_4 = new Event("Irgendwas wichtiges, was ich vergessen hab, kaufen", "Hier steht irgendwas zum Testen drin.", 2);
            e2_1.priority = 3.5;
            e2_2.priority = 1.4;
            e2_3.priority = 12.8;
            e2_4.priority = 5.6;
            e2_1.color = context.getResources().getColor(R.color.groupEvent_blue);
            e2_2.color = context.getResources().getColor(R.color.groupEvent_blue);
            e2_3.color = context.getResources().getColor(R.color.groupEvent_blue);
            e2_4.color = context.getResources().getColor(R.color.groupEvent_blue);
            eDao.insert(e2_1);
            eDao.insert(e2_2);
            eDao.insert(e2_3);
            eDao.insert(e2_4);

            Event e3_1 = new Event("Trainingssachen mit große Tasche umpacken", "in der kleinen ist kein Platz mehr", 3);
            Event e3_2 = new Event("Klettern gehen", "hat letzte mal so viel Spaß gemacht", 3);
            e3_1.priority = 2.9;
            e3_2.priority = 7.4;
            e3_1.color = context.getResources().getColor(R.color.groupEvent_red);
            e3_2.color = context.getResources().getColor(R.color.groupEvent_red);
            eDao.insert(e3_1);
            eDao.insert(e3_2);

            Event e4_1 = new Event("neue Bremsscheiden besorgen", "wegen der HU", 4);
            Event e4_2 = new Event("Reifen online bestellen", "noch vor dem Winter", 4);
            e4_1.priority = 1.9;
            e4_2.priority = 8.4;
            e4_1.color = context.getResources().getColor(R.color.groupEvent_green);
            e4_2.color = context.getResources().getColor(R.color.groupEvent_green);
            eDao.insert(e4_1);
            eDao.insert(e4_2);

            //Create TestData for Performance Test
//            for (int i = 0; i < 20 ; i++) {
//                gEDao.insert(new GroupEvent("Test Group", "Das ist hier nur zum Testen", context.getResources().getColor(R.color.groupEvent_orange), R.drawable.ic_edit_black_24dp, -1));
//            }
//            for (int i = 0; i < 20 ; i++) {
//                eDao.insert( new Event("Test Event -------------------------------------------------------------------------- " +
//                        "---------------------------------------------------------------------------------------------------------- ", "Das ist hier nur zum Testen", 2));
//            }

            return null;
        }
    }
}


