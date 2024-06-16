package soulasphyxia.videostorageapi.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "hashtags")
@NoArgsConstructor
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    @JsonIgnore
    private Long id;
    @Column(name = "name")
    private String name;

    public HashTag(String name){
        this.name = name;
    }
}
