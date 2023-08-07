package ru.practicum.mainService.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comment_blames")
public class CommentBlame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Override
    public String toString() {
        return "CommentBlame{" +
                "id=" + id +
                ", userId=" + user.getId() +
                ", eventId=" + comment.getId() +
                '}';
    }
}
