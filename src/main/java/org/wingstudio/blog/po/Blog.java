package org.wingstudio.blog.po;

import com.github.rjeschke.txtmark.Processor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Entity @Setter @Getter @NoArgsConstructor
public class Blog {

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; // 用户的唯一标识

    @NotEmpty(message = "标题不能为空")
    @Size(min=2, max=50)
    @Column(nullable = false, length = 50) // 映射为字段，值不能为空
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(min=2, max=300)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String summary;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;

    public void setContent(String content){
        this.content=content;
        this.htmlContent=Processor.process(content);
    }

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String htmlContent; // 将 md 转为 html

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp createTime;

    @Column(name="readSize")
    private Integer readSize = 0; // 访问量、阅读量

    @Column(name="commentSize")
    private Integer commentSize = 0;  // 评论量

    @Column(name="voteSize")
    private Integer voteSize = 0;  // 点赞量


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> comments;

    public void setComments(List<Comment> comments){
        this.comments=comments;
        this.commentSize=comments.size();
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
        this.commentSize=this.comments.size();
    }

    public void removeComment(Long commentId){
        comments.remove(new Comment(commentId));
        this.commentSize=comments.size();
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "blog_vote", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "id"))
    private List<Vote> votes;

    public void setVotes(List<Vote> votes){
        this.votes=votes;
        this.voteSize=votes.size();
    }

    public boolean addVote(Vote vote){
        boolean isExist=this.votes.contains(vote);
        if(!isExist){
            this.votes.add(vote);
            this.voteSize=this.votes.size();
        }
        return isExist;
    }

    public void removeVote(Long voteId){
        this.votes.remove(new Vote(voteId));
        this.voteSize=votes.size();
    }

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="catalog_id")
    private Catalog catalog;

    @Column(name="tags", length = 100)
    private String tags;  // 标签

    public Blog(String title, String summary,String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

}
