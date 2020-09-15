package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Quiz {
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Text is mandatory")
    private String text;
    @NotNull
    @Size(min = 2,message = "Options is mandatory")
    private String[] options;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int[] answer;
    @Id
    private int id;

    public Quiz() {
    }

    public Quiz(String title, String text, String[] options, int[] answer, int id) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.id = id;
    }

    @JsonIgnore
    public int[] getAnswer() {
        return answer;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String[] getOptions() {
        return options;
    }
}