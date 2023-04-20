package ris1b.todo_withdb.tasks;

//import javax.persistence.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ris1b.todo_withdb.common.BaseEntity;
import ris1b.todo_withdb.notes.NoteEntity;

import java.util.Date;
import java.util.List;

@Entity(name = "tasks")  // This class represents a Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "due_date", nullable = false)
    Date dueDate;

    @Column(name = "done", nullable = false, columnDefinition = "boolean default false")
    boolean done;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    List<NoteEntity> notes;
}
