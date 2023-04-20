package ris1b.todo_withdb.tasks;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TasksService {
    private final TasksRepository tasksRepository;
    private final ModelMapper modelMapper;

    public TasksService(TasksRepository tasksRepository, ModelMapper modelMapper) {
        this.tasksRepository = tasksRepository;
        this.modelMapper = modelMapper;
    }

    public List<TaskDto> getAllTasks(){
        /*
        * put into a stream, map it from task to TaskDto,
        * Collect into a List & return.
        * */
        return tasksRepository.findAll()
                .stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
    }

    public TaskDto getTaskById(Long id){
        var task = tasksRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        return modelMapper.map(task, TaskDto.class);
    }

    /*
    * Here we have to add a Task and add things one by one.
    * */
    public TaskDto createNewTask(TaskDto task){
        if (task.getDueDate() != null && task.getDueDate().before(new Date())) {
            throw new TaskInvalidException("Due date must be in the future");
        }

        // modelMapper: takes the task object and creates a TaskEntity.
        var taskEntity = modelMapper.map(task, TaskEntity.class); //from task to TaskEntity.class
        var savedTask = tasksRepository.save(taskEntity);
        return modelMapper.map(savedTask, TaskDto.class); // return (map->saved task back to TaskDto)
    }

    public TaskDto updateTaskById(Long id, TaskDto taskDto) {
        TaskEntity task = tasksRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        // TODO: Handle Not Found Exception

        if (taskDto.getDueDate() != null && taskDto.getDueDate().before(new Date())) {
            throw new TaskInvalidException("Due date must be in the future");
        }

        modelMapper.map(taskDto, task);
        TaskEntity updatedTask = tasksRepository.save(task);
        return modelMapper.map(updatedTask, TaskDto.class);
    }

    public void deleteAllTasks(){
        tasksRepository.deleteAll();
    }

    public void deleteTaskById(Long id) {
        TaskEntity task = tasksRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        tasksRepository.deleteById(id);
    }

    static class TaskNotFoundException extends IllegalArgumentException {
        public TaskNotFoundException(Long id) {
            super("Task with id " + id + " not found");
        }
    }

    static class TaskAlreadyExistsException extends IllegalArgumentException {
        public TaskAlreadyExistsException(Long id) {
            super("Task with id " + id + " already exists");
        }
    }

    static class TaskInvalidException extends IllegalArgumentException {
        public TaskInvalidException(String message) {
            super(message);
        }
    }
}
