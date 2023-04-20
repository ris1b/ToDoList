package ris1b.todo_withdb.notes;


import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/notes")
public class NotesController {
    private final NotesService notesService;
    private final ModelMapper modelMapper;

    public NotesController(NotesService notesService, ModelMapper modelMapper) {
        this.notesService = notesService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<NotesDto>> getAllNotesByTaskId(@PathVariable Long taskId) {
        List<NotesDto> notes = notesService.getAllNotesByTaskId(taskId);

        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NotesDto> getNoteByTaskAndNoteId(@PathVariable Long taskId, @PathVariable Long noteId) {
        NotesDto note = notesService.getNoteByTaskAndNoteId(taskId, noteId);

        return ResponseEntity.ok(note);
    }

    @PostMapping("/")
    public ResponseEntity<NotesDto> addNoteToTask(@PathVariable Long taskId, @RequestBody NotesDto noteDto) {
        NotesDto createdNote = notesService.createNoteByTaskId(taskId, noteDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }


    @PutMapping("/{noteId}")
    public ResponseEntity<NotesDto> updateNoteById(@PathVariable Long taskId, @PathVariable Long noteId, @RequestBody NotesDto noteDto) {
        NotesDto updatedNote = notesService.updateNoteById(taskId, noteId, noteDto);
        return ResponseEntity.ok(updatedNote);
    }


    @DeleteMapping("/{noteId}")
    public ResponseEntity<String> deleteNoteById(@PathVariable Long taskId, @PathVariable Long noteId) {
        notesService.deleteNoteById(taskId, noteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted note with ID " + noteId + " from task with ID " + taskId + " successfully");
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteAllNoteById(@PathVariable Long taskId) {
        notesService.deleteAllNoteById(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted all notes from task with ID " + taskId + " successfully");
    }

}