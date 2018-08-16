package org.wingstudio.blog.po;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity @Setter @Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Comment {

    public Comment(Long id){
        this.id=id;
    }

    public Comment(User user,@NotEmpty(message = "评论内容不能为空") @Size(min = 2, max = 500) String content) {
        this.content = content;
        this.user = user;
    }

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; // 用户的唯一标识

    @NotEmpty(message = "评论内容不能为空")
    @Size(min=2, max=500)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp createTime;

}
