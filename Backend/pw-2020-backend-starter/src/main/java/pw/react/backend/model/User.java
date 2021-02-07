package pw.react.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pw.react.backend.utils.JsonDateDeserializer;
import pw.react.backend.utils.JsonDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {

    public static User EMPTY = new User();

    public User() {}
    public User (String log, String pass, String key, String first_name, String second_name)
    {
        this.user_login = log;
        this.user_password = pass;
        this.user_key = key;
        this.first_name = first_name;
        this.second_name = second_name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "user_login")
    private String user_login;
    @Column(name = "user_password")
    private String user_password;
    @Column(name = "user_key")
    private String user_key;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "second_name")
    private String second_name;
}
