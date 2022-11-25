package com.habit.thehabit.record.command.app.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecordAdminDTO {

    private Long recordCode;
    private String bookName;
    private String bookISBN;
    private String bookAuthor;
    private String name;

}
