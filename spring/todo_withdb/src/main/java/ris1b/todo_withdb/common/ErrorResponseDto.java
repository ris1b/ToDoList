package ris1b.todo_withdb.common;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class ErrorResponseDto {
    @NonNull
    private String message;
}
