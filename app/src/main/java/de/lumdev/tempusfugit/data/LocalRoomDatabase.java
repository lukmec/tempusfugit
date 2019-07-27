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

                    //create Trigger in SQLite DB, which contain part of business logic
                    // 1.1) --> set color of event, when color of parent group_event changes
                    db.execSQL("CREATE TRIGGER group_event_color_change AFTER UPDATE OF color ON group_event BEGIN UPDATE event SET color = new.color WHERE event.parent_id = new.id; END;");
                    // 1.2) --> set textColor of event, when textColor of parent group_event changes
                    db.execSQL("CREATE TRIGGER group_event_textcolor_change AFTER UPDATE OF text_color ON group_event BEGIN UPDATE event SET text_color = new.text_color WHERE event.parent_id = new.id; END;");
                    // 1.3) --> set icon of event, when icon of parent group_event changes
                    db.execSQL("CREATE TRIGGER group_event_icon_change AFTER UPDATE OF icon ON group_event BEGIN UPDATE event SET icon = new.icon WHERE event.parent_id = new.id; END;");
                    // 1.4) --> set archive state of event, when archive state of parent group_event changes
                    db.execSQL("CREATE TRIGGER group_event_archived_change AFTER UPDATE OF archived ON group_event BEGIN UPDATE event SET archived = new.archived WHERE event.parent_id = new.id; END;");
                    // 2.1) --> set color of event, when new event is added, or when event is updated
                    db.execSQL("CREATE TRIGGER set_event_color_on_insert AFTER INSERT ON event BEGIN UPDATE event SET color = (SELECT color FROM group_event WHERE event.parent_id = group_event.id) WHERE EXISTS (SELECT color FROM group_event WHERE event.parent_id = group_event.id); END;");
                    db.execSQL("CREATE TRIGGER set_event_color_on_update AFTER UPDATE OF parent_id ON event BEGIN UPDATE event SET color = (SELECT color FROM group_event WHERE event.parent_id = group_event.id) WHERE EXISTS (SELECT color FROM group_event WHERE event.parent_id = group_event.id); END;");
                    // 2.2) --> set textColor of event, when new event is added, or when event is updated
                    db.execSQL("CREATE TRIGGER set_event_textcolor_on_insert AFTER INSERT ON event BEGIN UPDATE event SET text_color = (SELECT text_color FROM group_event WHERE event.parent_id = group_event.id) WHERE EXISTS (SELECT text_color FROM group_event WHERE event.parent_id = group_event.id); END;");
                    db.execSQL("CREATE TRIGGER set_event_textcolor_on_update AFTER UPDATE OF parent_id ON event BEGIN UPDATE event SET text_color = (SELECT text_color FROM group_event WHERE event.parent_id = group_event.id) WHERE EXISTS (SELECT text_color FROM group_event WHERE event.parent_id = group_event.id); END;");
                    // 2.3) --> set icon of event, when new event is added, or when event is updated
                    db.execSQL("CREATE TRIGGER set_event_icon_on_insert AFTER INSERT ON event BEGIN UPDATE event SET icon = (SELECT icon FROM group_event WHERE event.parent_id = group_event.id) WHERE EXISTS (SELECT icon FROM group_event WHERE event.parent_id = group_event.id); END;");
                    db.execSQL("CREATE TRIGGER set_event_icon_on_update AFTER UPDATE OF parent_id ON event BEGIN UPDATE event SET icon = (SELECT icon FROM group_event WHERE event.parent_id = group_event.id) WHERE EXISTS (SELECT icon FROM group_event WHERE event.parent_id = group_event.id); END;");
                    // 3) --> calculate priority when importance or urgency changes
                    db.execSQL("CREATE TRIGGER event_calc_priority_after_importance_change AFTER UPDATE OF importance ON event BEGIN UPDATE event SET priority = new.importance + new.urgency WHERE event.id = new.id; END;");
                    db.execSQL("CREATE TRIGGER event_calc_priority_after_urgency_change AFTER UPDATE OF urgency ON event BEGIN UPDATE event SET priority = new.importance + new.urgency WHERE event.id = new.id; END;");
                    // 4) --> calculate group_event progress when new event is added, or when event is set to done (only consider events, that are not archived)
                    db.execSQL("CREATE TRIGGER group_event_calc_progress_on_insert AFTER INSERT ON event BEGIN UPDATE group_event SET progress = round((SELECT count(event.id) FROM event WHERE event.parent_id = new.parent_id AND event.done ='1' AND event.archived = '0')/((SELECT count(event.id) FROM event WHERE event.parent_id = new.parent_id AND event.archived = '0')*1.0)*100) WHERE id = new.parent_id; END;");
                    db.execSQL("CREATE TRIGGER group_event_calc_progress_on_update AFTER UPDATE OF done ON event BEGIN UPDATE group_event SET progress = round((SELECT count(event.id) FROM event WHERE event.parent_id = new.parent_id AND event.done ='1' AND event.archived = '0')/((SELECT count(event.id) FROM event WHERE event.parent_id = new.parent_id AND event.archived = '0')*1.0)*100) WHERE id = new.parent_id; END;");
                    // 5) --> delete all corresponding events, when a groupEvent is deleted
                    db.execSQL("CREATE TRIGGER delete_events_on_group_event_deletion AFTER DELETE ON group_event BEGIN DELETE FROM event WHERE event.parent_id = old.id; END;");

                    //populate when db is created (db is created with installation; reinstall app to recreate db)
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
            eDao.deleteAll();

            //get Colors
            int organge = context.getResources().getColor(R.color.groupEvent_orange);
            int blue = context.getResources().getColor(R.color.groupEvent_blue);
            int red = context.getResources().getColor(R.color.groupEvent_red);
            int green = context.getResources().getColor(R.color.groupEvent_green);
            int black = context.getResources().getColor(R.color.tf_black);

            //Create Test Data
            GroupEvent gE1 = new GroupEvent("Telematikprojekt", "Meine Aufgaben des Telematikprojektes", organge, black, R.drawable.ic_edit_black_24dp, -1);
            GroupEvent gE2 = new GroupEvent("Einkaufen", "Meine Einkaufsliste", blue, black, R.drawable.ic_shopping_cart_black_24dp, -1);
            GroupEvent gE3 = new GroupEvent("Sport", "geplante Trainingseinheiten", red, black, R.drawable.ic_fitness_center_black_24dp, -1);
            GroupEvent gE4 = new GroupEvent("Autoreparatur", "Muss ich noch vor nächstem TÜV überprüfen", green, black, R.drawable.ic_directions_car_black_24dp, -1);
            gEDao.insert(gE1);
            gEDao.insert(gE2);
            gEDao.insert(gE3);
            gEDao.insert(gE4);

            Event e1_1 = new Event("Einleitung schreiben", " ", 1);
            Event e1_2 = new Event("Literaturquellen raussuchen", " ", 1);
            Event e1_3 = new Event("Gedanken zur Entwickler- Dokumentation machen", " ", 1);
            Event e1_4 = new Event("mit Betreuern sprechen", " ", 1);
            e1_1.done = true;
            e1_2.done = true;
            e1_1.importance = 1;
            e1_1.urgency = 1;
            e1_2.importance = 1;
            e1_2.urgency = 1;
            e1_3.importance = 1;
            e1_3.urgency = 1;
            e1_4.importance = 1;
            e1_4.urgency = 1;
//            e1_1.priority = 3.5;
//            e1_2.priority = 1.4;
//            e1_3.priority = 12.8;
//            e1_4.priority = 5.6;
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
            e2_1.importance = 1;
            e2_1.urgency = 1;
            e2_2.importance = 1;
            e2_2.urgency = 1;
            e2_3.importance = 1;
            e2_3.urgency = 1;
            e2_4.importance = 1;
            e2_4.urgency = 1;
//            e2_1.priority = 3.5;
//            e2_2.priority = 1.4;
//            e2_3.priority = 12.8;
//            e2_4.priority = 5.6;
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
            e3_1.importance = 1;
            e3_1.urgency = 1;
            e3_2.importance = 1;
            e3_2.urgency = 1;
//            e3_1.priority = 2.9;
//            e3_2.priority = 7.4;
            e3_1.color = context.getResources().getColor(R.color.groupEvent_red);
            e3_2.color = context.getResources().getColor(R.color.groupEvent_red);
            eDao.insert(e3_1);
            eDao.insert(e3_2);

            Event e4_1 = new Event("neue Bremsscheiben besorgen", "wegen der HU", 4);
            Event e4_2 = new Event("Reifen online bestellen", "noch vor dem Winter", 4);
            e4_1.importance = 1;
            e4_1.urgency = 1;
            e4_2.importance = 1;
            e4_2.urgency = 1;
//            e4_1.priority = 1.9;
//            e4_2.priority = 8.4;
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


