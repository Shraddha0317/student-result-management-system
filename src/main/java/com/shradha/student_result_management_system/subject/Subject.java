package com.shradha.student_result_management_system.subject;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "subjects",
     uniqueConstraints =   @UniqueConstraint(name ="subject_code",
                           columnNames = "subject_code")
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long  id;

    @Column(
            name = "subject_code",
            nullable = false,
            length = 20,
            unique = true
    )

   private String subjectCode;

    //NOT NULL, max 100 chars

    @Column(
            name = "subject_name",
            nullable = false,
            length = 100
    )
   private String subjectName;

    //   NOT NULL, must be > 0
      @Column(
              name = "max_marks",
              nullable = false
      )
     @Positive
    private Integer maxMarks;


      @NotNull
      @Column(length = 250)
    private  String description;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
   private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
   private LocalDateTime updatedAt;
}
