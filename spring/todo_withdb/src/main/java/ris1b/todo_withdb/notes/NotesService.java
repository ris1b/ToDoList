package ris1b.todo_withdb.notes;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ris1b.todo_withdb.tasks.TaskEntity;
import ris1b.todo_withdb.tasks.TasksRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotesService {
    private final NotesRepository notesRepository;
    private final TasksRepository tasksRepository;
    private final ModelMapper modelMapper;

    public NotesService(NotesRepository notesRepository, TasksRepository tasksRepository, ModelMapper modelMapper) {
        this.notesRepository = notesRepository;
        this.tasksRepository = tasksRepository;
        this.modelMapper = modelMapper;
    }


    // get all the Notes, in a Task
    public List<NotesDto> getAllNotesByTaskId(Long taskId) {
        TaskEntity task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        return task.getNotes()
                .stream()
                .map(note -> modelMapper.map(note, NotesDto.class))
                .collect(Collectors.toList());
    }

    // get a particular Note in a Task
    public NotesDto getNoteByTaskAndNoteId(Long taskId, Long noteId) {
        TaskEntity task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        NoteEntity note = task.getNotes()
                .stream()
                .filter(n -> n.getId().equals(noteId))
                .findFirst()
                .orElseThrow(() -> new NoteNotFoundException(noteId));
        return modelMapper.map(note, NotesDto.class);
    }

    public NotesDto createNoteByTaskId(Long taskId, NotesDto noteDto){
        TaskEntity task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        var noteEntity = modelMapper.map(noteDto, NoteEntity.class);
        noteEntity.setTask(task);

        var savedNote = notesRepository.save(noteEntity);
        System.out.println("Note saved !!!----");
        return modelMapper.map(savedNote, NotesDto.class);
    }

    // update a Note, by TaskId and NoteId
    public NotesDto updateNoteById(Long taskId, Long noteId, NotesDto noteDto) {
        TaskEntity task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        NoteEntity note = task.getNotes().stream()
                .filter(n -> n.getId().equals(noteId))
                .findFirst()
                .orElseThrow(() -> new NoteNotFoundException(noteId));

        note.setTitle(noteDto.getTitle());
        note.setBody(noteDto.getBody());

        var updatedNote = notesRepository.save(note);

        return modelMapper.map(updatedNote, NotesDto.class);
    }


    // delete a Note, in a Task --> Does not return the Deleted Note.
    public void deleteNoteById(Long taskId, Long noteId) {
        TaskEntity task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        var note = task.getNotes().stream()
                .filter(n -> n.getId().equals(noteId))
                .findFirst()
                .orElseThrow(() -> new NoteNotFoundException(noteId));

        task.getNotes().remove(note);

        notesRepository.deleteById(note.getId());
    }

    // delete all Notes, in a Task
    public void deleteAllNoteById(Long taskId) {
        TaskEntity task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        task.getNotes().clear();

        notesRepository.deleteAllByTaskId(taskId);
//        notesRepository.deleteAll();
    }

    static class NoteNotFoundException extends IllegalArgumentException {
        public NoteNotFoundException(Long id) {
            // TODO: add task id also !
            super("Note with id " + id );
        }
    }

    static class TaskNotFoundException extends IllegalArgumentException {
        public TaskNotFoundException(Long id) {
            super("Task with id " + id + " not found");
        }
    }
}
