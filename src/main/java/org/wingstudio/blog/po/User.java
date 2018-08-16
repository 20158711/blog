package org.wingstudio.blog.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Setter @Getter
    private Long id; // 用户的唯一标识

    @NotEmpty(message = "姓名不能为空")
    @Size(min=2, max=20)
    @Column(nullable = false, length = 20) // 映射为字段，值不能为空
    @Setter @Getter
    private String name;

    @NotEmpty(message = "邮箱不能为空")
    @Size(max=50)
    @Email(message= "邮箱格式不对" )
    @Column(nullable = false, length = 50, unique = true)
    @Setter @Getter
    private String email;

    @NotEmpty(message = "账号不能为空")
    @Size(min=3, max=20)
    @Column(nullable = false, length = 20, unique = true)
    @Setter @Getter
    private String username; // 用户账号，用户登录时的唯一标识

    @NotEmpty(message = "密码不能为空")
    @Size(max=100)
    @Column(length = 100)
    @Setter @Getter
    private String password; // 登录时密码

    public void encoderPassword(String password){
        this.password=new BCryptPasswordEncoder().encode(password);
    }

    @Column(length = 200)
    @Setter @Getter
    private String avatar; // 头像图片地址

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    @Setter @Getter
    private List<Authority> authorities;

    public String getAuths(){
        if (authorities==null)
            return "";
        StringBuilder builder=new StringBuilder();
        for (Authority authority : authorities) {
            builder.append(authority.getAuthority()).append(",");
        }
        builder.replace(builder.length()-1,builder.length(),"");
        return builder.toString();
    }


    public User(String name, String email,String username,String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
