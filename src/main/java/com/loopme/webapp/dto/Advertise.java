package com.loopme.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Volodymyr Dema. Will see.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advertise {
    int id;
    String description;
    String url;
}
