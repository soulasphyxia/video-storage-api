package soulasphyxia.videostorageapi.model;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PostDTO {

    private String title;

    private String content;

    private Date createdAt;

    private String data;

}
