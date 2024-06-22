package com.volunteer.main.model.response;

import com.volunteer.main.model.request.DonationDTO;
import lombok.Data;

@Data
public class DonationResponseDTO {
    protected ResponseStatus responseStatus;
    protected DonationDTO donationDTO;
}
