package ris1b.todo_withdb.tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

@DataJpaTest
public class TasksRepositoryTests {
    @Autowired
    private TasksRepository tasksRepository;

    @Test
    public void canCreateTasks(){
        TaskEntity task = new TaskEntity();
        task.name = "test task";
        task.dueDate = new Date();
        tasksRepository.save(task);

            Assertions.assertEquals("test task", tasksRepository.findAll().get(0).name);
    }
}
