package com.loopme.webapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertiseBunch {
    List<Advertise> ads;
}
