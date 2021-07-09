package za.co.vodacom.vodacommft.controller;
/**
 * @author jan & modified by mz herbie on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.dto
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import za.co.vodacom.vodacommft.dto.DeliveryCodeDTO;
import za.co.vodacom.vodacommft.service.IFileDeliveryService;
import za.co.vodacom.vodacommft.service.impl.FileDeliveryService;

import javax.validation.Valid;


@RestController
public class DeliveryController {

    private IFileDeliveryService deliveryService;

    public DeliveryController(FileDeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping(path="/deliver")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public ResponseEntity updateTodo(@Valid @RequestBody DeliveryCodeDTO deliveryCodeDTO){
        deliveryProcessing(deliveryCodeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void deliveryProcessing(DeliveryCodeDTO deliveryCodeDTO) {
        deliveryService.deliveryProcessing(deliveryCodeDTO.getConsumerCode(), deliveryCodeDTO.getRouteShortName());
    }
}
