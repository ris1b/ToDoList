package ris1b.todo_withdb.tasks;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.Date;

/*We have created this DTO for all kinds of Data Transfer !*/
@Data
public class TaskDto {

    @Nullable
    Long id;
    @Nullable
    String name;
    @Nullable
    Date dueDate;
    Boolean done;
}
