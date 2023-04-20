package ris1b.todo_withdb.tasks;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ris1b.todo_withdb.common.ErrorResponseDto;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    private TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping("/")
    ResponseEntity<List<TaskDto>> getAllTasks() {
        var tasks = tasksService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /*
    * When creating a new task also, we expect the request body to be TaskDto.
    * */
    @PostMapping("/")
    ResponseEntity<TaskDto> createNewTask(@RequestBody TaskDto task) {
        var savedTask = tasksService.createNewTask(task);
        /*Created and it returns a URI
        * When we make something as Created, we should pass a URI
        * The URI tells the Client where to find the Entity which was created.
        * */
        return ResponseEntity.created(URI.create("/tasks/" + savedTask.getId())).body(savedTask);
    }

    @GetMapping("/{id}")
    ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        var task = tasksService.getTaskById(id);
        return ResponseEntity.ok(task);  // response entity is okay, and we've put tasks into it.
    }

    @PutMapping("/{id}")
    ResponseEntity<TaskDto> updateTaskById(@PathVariable Long id, @RequestBody TaskDto task) {
        var updatedTask = tasksService.updateTaskById(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteTaskById(@PathVariable Long id) {
        tasksService.deleteTaskById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted task with ID " + id + " successfully");
    }

    @DeleteMapping("/")
    ResponseEntity<String> deleteAllTasks(){
        tasksService.deleteAllTasks();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted all Tasks successfully");
    }

    @ExceptionHandler({
            TasksService.TaskNotFoundException.class,
            TasksService.TaskAlreadyExistsException.class,
            TasksService.TaskInvalidException.class
    })
    public ResponseEntity<ErrorResponseDto> handleError(Exception e) {
        HttpStatus errorStatus;

        if (e instanceof TasksService.TaskNotFoundException) {
            errorStatus = HttpStatus.NOT_FOUND;
        } else if (e instanceof TasksService.TaskAlreadyExistsException) {
            errorStatus = HttpStatus.CONFLICT;
        } else if (e instanceof TasksService.TaskInvalidException) {
            errorStatus = HttpStatus.BAD_REQUEST;
        } else {
            errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity
                .status(errorStatus)
                .body(new ErrorResponseDto(e.getMessage()));
    }

}
