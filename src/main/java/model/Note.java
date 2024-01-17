package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Note {
    private int id;
    private String title;
    private String description;
    private int userId;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }
}