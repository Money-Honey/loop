package com.loopme.webapp.model.dto;

import lombok.*;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdvertiseRequestEvent {
    String country;
    String os;
    Integer limit;
}
