package com.carve.cravex.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailEvent {
    private String receiverEmail;
    private String subject;
    private String message;
//    private String orderId;
//    private String name;
}
