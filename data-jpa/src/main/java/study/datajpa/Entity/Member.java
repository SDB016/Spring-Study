package study.datajpa.Entity;

import lombok.*;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","username","age"})
@NamedQuery(name = "Member.findByUsername",
        query = "select m from Member m where m.username =: username")
@NamedEntityGraph(name = "Member.WithTeam",
        attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String name, int age, Team team) {
        this.username = name;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String name, int age) {
        this.username = name;
        this.age = age;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
