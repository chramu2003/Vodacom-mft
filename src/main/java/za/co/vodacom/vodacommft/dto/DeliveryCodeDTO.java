package za.co.vodacom.vodacommft.dto;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.dto
 */


import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public class DeliveryCodeDTO {

    @NotBlank(message = "RouteShortName is mandatory")
    private String routeShortName;

    @NotBlank(message = "ConsumerCode is mandatory")
    private String consumerCode;
}
