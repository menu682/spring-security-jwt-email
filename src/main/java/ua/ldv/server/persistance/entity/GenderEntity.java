package ua.ldv.server.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "gender")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GenderEntity extends BaseEntity{

    @Column(name = "gender")
    private String gender;

}
