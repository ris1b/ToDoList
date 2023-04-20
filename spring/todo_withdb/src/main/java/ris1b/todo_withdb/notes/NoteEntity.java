package ris1b.todo_withdb.notes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ris1b.todo_withdb.common.BaseEntity;
import ris1b.todo_withdb.tasks.TaskEntity;

@Entity(name = "notes")  // This class represents a Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteEntity extends BaseEntity {

    @Column(name = "title", nullable = false, length = 100)
    String title;

    @Column(name = "body", nullable = false, length = 1000)
    String body;
    // making length 1000, so automatically it won't be able to use varchar(255)
    // and end up using text data type.

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")           // the join column will be called task_id
    TaskEntity task;


}








