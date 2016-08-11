package com.loopme.webapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Volodymyr Dema. Will see.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertiseBunch {
    List<Advertise> ads;
}
