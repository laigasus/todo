package api.crusher.todo.domain;


import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Builder
@Entity
@Getter
@Comment("태스크")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @Tsid
    @Column(updatable = false, nullable = false)
    @Comment("ID")
    private Long id;

    @Comment("제목")
    @Column(nullable = false)
    private String title;

    @Comment("설명")
    @Column(nullable = false)
    private String description;

    @Comment("상태")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'TODO'")
    private TaskState state;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("생성일")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("수정일")
    private Date updatedAt;

    @Version
    @ColumnDefault("1")
    private Long version;

    public Task update(Task task) {
        this.title = task.title != null ? task.title : this.title;
        this.state = task.state != null ? task.state : this.state;
        this.description = task.description != null ? task.description : this.description;
        return this;
    }

}
