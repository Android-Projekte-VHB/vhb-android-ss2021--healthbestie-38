package de.ur.mi.android.demos.healthbestie.dashboard.room;

import androidx.room.TypeConverter;
import java.util.Date;
import java.util.UUID;
import de.ur.mi.android.demos.healthbestie.dashboard.tasks.Task;


public class TaskAttributeTypeConverter {

    @TypeConverter
    public static Date millisecondsToDate(Long milliseconds) {
        return milliseconds == null ? null : new Date(milliseconds);
    }
    @TypeConverter
    public static Long dateToMilliseconds(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static UUID stringToUUID(String taskID_string) {
        return taskID_string == null ? null : UUID.fromString(taskID_string);
    }
    @TypeConverter
    public static String UUID_toString(UUID taskID) {
        return taskID == null ? null : taskID.toString();
    }


    @TypeConverter
    public static Task.TaskState intToTaskState(Integer taskStateInt) {
        return Task.TaskState.values()[taskStateInt];
    }
    @TypeConverter
    public static Integer taskStateToInt(Task.TaskState taskState) {
        return taskState.ordinal();
    }
}