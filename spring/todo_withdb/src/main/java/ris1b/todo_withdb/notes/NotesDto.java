package ris1b.todo_withdb.notes;

import lombok.Data;

@Data
public class NotesDto {

    private Long id;
    private String title;
    private String body;
    private Long taskId;

}
